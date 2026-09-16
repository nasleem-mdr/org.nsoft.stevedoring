package org.nsoft.stevedoring.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.MSysConfig;
import org.nsoft.stevedoring.model.MStevStatementOfFact;
import org.nsoft.stevedoring.model.MStevVesselSchedule;

/**
 * Logika "Auto-Adjustment & Delivery" yang berjalan saat
 * {@link MStevStatementOfFact} di-Complete:
 *
 *  1. Update QtyOrdered pada C_OrderLine sesuai realisasi tonase SoF,
 *     PER PRODUK (satu SPK bisa punya beberapa line/komoditas berbeda —
 *     lihat {@link MStevStatementOfFact#getRealizedQtyByProduct()}).
 *  2. Terbitkan SATU M_InOut (header), dengan SATU M_InOutLine per
 *     produk yang direalisasikan, lalu di-Complete.
 *
 * KEPUTUSAN DESAIN: pembuatan C_Invoice SENGAJA TIDAK dilakukan di sini.
 * Setelah M_InOut Complete, penerbitan invoice diserahkan sepenuhnya ke
 * prosedur standar iDempiere:
 *   - C_BPartner.InvoiceRule menentukan kapan customer boleh di-invoice
 *     (Immediate / After Delivery / Customer Scheduled After Delivery),
 *   - Proses batch bawaan "Generate Invoices (Manual)" / scheduler
 *     C_InvoiceBatch mengambil M_InOut yang sudah Complete dan belum
 *     ter-invoice, lalu men-generate C_Invoice — termasuk untuk customer
 *     yang minta invoice digabung bulanan (banyak M_InOut -> 1 Invoice).
 * Ini menghindari SoFFinanceService memaksakan 1 SoF = 1 Invoice, yang
 * akan bentrok dengan kebutuhan invoicing batch/periodik semacam itu.
 *
 * Kode Faktur Pajak (mis. "07" untuk fasilitas FTZ Batam, kolom custom
 * C_Invoice.STEV_FakturPajakKode) TIDAK di-set otomatis di sini karena
 * invoice-nya sendiri tidak dibuat di sini. Kalau kode itu perlu otomatis
 * terisi juga saat invoice di-generate lewat proses batch standar,
 * sarankan didefault dari C_BPartner (mis. flag "Customer FTZ Batam")
 * lewat ModelValidator kecil terpisah di C_Invoice — BUKAN tanggung
 * jawab plugin Stevedoring ini.
 *
 * SEMUA dijalankan dalam trx yang sama dengan proses Complete SoF
 * (get_TrxName() dari SoF), sehingga jika salah satu langkah gagal,
 * seluruh Complete SoF ikut di-rollback oleh DocumentEngine.
 */
public class SoFFinanceService
{
    private static final CLogger log = CLogger.getCLogger(SoFFinanceService.class);

    /**
     * Nama DocType Shipment "Jasa" (non-inventory delivery) yang dipakai
     * proses ini. Disimpan sebagai M_SysConfig agar configurable per
     * client tanpa deploy ulang, mengikuti pola
     * AUTOPRICE_SPIKE_THRESHOLD_PCT pada plugin autoprice.
     */
    private static final String SYSCONFIG_SHIPMENT_DOCTYPE = "STEV_SHIPMENT_DOCTYPE_NAME";

    public void processCompletion(MStevStatementOfFact sof) throws Exception
    {
        String trxName = sof.get_TrxName();

        MStevVesselSchedule schedule = sof.getVesselSchedule();
        MOrder order = schedule.getOrder();

        // --- Realisasi per produk ---
        Map<Integer, BigDecimal> realizedByProduct = sof.getRealizedQtyByProduct();
        if (realizedByProduct.isEmpty())
            throw new IllegalStateException("Tidak ada realisasi (STEV_TallyLine kosong) untuk SoF "
                    + sof.getDocumentNo() + " — Complete dibatalkan");

        MOrderLine[] orderLines = order.getLines(true, null);
        if (orderLines.length == 0)
            throw new IllegalStateException("SPK (C_Order) " + order.getDocumentNo() + " tidak punya line jasa");

        // -----------------------------------------------------------------
        // 1) Update QtyOrdered per produk pada C_OrderLine yang sesuai
        // -----------------------------------------------------------------
        Map<Integer, MOrderLine> orderLineByProduct = new java.util.LinkedHashMap<>();
        for (MOrderLine ol : orderLines)
            orderLineByProduct.put(ol.getM_Product_ID(), ol);

        for (Map.Entry<Integer, BigDecimal> entry : realizedByProduct.entrySet())
        {
            int productId = entry.getKey();
            MOrderLine orderLine = orderLineByProduct.get(productId);
            if (orderLine == null)
                throw new IllegalStateException("Produk M_Product_ID=" + productId
                        + " tercatat di STEV_TallyLine tapi TIDAK ADA di line SPK (C_Order) "
                        + order.getDocumentNo() + " — periksa kembali kontrak awal SPK atau input Tally Line");

            BigDecimal qtyRealized = entry.getValue();
            if (qtyRealized == null || qtyRealized.signum() <= 0)
                throw new IllegalStateException("Realisasi produk M_Product_ID=" + productId + " tidak valid (<= 0)");

            orderLine.setQtyOrdered(qtyRealized);
            if (!orderLine.save())
                throw new IllegalStateException("Gagal update QtyOrdered pada C_OrderLine "
                        + orderLine.getC_OrderLine_ID() + " (M_Product_ID=" + productId + ")");
        }

        // -----------------------------------------------------------------
        // 2) Terbitkan & Complete M_InOut (Shipment) — satu header, satu
        //    line per produk. Invoice TIDAK dibuat di sini — lihat catatan
        //    desain di Javadoc class ini.
        // -----------------------------------------------------------------
        MInOut inout = createServiceShipment(order, orderLineByProduct, realizedByProduct, sof, trxName);

        sof.set_ValueOfColumn("Processed", "Y");

        log.info("SoF " + sof.getDocumentNo() + " completed: " + realizedByProduct.size()
                + " produk diproses, M_InOut=" + inout.getDocumentNo()
                + " (invoice mengikuti prosedur/jadwal standar iDempiere, tidak dibuat di sini)");
    }

    private MInOut createServiceShipment(MOrder order, Map<Integer, MOrderLine> orderLineByProduct,
            Map<Integer, BigDecimal> realizedByProduct, MStevStatementOfFact sof, String trxName)
    {
        MInOut inout = new MInOut(order, 0 /* let DocType default resolve */, sof.getSignedDate() != null
                ? sof.getSignedDate() : new Timestamp(System.currentTimeMillis()));

        int shipmentDocTypeId = resolveDocTypeId(SYSCONFIG_SHIPMENT_DOCTYPE, MDocType.DOCBASETYPE_MaterialDelivery,
                order.getAD_Client_ID(), order.getAD_Org_ID());
        if (shipmentDocTypeId > 0)
            inout.setC_DocTypeTarget_ID(shipmentDocTypeId);

        inout.setDocStatus(MInOut.DOCSTATUS_Drafted);
        inout.setDocAction(MInOut.DOCACTION_Complete);
        inout.set_ValueOfColumn("STEV_StatementOfFact_ID", sof.getSTEV_StatementOfFact_ID());
        if (!inout.save())
            throw new IllegalStateException("Gagal membuat header M_InOut untuk SoF " + sof.getDocumentNo());

        // Satu M_InOutLine per produk yang direalisasikan
        for (Map.Entry<Integer, BigDecimal> entry : realizedByProduct.entrySet())
        {
            MOrderLine orderLine = orderLineByProduct.get(entry.getKey());
            BigDecimal qty = entry.getValue();

            MInOutLine line = new MInOutLine(inout);
            line.setOrderLine(orderLine, 0, qty);
            line.setQty(qty);
            line.setM_Locator_ID(0); // Jasa: tidak menyentuh stok gudang fisik
            line.setDescription("Auto-generated dari SoF " + sof.getDocumentNo());
            if (!line.save())
                throw new IllegalStateException("Gagal membuat M_InOutLine (M_Product_ID=" + entry.getKey()
                        + ") untuk InOut " + inout.getDocumentNo());
        }

        // Complete M_InOut supaya QtyDelivered pada tiap order line ikut
        // ter-update, dan M_InOut ini otomatis "terlihat" oleh proses
        // Generate Invoices standar iDempiere (yang men-scan M_InOut
        // Complete & belum ter-invoice sesuai C_BPartner.InvoiceRule).
        if (!inout.processIt(MInOut.DOCACTION_Complete) || !inout.save())
            throw new IllegalStateException("Gagal Complete M_InOut " + inout.getDocumentNo()
                    + ": " + inout.getProcessMsg());

        return inout;
    }

    private int resolveDocTypeId(String sysConfigKey, String docBaseType, int adClientId, int adOrgId)
    {
        String docTypeName = MSysConfig.getValue(sysConfigKey, null, adClientId, adOrgId);
        if (docTypeName == null || docTypeName.isEmpty())
            return -1; // biarkan default resolution bawaan MInOut yang jalan

        List<MDocType> types = new Query(Env.getCtx(), MDocType.Table_Name,
                "Name=? AND DocBaseType=? AND AD_Client_ID=?", null)
                .setParameters(docTypeName, docBaseType, adClientId)
                .list();
        return types.isEmpty() ? -1 : types.get(0).getC_DocType_ID();
    }
}
