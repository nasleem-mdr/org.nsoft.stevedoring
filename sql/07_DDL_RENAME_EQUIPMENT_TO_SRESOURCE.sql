-- =====================================================================
-- Module: Stevedoring (org.nsoft.stevedoring)
-- File  : 07_DDL_RENAME_EQUIPMENT_TO_SRESOURCE.sql
-- Tujuan: Perbaikan nama kolom STEV_TallyLine.Equipment_ID -> S_Resource_ID
--
-- ALASAN: iDempiere mendeteksi Table Reference otomatis saat Generate
-- Model berdasarkan POLA NAMA kolom "<TableName>_ID". Nama "Equipment_ID"
-- membuat iDempiere mencari tabel bernama "Equipment" (tidak ada),
-- makanya muncul error saat Generate Model. Karena kolom ini memang FK
-- ke S_Resource, nama kolomnya harus persis "S_Resource_ID", bukan nama
-- bebas seperti "Equipment_ID" — sama seperti kasus FlagCountry yang
-- sebelumnya diganti jadi C_Country_ID.
-- =====================================================================

-- Kalau tabel STEV_TallyLine BELUM ada data (kasus paling umum saat ini,
-- masih tahap development):
ALTER TABLE STEV_TallyLine RENAME COLUMN Equipment_ID TO S_Resource_ID;

-- Constraint FK yang sudah ada (STEV_TallyLine_Equip_FK) tetap menunjuk
-- ke S_Resource, jadi tidak perlu dibuat ulang — cukup direname supaya
-- konsisten penamaannya (opsional, PostgreSQL tidak mewajibkan):
ALTER TABLE STEV_TallyLine RENAME CONSTRAINT STEV_TallyLine_Equip_FK
    TO STEV_TallyLine_SResource_FK;

-- ---------------------------------------------------------------------
-- Kalau tabel SUDAH ada data (skip bagian di atas, pakai ini):
-- ---------------------------------------------------------------------
-- ALTER TABLE STEV_TallyLine ADD COLUMN S_Resource_ID NUMERIC(10);
-- UPDATE STEV_TallyLine SET S_Resource_ID = Equipment_ID;
-- ALTER TABLE STEV_TallyLine DROP CONSTRAINT STEV_TallyLine_Equip_FK;
-- ALTER TABLE STEV_TallyLine DROP COLUMN Equipment_ID;
-- ALTER TABLE STEV_TallyLine
--     ADD CONSTRAINT STEV_TallyLine_SResource_FK FOREIGN KEY (S_Resource_ID)
--         REFERENCES S_Resource(S_Resource_ID);

-- =====================================================================
-- SETELAH DDL INI:
-- 1. Jalankan Synchronize Database di Application Dictionary — kolom
--    lama Equipment_ID di AD_Column akan perlu disesuaikan/dihapus, dan
--    kolom baru S_Resource_ID otomatis terdeteksi sebagai Table
--    Reference ke S_Resource (Reference Value tidak perlu diisi manual).
-- 2. Jalankan ulang Generate Model — X_STEV_TallyLine akan punya
--    getS_Resource_ID()/setS_Resource_ID() dan getS_Resource(), bukan
--    lagi getEquipment_ID().
-- 3. Update kode Java yang masih mereferensikan getEquipment_ID() /
--    setEquipment_ID() (lihat catatan di model/MStevTallyLine.java).
-- =====================================================================
