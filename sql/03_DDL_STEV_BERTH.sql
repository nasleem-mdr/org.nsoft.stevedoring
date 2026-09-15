-- =====================================================================
-- Module: Stevedoring (org.nsoft.stevedoring)
-- File  : 03_DDL_STEV_BERTH.sql
-- Tujuan: Master data dermaga (STEV_Berth), menggantikan kolom free-text
--         STEV_VesselSchedule.BerthLocation dengan FK STEV_Berth_ID.
--
-- Alasan jadi Table Reference (bukan List): dermaga punya atribut teknis
-- sendiri (panjang, kedalaman, kapasitas) yang berguna untuk validasi
-- penjadwalan (mis. cek draft kapal vs kedalaman dermaga), dan daftarnya
-- bisa bertambah tanpa perlu ubah struktur AD_Reference/AD_Ref_List.
-- =====================================================================

-- ---------------------------------------------------------------------
-- A. MASTER DERMAGA
-- ---------------------------------------------------------------------
CREATE TABLE STEV_Berth
(
    STEV_Berth_ID       NUMERIC(10)   NOT NULL,
    AD_Client_ID        NUMERIC(10)   NOT NULL,
    AD_Org_ID           NUMERIC(10)   NOT NULL,
    IsActive            CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created             TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy           NUMERIC(10)   NOT NULL,
    Updated              TIMESTAMP    DEFAULT now() NOT NULL,
    UpdatedBy           NUMERIC(10)   NOT NULL,

    Value               VARCHAR(20)   NOT NULL,           -- Kode dermaga, mis. "D1", "D2"
    Name                VARCHAR(60)   NOT NULL,            -- Nama dermaga, mis. "Dermaga 1 - Batu Ampar"
    LengthMeter         NUMERIC(8,2),                       -- Panjang dermaga (meter)
    DepthMeter          NUMERIC(6,2),                       -- Kedalaman air (meter) — untuk cek terhadap Draft kapal
    MaxDWT              NUMERIC(12,2),                      -- Kapasitas maksimum kapal (DWT) yang bisa sandar
    Description         VARCHAR(255),

    CONSTRAINT STEV_Berth_PK PRIMARY KEY (STEV_Berth_ID),
    CONSTRAINT STEV_Berth_Value_UQ UNIQUE (AD_Client_ID, Value)
);
COMMENT ON TABLE STEV_Berth IS 'Master data dermaga tempat kapal sandar';

CREATE SEQUENCE STEV_Berth_ID_SEQ START WITH 1000000;

-- ---------------------------------------------------------------------
-- B. GANTI KOLOM FREE-TEXT DI STEV_VesselSchedule DENGAN FK
-- ---------------------------------------------------------------------
-- Kalau tabel STEV_VesselSchedule BELUM live / belum ada datanya
-- (kasus paling umum di tahap development ini), cukup:

ALTER TABLE STEV_VesselSchedule DROP COLUMN IF EXISTS BerthLocation;

ALTER TABLE STEV_VesselSchedule ADD COLUMN STEV_Berth_ID NUMERIC(10);

ALTER TABLE STEV_VesselSchedule
    ADD CONSTRAINT STEV_VesSched_Berth_FK FOREIGN KEY (STEV_Berth_ID)
        REFERENCES STEV_Berth(STEV_Berth_ID);

-- ---------------------------------------------------------------------
-- C. (Opsional) Migrasi kalau sudah terlanjur ada data BerthLocation lama
--    Uncomment & sesuaikan bagian ini, JANGAN dijalankan bareng bagian B
--    di atas jika tabel sudah punya isi — jalankan urutan berikut:
-- ---------------------------------------------------------------------
-- ALTER TABLE STEV_VesselSchedule ADD COLUMN STEV_Berth_ID NUMERIC(10);
--
-- -- Buat master dermaga dari nilai-nilai unik yang sudah ada
-- INSERT INTO STEV_Berth (STEV_Berth_ID, AD_Client_ID, AD_Org_ID, IsActive,
--         Created, CreatedBy, Updated, UpdatedBy, Value, Name)
-- SELECT nextval('STEV_Berth_ID_SEQ'), AD_Client_ID, 0, 'Y',
--         now(), 0, now(), 0, BerthLocation, BerthLocation
-- FROM (SELECT DISTINCT AD_Client_ID, BerthLocation FROM STEV_VesselSchedule
--       WHERE BerthLocation IS NOT NULL) src;
--
-- UPDATE STEV_VesselSchedule vs
-- SET STEV_Berth_ID = b.STEV_Berth_ID
-- FROM STEV_Berth b
-- WHERE vs.BerthLocation = b.Value AND vs.AD_Client_ID = b.AD_Client_ID;
--
-- ALTER TABLE STEV_VesselSchedule DROP COLUMN BerthLocation;
-- ALTER TABLE STEV_VesselSchedule
--     ADD CONSTRAINT STEV_VesSched_Berth_FK FOREIGN KEY (STEV_Berth_ID)
--         REFERENCES STEV_Berth(STEV_Berth_ID);

-- =====================================================================
-- CATATAN: setelah script ini, jalankan Synchronize Database di
-- Application Dictionary agar AD_Table STEV_Berth dan kolom baru
-- STEV_VesselSchedule.STEV_Berth_ID terdaftar & ter-generate model-nya.
-- =====================================================================
