package org.nsoft.stevedoring.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.nsoft.stevedoring.model.MStevStatementOfFact;
import org.nsoft.stevedoring.model.MStevVesselSchedule;

/**
 * Logika "Auto-Adjustment & Delivery" yang berjalan saat
 * {@link MStevStatementOfFact} di-Complete:
 *
 *  1. Update QtyOrdered pada C_OrderLine sesuai realisasi tonase SoF.
 *  2. Terbitkan M_InOut (tipe Jasa / non-inventory) sebagai bukti
 *     penyerahan jasa stevedoring ke customer.
 *  3. Siapkan draft C_Invoice dari SPK, dengan kode Faktur Pajak 07
 *     (fasilitas FTZ Batam) di-set pada kolom custom
 *     C_Invoice.STEV_FakturPajakKode.
 *
 * SEMUA dijalankan dalam trx yang sama dengan proses Complete SoF
 * (get_TrxName() dari SoF), sehingga jika salah satu langkah gagal,
 * seluruh Complete SoF ikut di-rollback oleh DocumentEngine.
 */
public class SoFFinanceService
{
    /** Kode transaksi Faktur Pajak untuk fasilitas PPN Tidak Dipungut FTZ Batam */
    public static final String FAKTUR_PAJAK_KODE_FTZ_BATAM = "07";

    /**
     * Nama DocType Shipment "Jasa" (non-inventory delivery) dan DocType
     * Invoice yang dipakai proses ini. Disarankan disimpan sebagai
     * M_SysConfig agar configurable per client tanpa deploy ulang,
     * mengikuti pola AUTOPRICE_SPIKE_THRESHOLD_PCT pada plugin autoprice.
     */
    private static final String SYSCONFIG_SHIPMENT_DOCTYPE = "STEV_SHIPMENT_DOCTYPE_NAME";
    private static final String SYSCONFIG_INVOICE_DOCTYPE  = "STEV_INVOICE_DOCTYPE_NAME";

    public void processCompletion(MStevStatementOfFact sof) throws Exception
    {
        Properties ctx = sof.getCtx();
        String trxName = sof.get_TrxName();

        MStevVesselSchedule schedule = sof.getVesselSchedule();
        MOrder order = schedule.getOrder();
        BigDecimal qtyRealized = sof.getTotalQtyRealized();

        if (qtyRealized == null || qtyRealized.signum() <= 0)
            throw new IllegalStateException("TotalQtyRealized SoF tidak valid (<= 0), Complete dibatalkan");

        // -----------------------------------------------------------------
        // 1) Update QtyOrdered pada C_OrderLine sesuai realisasi tonase SoF
        // -----------------------------------------------------------------
        MOrderLine orderLine = getServiceOrderLine(order, trxName);
        BigDecimal qtyBefore = orderLine.getQtyOrdered();
        orderLine.setQtyOrdered(qtyRealized);
        if (!orderLine.save())
            throw new IllegalStateException("Gagal update QtyOrdered pada C_OrderLine " + orderLine.getC_OrderLine_ID());

        // -----------------------------------------------------------------
        // 2) Terbitkan M_InOut (Shipment) bertipe Jasa
        // -----------------------------------------------------------------
        MInOut inout = createServiceShipment(order, orderLine, qtyRealized, sof, trxName);

        // -----------------------------------------------------------------
        // 3) Siapkan draft C_Invoice + Faktur Pajak Kode 07 (FTZ Batam)
        // -----------------------------------------------------------------
        MInvoice invoice = createDraftInvoice(order, orderLine, qtyRealized, inout, sof, trxName);

        sof.set_ValueOfColumn("Processed", "Y");

        org.compiere.util.CLogger.getCLogger(SoFFinanceService.class).info(
                "SoF " + sof.getDocumentNo() + " completed: QtyOrdered " + qtyBefore + " -> " + qtyRealized
                        + ", M_InOut=" + inout.getDocumentNo() + ", C_Invoice=" + invoice.getDocumentNo());
    }

    /**
     * SPK (C_Order) untuk Stevedoring diasumsikan punya SATU line jasa utama
     * yang menampung Tarif Jasa (M_Product) — lihat kontrak awal. Jika ke
     * depan SPK bisa multi-line (mis. per jenis komoditas), ganti helper ini
     * dengan pencarian berdasarkan M_Product_ID yang sama dengan
     * TallyLine, bukan "line pertama".
     */
    private MOrderLine getServiceOrderLine(MOrder order, String trxName)
    {
        MOrderLine[] lines = order.getLines(true, null);
        if (lines.length == 0)
            throw new IllegalStateException("SPK (C_Order) " + order.getDocumentNo() + " tidak punya line jasa");
        return lines[0];
    }

    private MInOut createServiceShipment(MOrder order, MOrderLine orderLine, BigDecimal qty,
            MStevStatementOfFact sof, String trxName)
    {
        MInOut inout = new MInOut(order, 0 /* let DocType default resolve */, sof.getSignedDate() != null
                ? sof.getSignedDate() : new Timestamp(System.currentTimeMillis()));

        // Resolusi DocType Shipment "Jasa" via SysConfig, fallback ke default order
        int shipmentDocTypeId = resolveDocTypeId(SYSCONFIG_SHIPMENT_DOCTYPE, MDocType.DOCBASETYPE_MaterialDelivery,
                order.getAD_Client_ID(), order.getAD_Org_ID());
        if (shipmentDocTypeId > 0)
            inout.setC_DocTypeTarget_ID(shipmentDocTypeId);

        inout.setDocStatus(MInOut.DOCSTATUS_Drafted);
        inout.setDocAction(MInOut.DOCACTION_Complete);
        inout.set_ValueOfColumn("STEV_StatementOfFact_ID", sof.getSTEV_StatementOfFact_ID());
        if (!inout.save())
            throw new IllegalStateException("Gagal membuat header M_InOut untuk SoF " + sof.getDocumentNo());

        MInOutLine line = new MInOutLine(inout);
        line.setOrderLine(orderLine, 0, qty);
        line.setQty(qty);
        // Jasa: tidak menyentuh stok gudang fisik
        line.setM_Locator_ID(0);
        line.setDescription("Auto-generated dari SoF " + sof.getDocumentNo());
        if (!line.save())
            throw new IllegalStateException("Gagal membuat M_InOutLine untuk InOut " + inout.getDocumentNo());

        // Complete M_InOut supaya QtyDelivered pada order line ikut ter-update
        // secara otomatis oleh standard iDempiere doc engine.
        if (!inout.processIt(MInOut.DOCACTION_Complete) || !inout.save())
            throw new IllegalStateException("Gagal Complete M_InOut " + inout.getDocumentNo()
                    + ": " + inout.getProcessMsg());

        return inout;
    }

    private MInvoice createDraftInvoice(MOrder order, MOrderLine orderLine, BigDecimal qty, MInOut inout,
            MStevStatementOfFact sof, String trxName)
    {
        MInvoice invoice = new MInvoice(order, 0, sof.getSignedDate() != null
                ? sof.getSignedDate() : new Timestamp(System.currentTimeMillis()));

        int invoiceDocTypeId = resolveDocTypeId(SYSCONFIG_INVOICE_DOCTYPE, MDocType.DOCBASETYPE_SalesInvoice,
                order.getAD_Client_ID(), order.getAD_Org_ID());
        if (invoiceDocTypeId > 0)
            invoice.setC_DocTypeTarget_ID(invoiceDocTypeId);

        // Faktur Pajak Kode 07 — fasilitas PPN Tidak Dipungut untuk kawasan FTZ Batam
        invoice.set_ValueOfColumn("STEV_FakturPajakKode", FAKTUR_PAJAK_KODE_FTZ_BATAM);
        invoice.set_ValueOfColumn("STEV_StatementOfFact_ID", sof.getSTEV_StatementOfFact_ID());
        invoice.setDocStatus(MInvoice.DOCSTATUS_Drafted);
        invoice.setDocAction(MInvoice.DOCACTION_Complete);
        if (!invoice.save())
            throw new IllegalStateException("Gagal membuat header C_Invoice untuk SoF " + sof.getDocumentNo());

        MInvoiceLine invLine = new MInvoiceLine(invoice);
        invLine.setOrderLine(orderLine, 0, qty);
        invLine.setQty(qty);
        invLine.setM_InOutLine_ID(getFirstInOutLineId(inout));
        invLine.setPrice(); // ambil harga dari price list SPK (Tarif Jasa)
        if (!invLine.save())
            throw new IllegalStateException("Gagal membuat C_InvoiceLine untuk Invoice " + invoice.getDocumentNo());

        // Sengaja TIDAK di-Complete di sini — sesuai requirement, Invoice
        // disiapkan sebagai DRAFT untuk direview/di-approve tim Finance
        // sebelum benar-benar diposting/dikirim ke customer.
        invoice.saveEx();
        return invoice;
    }

    private int getFirstInOutLineId(MInOut inout)
    {
        MInOutLine[] lines = inout.getLines(true);
        return lines.length > 0 ? lines[0].getM_InOutLine_ID() : 0;
    }

    private int resolveDocTypeId(String sysConfigKey, String docBaseType, int adClientId, int adOrgId)
    {
        String docTypeName = org.compiere.util.MSysConfig.getValue(sysConfigKey, null, adClientId, adOrgId);
        if (docTypeName == null || docTypeName.isEmpty())
            return -1; // biarkan default resolution bawaan MInOut/MInvoice yang jalan

        List<MDocType> types = new Query(Env.getCtx(), MDocType.Table_Name,
                "Name=? AND DocBaseType=? AND AD_Client_ID=?", null)
                .setParameters(docTypeName, docBaseType, adClientId)
                .list();
        return types.isEmpty() ? -1 : types.get(0).getC_DocType_ID();
    }
}
