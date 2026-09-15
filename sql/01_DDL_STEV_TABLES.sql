-- =====================================================================
-- Module: Stevedoring (org.nsoft.stevedoring)
-- File  : 01_DDL_STEV_TABLES.sql
-- Tujuan: DDL fisik PostgreSQL untuk tabel-tabel custom Stevedoring.
--
-- CATATAN PENTING (alur kerja iDempiere yang benar):
--   1. Jalankan script ini langsung ke database (psql / pgAdmin).
--   2. Buka menu "Table and Column" di Application Dictionary,
--      klik tombol "Sync This Column" / gunakan "DB.Synchronize" dari
--      About window (atau import via 2Pack) sehingga AD_Table &
--      AD_Column otomatis terbentuk dari tabel fisik ini.
--   3. Set AD_Table.ClassName untuk STEV_StatementOfFact menjadi:
--        org.nsoft.stevedoring.model.MStevStatementOfFact
--      (opsional untuk tabel lain jika ingin override behaviour PO).
--   4. Generate Model (X_STEV_*.java) via menu "Generate Model" /
--      idempiere ant task "generateModel" — class X_STEV_* akan
--      menjadi superclass dari MStevXxx yang ditulis manual di plugin ini.
-- =====================================================================

-- ---------------------------------------------------------------------
-- A. MASTER KAPAL
-- ---------------------------------------------------------------------
CREATE TABLE STEV_Vessel
(
    STEV_Vessel_ID      NUMERIC(10)   NOT NULL,
    AD_Client_ID        NUMERIC(10)   NOT NULL,
    AD_Org_ID           NUMERIC(10)   NOT NULL,
    IsActive            CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created             TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy           NUMERIC(10)   NOT NULL,
    Updated             TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy           NUMERIC(10)   NOT NULL,

    Name                VARCHAR(120)  NOT NULL,          -- Nama Kapal
    CallSign            VARCHAR(20),
    IMONumber           VARCHAR(20),
    FlagCountry         VARCHAR(60),
    VesselType          VARCHAR(30),                      -- Bulk Carrier, Tanker, Container, dll
    LOA                 NUMERIC(10,2),                     -- Length Overall (meter)
    GRT                 NUMERIC(12,2),                     -- Gross Register Tonnage
    NRT                 NUMERIC(12,2),                     -- Net Register Tonnage
    DraftMeter          NUMERIC(6,2),                      -- Draft kapal (meter)
    Description         VARCHAR(255),

    CONSTRAINT STEV_Vessel_PK PRIMARY KEY (STEV_Vessel_ID)
);
COMMENT ON TABLE STEV_Vessel IS 'Master data kapal untuk operasional Stevedoring';

-- ---------------------------------------------------------------------
-- B. VESSEL SCHEDULE (Operasional) — referensi ke C_Order (SPK)
-- ---------------------------------------------------------------------
CREATE TABLE STEV_VesselSchedule
(
    STEV_VesselSchedule_ID  NUMERIC(10)   NOT NULL,
    AD_Client_ID            NUMERIC(10)   NOT NULL,
    AD_Org_ID               NUMERIC(10)   NOT NULL,
    IsActive                CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created                 TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy               NUMERIC(10)   NOT NULL,
    Updated                 TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy               NUMERIC(10)   NOT NULL,

    DocumentNo              VARCHAR(30)   NOT NULL,
    C_Order_ID              NUMERIC(10)   NOT NULL,        -- FK -> SPK (Sales Order)
    STEV_Vessel_ID          NUMERIC(10)   NOT NULL,        -- FK -> Master Kapal
    C_BPartner_ID           NUMERIC(10)   NOT NULL,        -- Agen Shipping / Customer

    ActivityType            VARCHAR(20)   NOT NULL,        -- Discharge / Loading
    BerthLocation           VARCHAR(60),

    ETA                     TIMESTAMP,
    ETB                     TIMESTAMP,
    ETD                     TIMESTAMP,
    ATA                     TIMESTAMP,
    ATB                     TIMESTAMP,
    ATD                     TIMESTAMP,

    DocStatus               VARCHAR(2)    DEFAULT 'DR' NOT NULL,  -- DR / IP / CO / VO
    Processed               CHAR(1)       DEFAULT 'N' NOT NULL,
    Description              VARCHAR(255),

    CONSTRAINT STEV_VesselSchedule_PK PRIMARY KEY (STEV_VesselSchedule_ID),
    CONSTRAINT STEV_VesSched_Order_FK FOREIGN KEY (C_Order_ID) REFERENCES C_Order(C_Order_ID),
    CONSTRAINT STEV_VesSched_Vessel_FK FOREIGN KEY (STEV_Vessel_ID) REFERENCES STEV_Vessel(STEV_Vessel_ID),
    CONSTRAINT STEV_VesSched_BP_FK FOREIGN KEY (C_BPartner_ID) REFERENCES C_BPartner(C_BPartner_ID)
);
COMMENT ON TABLE STEV_VesselSchedule IS 'Jadwal & realisasi sandar kapal, terhubung ke SPK (C_Order)';

-- ---------------------------------------------------------------------
-- C. TALLY SHEET & TALLY LINE (Lapangan) — referensi ke Vessel Schedule
-- ---------------------------------------------------------------------
CREATE TABLE STEV_TallySheet
(
    STEV_TallySheet_ID     NUMERIC(10)   NOT NULL,
    AD_Client_ID           NUMERIC(10)   NOT NULL,
    AD_Org_ID              NUMERIC(10)   NOT NULL,
    IsActive               CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created                TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy              NUMERIC(10)   NOT NULL,
    Updated                TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy              NUMERIC(10)   NOT NULL,

    DocumentNo             VARCHAR(30)   NOT NULL,
    STEV_VesselSchedule_ID NUMERIC(10)   NOT NULL,         -- FK -> Vessel Schedule
    TallyDate              DATE          NOT NULL,
    ShiftNo                VARCHAR(10)   NOT NULL,          -- Shift 1 / 2 / 3
    DocStatus              VARCHAR(2)    DEFAULT 'DR' NOT NULL,
    Description            VARCHAR(255),

    CONSTRAINT STEV_TallySheet_PK PRIMARY KEY (STEV_TallySheet_ID),
    CONSTRAINT STEV_TallySheet_VS_FK FOREIGN KEY (STEV_VesselSchedule_ID)
        REFERENCES STEV_VesselSchedule(STEV_VesselSchedule_ID)
);
COMMENT ON TABLE STEV_TallySheet IS 'Header pencatatan tally per shift';

CREATE TABLE STEV_TallyLine
(
    STEV_TallyLine_ID      NUMERIC(10)   NOT NULL,
    AD_Client_ID           NUMERIC(10)   NOT NULL,
    AD_Org_ID              NUMERIC(10)   NOT NULL,
    IsActive               CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created                TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy              NUMERIC(10)   NOT NULL,
    Updated                TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy              NUMERIC(10)   NOT NULL,

    Line                   NUMERIC(10)   NOT NULL,
    STEV_TallySheet_ID     NUMERIC(10)   NOT NULL,         -- FK -> Tally Sheet
    HatchNo                VARCHAR(10),                     -- Nomor Palka
    Equipment_ID           NUMERIC(10),                     -- FK -> S_Resource (alat berat)
    M_Product_ID           NUMERIC(10)   NOT NULL,          -- FK -> Product (jenis jasa/komoditas)
    QtyMoved               NUMERIC(20,4) NOT NULL,          -- Ritase / tonase per baris
    C_UOM_ID               NUMERIC(10)   NOT NULL,
    TimeStart              TIMESTAMP,
    TimeEnd                TIMESTAMP,
    Description            VARCHAR(255),

    CONSTRAINT STEV_TallyLine_PK PRIMARY KEY (STEV_TallyLine_ID),
    CONSTRAINT STEV_TallyLine_TS_FK FOREIGN KEY (STEV_TallySheet_ID)
        REFERENCES STEV_TallySheet(STEV_TallySheet_ID),
    CONSTRAINT STEV_TallyLine_Prod_FK FOREIGN KEY (M_Product_ID) REFERENCES M_Product(M_Product_ID),
    CONSTRAINT STEV_TallyLine_UOM_FK FOREIGN KEY (C_UOM_ID) REFERENCES C_UOM(C_UOM_ID),
    CONSTRAINT STEV_TallyLine_Equip_FK FOREIGN KEY (Equipment_ID) REFERENCES S_Resource(S_Resource_ID)
);
COMMENT ON TABLE STEV_TallyLine IS 'Detail ritase/bongkar-muat per shift, palka, dan alat berat';

-- ---------------------------------------------------------------------
-- D. STATEMENT OF FACT (Berita Acara) — akumulasi akhir per Vessel Schedule
-- ---------------------------------------------------------------------
CREATE TABLE STEV_StatementOfFact
(
    STEV_StatementOfFact_ID NUMERIC(10)  NOT NULL,
    AD_Client_ID             NUMERIC(10) NOT NULL,
    AD_Org_ID                NUMERIC(10) NOT NULL,
    IsActive                 CHAR(1)     DEFAULT 'Y' NOT NULL,
    Created                  TIMESTAMP   DEFAULT now() NOT NULL,
    CreatedBy                NUMERIC(10) NOT NULL,
    Updated                  TIMESTAMP   DEFAULT now() NOT NULL,
    UpdatedBy                NUMERIC(10) NOT NULL,

    DocumentNo               VARCHAR(30) NOT NULL,
    STEV_VesselSchedule_ID   NUMERIC(10) NOT NULL,        -- FK -> Vessel Schedule (1:1 penutup)

    TotalQtyRealized         NUMERIC(20,4) DEFAULT 0,      -- Akumulasi dari STEV_TallyLine.QtyMoved
    C_UOM_ID                 NUMERIC(10),
    TotalIdleTimeMinutes     NUMERIC(10,0) DEFAULT 0,
    IdleTimeReason           VARCHAR(255),                  -- hujan / kerusakan alat / dll
    MasterName               VARCHAR(120),                  -- Nama Master Kapal
    DigitalSignature         TEXT,                          -- Base64 signature image
    SignedDate               TIMESTAMP,

    DocStatus                VARCHAR(2)  DEFAULT 'DR' NOT NULL,  -- DR / IP / CO / VO / CL
    DocAction                VARCHAR(2)  DEFAULT 'CO' NOT NULL,  -- kolom standar DocAction iDempiere
    Processed                CHAR(1)     DEFAULT 'N' NOT NULL,   -- 'Y' setelah downstream selesai
    Processing               CHAR(1)     DEFAULT 'N',            -- lock flag standar DocAction
    IsApproved               CHAR(1)     DEFAULT 'N',
    Description              VARCHAR(255),

    CONSTRAINT STEV_SoF_PK PRIMARY KEY (STEV_StatementOfFact_ID),
    CONSTRAINT STEV_SoF_VS_FK FOREIGN KEY (STEV_VesselSchedule_ID)
        REFERENCES STEV_VesselSchedule(STEV_VesselSchedule_ID)
);
COMMENT ON TABLE STEV_StatementOfFact IS 'Berita Acara final — memicu Auto-Adjustment & Delivery saat Completed';

-- ---------------------------------------------------------------------
-- E. Sequences (opsional — iDempiere biasanya generate AD_Sequence sendiri
--    saat sinkronisasi tabel, tapi disiapkan manual sebagai cadangan)
-- ---------------------------------------------------------------------
CREATE SEQUENCE STEV_Vessel_ID_SEQ            START WITH 1000000;
CREATE SEQUENCE STEV_VesselSchedule_ID_SEQ    START WITH 1000000;
CREATE SEQUENCE STEV_TallySheet_ID_SEQ        START WITH 1000000;
CREATE SEQUENCE STEV_TallyLine_ID_SEQ         START WITH 1000000;
CREATE SEQUENCE STEV_StatementOfFact_ID_SEQ   START WITH 1000000;
