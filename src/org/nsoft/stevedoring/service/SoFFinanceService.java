package org.nsoft.stevedoring.service;

import java.sql.Timestamp;
import java.util.List;

import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MLocator;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MWarehouse;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.model.MSysConfig;
import org.nsoft.stevedoring.model.MStevStatementOfFact;
import org.nsoft.stevedoring.model.MStevStatementOfFactLine;
import org.nsoft.stevedoring.model.MStevVesselSchedule;

/**
 * Logika "Auto-Adjustment & Delivery" yang berjalan saat
 * {@link MStevStatementOfFact} di-Complete:
 *
 *  1. Update QtyOrdered pada C_OrderLine sesuai realisasi tonase SoF,
 *     PER BARIS {@link MStevStatementOfFactLine} (satu SPK bisa punya
 *     beberapa line/komoditas berbeda).
 *  2. Terbitkan SATU M_InOut (header), dengan SATU M_InOutLine per
 *     baris SoF, lalu di-Complete. M_InOutLine_ID yang terbentuk
 *     disimpan balik ke STEV_StatementOfFactLine untuk traceability.
 *
 * PENTING — SUMBER DATA: proses ini TIDAK query ulang STEV_TallyLine.
 * Sumbernya adalah {@link MStevStatementOfFact#getLines()} —
 * snapshot yang sudah di-regenerate & dibekukan tepat sebelum Complete
 * (lihat {@link MStevStatementOfFact#regenerateLines()} di
 * {@code prepareIt()}). Ini memastikan angka yang dipakai untuk
 * QtyOrdered/M_InOut adalah PERSIS yang sama dengan yang tampil di tab
 * detail SoF saat user terakhir kali melihatnya sebelum klik Complete —
 * bukan hasil hitung ulang yang bisa saja sudah berbeda kalau ada
 * TallyLine yang berubah di antara waktu itu dan waktu Complete
 * benar-benar diproses.
 *
 * Pembuatan C_Invoice SENGAJA TIDAK dilakukan di sini — mengikuti
 * prosedur/jadwal invoicing standar iDempiere (C_BPartner.InvoiceRule +
 * proses batch Generate Invoices), supaya kompatibel dengan customer
 * yang minta invoice digabung batch/bulanan.
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

        List<MStevStatementOfFactLine> sofLines = sof.getLines();
        if (sofLines.isEmpty())
            throw new IllegalStateException("STEV_StatementOfFactLine kosong untuk SoF "
                    + sof.getDocumentNo() + " — Complete dibatalkan");

        // -----------------------------------------------------------------
        // 1) Validasi & update QtyOrdered per baris pada C_OrderLine terkait
        // -----------------------------------------------------------------
        for (MStevStatementOfFactLine sofLine : sofLines)
        {
            if (sofLine.getC_OrderLine_ID() <= 0)
                throw new IllegalStateException("Produk M_Product_ID=" + sofLine.getM_Product_ID()
                        + " (baris " + sofLine.getLine() + ") tidak punya C_OrderLine yang cocok di SPK "
                        + order.getDocumentNo() + " — periksa kembali kontrak awal SPK atau input Tally Line");

            if (sofLine.getQtyRealized() == null || sofLine.getQtyRealized().signum() <= 0)
                throw new IllegalStateException("QtyRealized tidak valid (<= 0) pada baris "
                        + sofLine.getLine() + " SoF " + sof.getDocumentNo());

            MOrderLine orderLine = new MOrderLine(sof.getCtx(), sofLine.getC_OrderLine_ID(), trxName);
            orderLine.setQtyOrdered(sofLine.getQtyRealized());
            if (!orderLine.save())
                throw new IllegalStateException("Gagal update QtyOrdered pada C_OrderLine "
                        + orderLine.getC_OrderLine_ID() + " (M_Product_ID=" + sofLine.getM_Product_ID() + ")");
        }

        // -----------------------------------------------------------------
        // 2) Terbitkan & Complete M_InOut (Shipment) — satu header, satu
        //    line per baris SoF. Invoice TIDAK dibuat di sini.
        // -----------------------------------------------------------------
        createServiceShipment(order, sofLines, sof, trxName);

        sof.set_ValueOfColumn("Processed", "Y");

        log.info("SoF " + sof.getDocumentNo() + " completed: " + sofLines.size()
                + " baris diproses (invoice mengikuti prosedur/jadwal standar iDempiere, tidak dibuat di sini)");
    }

    private void createServiceShipment(MOrder order, List<MStevStatementOfFactLine> sofLines,
            MStevStatementOfFact sof, String trxName)
    {
        // PENTING: resolve & set Document Type SEBELUM save() pertama —
        // jangan andalkan resolusi default bawaan MInOut (pass 0 lalu
        // biarkan MInOut cari sendiri), karena itu yang menyebabkan error
        // "Not found Document Type for Shipment" kalau tidak ada
        // Document Type yang di-flag default untuk Org SPK ini.
        int shipmentDocTypeId = resolveDocTypeId(SYSCONFIG_SHIPMENT_DOCTYPE, MDocType.DOCBASETYPE_MaterialDelivery,
                order.getAD_Client_ID(), order.getAD_Org_ID());

        MInOut inout = new MInOut(order, shipmentDocTypeId, sof.getSignedDate() != null
                ? sof.getSignedDate() : new Timestamp(System.currentTimeMillis()));
        // Catatan: beda dengan MOrder/MInvoice, M_InOut TIDAK punya kolom
        // C_DocTypeTarget_ID — cuma C_DocType_ID. Constructor di atas
        // sudah menerapkan shipmentDocTypeId lewat parameter kedua, tapi
        // di-set eksplisit sekali lagi di sini supaya jelas terjamin
        // (bukan bergantung asumsi internal constructor).
        inout.setC_DocType_ID(shipmentDocTypeId);

        inout.setDocStatus(MInOut.DOCSTATUS_Drafted);
        inout.setDocAction(MInOut.DOCACTION_Complete);
        inout.set_ValueOfColumn("STEV_StatementOfFact_ID", sof.getSTEV_StatementOfFact_ID());
        if (!inout.save())
            throw new IllegalStateException("Gagal membuat header M_InOut untuk SoF " + sof.getDocumentNo());

        // M_InOutLine.M_Locator_ID adalah FK sungguhan ke M_Locator — TIDAK
        // boleh diisi 0 (bukan "kosong", tapi literal FK ke record yang
        // tidak ada, selalu ditolak constraint database). Walau produknya
        // Jasa, M_InOut/M_InOutLine tetap butuh Locator yang valid karena
        // memang didesain untuk pergerakan barang fisik — pakai Default
        // Locator dari Warehouse SPK.
        int locatorId = getDefaultLocatorId(order);

        // Satu M_InOutLine per baris SoF — sekaligus simpan balik
        // M_InOutLine_ID ke baris SoF untuk traceability.
        for (MStevStatementOfFactLine sofLine : sofLines)
        {
            MOrderLine orderLine = new MOrderLine(sof.getCtx(), sofLine.getC_OrderLine_ID(), trxName);

            MInOutLine line = new MInOutLine(inout);
            line.setOrderLine(orderLine, locatorId, sofLine.getQtyRealized());
            line.setQty(sofLine.getQtyRealized());
            line.setM_Locator_ID(locatorId);
            line.setDescription("Auto-generated dari SoF " + sof.getDocumentNo() + " baris " + sofLine.getLine());
            if (!line.save())
                throw new IllegalStateException("Gagal membuat M_InOutLine (baris SoF " + sofLine.getLine()
                        + ") untuk InOut " + inout.getDocumentNo());

            // Traceability: catat M_InOutLine_ID balik ke baris SoF, lewat
            // SQL langsung (bukan sofLine.save()) supaya tidak memicu
            // beforeSave/afterSave MStevStatementOfFactLine yang tidak perlu.
            DB.executeUpdateEx(
                    "UPDATE STEV_StatementOfFactLine SET M_InOutLine_ID=? WHERE STEV_StatementOfFactLine_ID=?",
                    new Object[] { line.getM_InOutLine_ID(), sofLine.getSTEV_StatementOfFactLine_ID() }, trxName);
        }

        // Complete M_InOut supaya QtyDelivered pada tiap order line ikut
        // ter-update, dan M_InOut ini otomatis "terlihat" oleh proses
        // Generate Invoices standar iDempiere.
        if (!inout.processIt(MInOut.DOCACTION_Complete) || !inout.save())
            throw new IllegalStateException("Gagal Complete M_InOut " + inout.getDocumentNo()
                    + ": " + inout.getProcessMsg());
    }

    /**
     * Ambil Default Locator dari Warehouse yang dipakai SPK. M_InOutLine
     * WAJIB punya M_Locator_ID yang valid (FK sungguhan ke M_Locator),
     * bahkan untuk produk Jasa — MWarehouse.getDefaultLocator() akan
     * otomatis membuatkan satu locator generik kalau warehouse itu
     * benar-benar belum punya locator sama sekali.
     */
    private int getDefaultLocatorId(MOrder order)
    {
        int warehouseId = order.getM_Warehouse_ID();
        if (warehouseId <= 0)
            throw new IllegalStateException("SPK (C_Order) " + order.getDocumentNo()
                    + " tidak punya Warehouse — set M_Warehouse_ID pada SPK terlebih dahulu");

        MWarehouse warehouse = MWarehouse.get(order.getCtx(), warehouseId);
        MLocator locator = warehouse.getDefaultLocator();
        if (locator == null || locator.getM_Locator_ID() <= 0)
            throw new IllegalStateException("Warehouse " + warehouse.getName()
                    + " tidak punya Default Locator — buat minimal 1 Locator untuk warehouse ini");

        return locator.getM_Locator_ID();
    }

    /**
     * Cari Document Type secara EKSPLISIT berdasarkan DocBaseType (+ Org
     * SPK sebagai prioritas, fallback ke Org manapun yang match di
     * client yang sama), TIDAK bergantung pada resolusi default bawaan
     * MInOut/MInvoice (yang sering gagal kalau tidak ada Document Type
     * yang ditandai default untuk kombinasi Client/Org tertentu).
     *
     * Kalau M_SysConfig (STEV_SHIPMENT_DOCTYPE_NAME) diisi, hasil query
     * dipersempit ke Document Type dengan Name tersebut persis — kalau
     * tidak diisi, ambil Document Type manapun dengan DocBaseType yang
     * sesuai, prioritaskan yang Org-nya sama dengan SPK, lalu yang
     * ditandai IsDefault.
     *
     * Melempar error EKSPLISIT (bukan return -1 lalu biarkan MInOut
     * gagal dengan pesan generik) kalau benar-benar tidak ketemu, supaya
     * user tahu persis harus setup apa di Document Type window.
     */
    private int resolveDocTypeId(String sysConfigKey, String docBaseType, int adClientId, int adOrgId)
    {
        String preferredName = MSysConfig.getValue(sysConfigKey, null, adClientId, adOrgId);

        StringBuilder whereClause = new StringBuilder("DocBaseType=? AND AD_Client_ID=? AND IsActive='Y'");
        List<Object> params = new java.util.ArrayList<>();
        params.add(docBaseType);
        params.add(adClientId);

        if (preferredName != null && !preferredName.isEmpty())
        {
            whereClause.append(" AND Name=?");
            params.add(preferredName);
        }

        List<MDocType> types = new Query(Env.getCtx(), MDocType.Table_Name, whereClause.toString(), null)
                .setParameters(params.toArray())
                .setOrderBy("CASE WHEN AD_Org_ID=" + adOrgId + " THEN 0 ELSE 1 END, IsDefault DESC")
                .list();

        if (types.isEmpty())
            throw new IllegalStateException("Tidak ditemukan Document Type dengan DocBaseType='" + docBaseType
                    + "' untuk AD_Client_ID=" + adClientId
                    + (preferredName != null ? " dan Name='" + preferredName + "'" : "")
                    + " — buat/aktifkan Document Type yang sesuai di menu Document Type (Application Dictionary), "
                    + "atau set M_SysConfig '" + sysConfigKey + "' ke nama Document Type yang benar.");

        return types.get(0).getC_DocType_ID();
    }
}
