-- =====================================================================
-- Module: Stevedoring (org.nsoft.stevedoring)
-- File  : 04_DDL_INAPORTNET_PLACEHOLDER.sql
-- Tujuan: Menyiapkan kolom-kolom referensi dokumen Inaportnet (PBM) di
--         STEV_VesselSchedule, SEBELUM spesifikasi teknis integrasi resmi
--         didapat dari Ditjen Hubla / BUP setempat.
--
-- CATATAN: Kolom-kolom ini murni PENYIMPANAN NOMOR REFERENSI hasil input
-- manual (atau hasil integrasi nanti). Tidak ada logic pengiriman data
-- ke Inaportnet di sini — itu akan jadi modul terpisah
-- (org.nsoft.stevedoring.integration.inaportnet) begitu spesifikasi
-- teknisnya tersedia, supaya module inti Stevedoring tetap bersih dari
-- ketergantungan format pemerintah yang bisa berubah sewaktu-waktu.
-- =====================================================================

ALTER TABLE STEV_VesselSchedule
    ADD COLUMN InaportnetPKKNo   VARCHAR(40);   -- Nomor PKK (Pemberitahuan Kedatangan Kapal)

ALTER TABLE STEV_VesselSchedule
    ADD COLUMN InaportnetRKBMNo  VARCHAR(40);   -- Nomor RKBM (Rencana Kegiatan Bongkar Muat)

ALTER TABLE STEV_VesselSchedule
    ADD COLUMN InaportnetPPKBNo  VARCHAR(40);   -- Nomor PPKB (Permohonan Pelayanan Kapal & Barang)

ALTER TABLE STEV_VesselSchedule
    ADD COLUMN InaportnetStatus  VARCHAR(30);   -- Status approval dari sisi Inaportnet (terpisah dari DocStatus internal)

COMMENT ON COLUMN STEV_VesselSchedule.InaportnetPKKNo  IS 'Nomor referensi PKK dari sistem Inaportnet — placeholder, diisi manual sampai integrasi otomatis tersedia';
COMMENT ON COLUMN STEV_VesselSchedule.InaportnetRKBMNo IS 'Nomor referensi RKBM dari sistem Inaportnet — placeholder, diisi manual sampai integrasi otomatis tersedia';
COMMENT ON COLUMN STEV_VesselSchedule.InaportnetPPKBNo IS 'Nomor referensi PPKB dari sistem Inaportnet — placeholder, diisi manual sampai integrasi otomatis tersedia';
COMMENT ON COLUMN STEV_VesselSchedule.InaportnetStatus IS 'Status approval pihak Otoritas Pelabuhan/Syahbandar di Inaportnet (mis. Diajukan/Disetujui/Ditolak) — bukan DocStatus internal iDempiere';

-- =====================================================================
-- CATATAN: setelah script ini, jalankan Synchronize Database di
-- Application Dictionary agar kolom-kolom baru terdaftar & ter-generate
-- ke X_STEV_VesselSchedule.
-- =====================================================================
