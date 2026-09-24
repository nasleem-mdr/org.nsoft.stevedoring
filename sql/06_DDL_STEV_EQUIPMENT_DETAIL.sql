-- =====================================================================
-- Module: Stevedoring (org.nsoft.stevedoring)
-- File  : 06_DDL_STEV_EQUIPMENT_DETAIL.sql
-- Tujuan: Tabel satelit untuk S_Resource (alat berat) — TIDAK menyentuh
--         tabel inti S_Resource — menampung:
--           a) Status kepemilikan (Owned / Rented), karena tidak semua
--              alat berat yang dipakai adalah milik sendiri.
--           b) Link ke A_Asset (dimensi akuntansi NF13) HANYA untuk alat
--              yang berstatus Owned.
--           c) Data vendor & kontrak sewa untuk alat berstatus Rented.
--           d) Spesifikasi teknis dasar alat berat.
-- =====================================================================

-- ---------------------------------------------------------------------
-- A. AD_Reference List: Ownership Type (Owned / Rented)
--    Dibuat sebagai List Reference karena nilainya tetap & terbatas.
--    Body INSERT ini contoh manual — cara paling aman tetap lewat GUI
--    Application Dictionary (menu Reference > Reference List), supaya
--    AD_Reference_ID & Value_ID ditangani otomatis oleh iDempiere.
-- ---------------------------------------------------------------------
-- AD_Reference: "STEV_EquipmentOwnershipType" (tipe: List)
-- AD_Ref_List values:
--     Value='OWN'  , Name='Milik Sendiri (Owned)'
--     Value='RENT' , Name='Sewa (Rented)'

-- ---------------------------------------------------------------------
-- B. TABEL SATELIT DETAIL ALAT BERAT
-- ---------------------------------------------------------------------
CREATE TABLE STEV_EquipmentDetail
(
    STEV_EquipmentDetail_ID  NUMERIC(10)   NOT NULL,
    AD_Client_ID             NUMERIC(10)   NOT NULL,
    AD_Org_ID                NUMERIC(10)   NOT NULL,
    IsActive                 CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created                  TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy                NUMERIC(10)   NOT NULL,
    Updated                  TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy                NUMERIC(10)   NOT NULL,

    S_Resource_ID            NUMERIC(10)   NOT NULL,        -- FK -> S_Resource (1:1, alat berat operasional)
    OwnershipType            VARCHAR(10)   NOT NULL DEFAULT 'OWN',  -- List: OWN / RENT

    -- Diisi HANYA jika OwnershipType = 'OWN'
    A_Asset_ID               NUMERIC(10),                    -- FK -> A_Asset (dimensi akuntansi NF13 / depresiasi)

    -- Diisi HANYA jika OwnershipType = 'RENT'
    C_BPartner_ID            NUMERIC(10),                    -- FK -> Vendor penyewaan alat
    RentalContractNo         VARCHAR(40),
    RentalStartDate          DATE,
    RentalEndDate            DATE,
    RentalRatePerDay         NUMERIC(20,2),
    C_Currency_ID            NUMERIC(10),                    -- Mata uang tarif sewa

    -- Spesifikasi teknis dasar (berlaku untuk kedua status kepemilikan)
    CapacityTon              NUMERIC(10,2),                   -- Kapasitas angkat/muat (ton)
    ManufactureYear          NUMERIC(4,0),
    Description              VARCHAR(255),

    CONSTRAINT STEV_EqDetail_PK PRIMARY KEY (STEV_EquipmentDetail_ID),
    CONSTRAINT STEV_EqDetail_Resource_UQ UNIQUE (S_Resource_ID),   -- relasi 1:1 ke S_Resource
    CONSTRAINT STEV_EqDetail_Resource_FK FOREIGN KEY (S_Resource_ID)
        REFERENCES S_Resource(S_Resource_ID),
    CONSTRAINT STEV_EqDetail_Asset_FK FOREIGN KEY (A_Asset_ID)
        REFERENCES A_Asset(A_Asset_ID),
    CONSTRAINT STEV_EqDetail_BPartner_FK FOREIGN KEY (C_BPartner_ID)
        REFERENCES C_BPartner(C_BPartner_ID),
    CONSTRAINT STEV_EqDetail_Currency_FK FOREIGN KEY (C_Currency_ID)
        REFERENCES C_Currency(C_Currency_ID),

    -- Konsistensi data: alat Owned wajib ada A_Asset_ID, alat Rented
    -- wajib ada C_BPartner_ID (vendor). Dicek juga di beforeSave Java
    -- supaya pesan error lebih ramah, ini sebagai jaring pengaman kedua
    -- di level database.
    CONSTRAINT STEV_EqDetail_Ownership_CHK CHECK (
        (OwnershipType = 'OWN'  AND C_BPartner_ID IS NULL) OR
        (OwnershipType = 'RENT' AND A_Asset_ID IS NULL)
    )
);
COMMENT ON TABLE STEV_EquipmentDetail IS
    'Detail kepemilikan & spesifikasi alat berat — satelit dari S_Resource, tidak mengubah tabel inti';

CREATE SEQUENCE STEV_EquipmentDetail_ID_SEQ START WITH 1000000;

-- =====================================================================
-- CATATAN:
-- 1. Buat AD_Reference "STEV_EquipmentOwnershipType" (List) via GUI
--    sebelum Synchronize, isi 2 value: OWN dan RENT (lihat bagian A).
-- 2. Setelah Synchronize Database, arahkan kolom OwnershipType di
--    Application Dictionary ke Reference List tersebut.
-- 3. A_Asset yang di-link WAJIB sudah melalui proses Asset Addition
--    standar iDempiere (menu Assets) bila ingin ikut proses depresiasi;
--    kalau cuma untuk keperluan dimensi pelaporan tanpa depresiasi,
--    cukup dibuat sebagai record A_Asset tanpa Asset Addition (sesuai
--    kebijakan tim Finance/Accounting Anda).
-- =====================================================================
