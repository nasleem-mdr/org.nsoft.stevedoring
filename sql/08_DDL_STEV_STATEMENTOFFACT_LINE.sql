-- =====================================================================
-- Module: Stevedoring (org.nsoft.stevedoring)
-- File  : 08_DDL_STEV_STATEMENTOFFACT_LINE.sql
-- Tujuan: Tabel detail/child dari STEV_StatementOfFact — snapshot
--         realisasi tonase per produk (+UOM), digenerate otomatis dari
--         agregasi STEV_TallyLine, dan menjadi DASAR SUMBER untuk
--         SoFFinanceService membuat M_InOutLine (bukan lagi query
--         agregasi langsung ke STEV_TallyLine saat Complete).
-- =====================================================================

CREATE TABLE STEV_StatementOfFactLine
(
    STEV_StatementOfFactLine_ID  NUMERIC(10)   NOT NULL,
    AD_Client_ID                 NUMERIC(10)   NOT NULL,
    AD_Org_ID                    NUMERIC(10)   NOT NULL,
    IsActive                     CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created                      TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy                    NUMERIC(10)   NOT NULL,
    Updated                      TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy                    NUMERIC(10)   NOT NULL,

    STEV_StatementOfFact_ID      NUMERIC(10)   NOT NULL,   -- FK -> header SoF
    Line                         NUMERIC(10)   NOT NULL,

    M_Product_ID                 NUMERIC(10)   NOT NULL,
    C_UOM_ID                     NUMERIC(10)   NOT NULL,
    QtyRealized                  NUMERIC(20,4) NOT NULL,    -- SUM(QtyMoved) TallyLine untuk produk+UOM ini

    -- Traceability ke dokumen downstream — diisi otomatis, bukan input user
    C_OrderLine_ID                NUMERIC(10),               -- line SPK yang cocok (diisi saat regenerate)
    M_InOutLine_ID                NUMERIC(10),               -- diisi SoFFinanceService setelah M_InOut dibuat

    Description                   VARCHAR(255),

    CONSTRAINT STEV_SoFLine_PK PRIMARY KEY (STEV_StatementOfFactLine_ID),
    CONSTRAINT STEV_SoFLine_Header_FK FOREIGN KEY (STEV_StatementOfFact_ID)
        REFERENCES STEV_StatementOfFact(STEV_StatementOfFact_ID),
    CONSTRAINT STEV_SoFLine_Product_FK FOREIGN KEY (M_Product_ID) REFERENCES M_Product(M_Product_ID),
    CONSTRAINT STEV_SoFLine_UOM_FK FOREIGN KEY (C_UOM_ID) REFERENCES C_UOM(C_UOM_ID),
    CONSTRAINT STEV_SoFLine_OrderLine_FK FOREIGN KEY (C_OrderLine_ID) REFERENCES C_OrderLine(C_OrderLine_ID),
    CONSTRAINT STEV_SoFLine_InOutLine_FK FOREIGN KEY (M_InOutLine_ID) REFERENCES M_InOutLine(M_InOutLine_ID),

    -- Satu produk+UOM cuma boleh muncul sekali per SoF (hasil GROUP BY,
    -- bukan input manual bebas)
    CONSTRAINT STEV_SoFLine_UQ UNIQUE (STEV_StatementOfFact_ID, M_Product_ID, C_UOM_ID)
);
COMMENT ON TABLE STEV_StatementOfFactLine IS
    'Snapshot realisasi per produk (regenerated dari STEV_TallyLine) — dasar untuk M_InOutLine, dibekukan setelah SoF Completed';

CREATE SEQUENCE STEV_StatementOfFactLine_ID_SEQ START WITH 1000000;

-- =====================================================================
-- CATATAN:
-- 1. Baris di tabel ini TIDAK diinput manual oleh user — di-generate
--    otomatis oleh MStevStatementOfFact.regenerateLines() setiap kali
--    header SoF disimpan (selama masih Draft/In Progress), dan sekali
--    lagi tepat sebelum Complete (prepareIt()) sebagai snapshot final.
--    Setelah DocStatus = Completed, baris ini TIDAK diregenerasi lagi
--    (frozen).
-- 2. Tambahkan sebagai child Tab di window STEV_StatementOfFact
--    (Link Column = STEV_StatementOfFact_ID), set Tab read-only
--    (IsReadOnly / hilangkan New/Delete) karena isinya memang bukan
--    untuk diedit manual.
-- =====================================================================
