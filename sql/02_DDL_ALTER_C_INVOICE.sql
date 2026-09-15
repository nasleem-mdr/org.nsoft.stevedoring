-- =====================================================================
-- Tambahan kolom custom pada C_Invoice untuk integrasi Faktur Pajak
-- Kode 07 (fasilitas PPN Tidak Dipungut — kawasan FTZ Batam).
-- Setelah ALTER, tambahkan kolom ini via Application Dictionary
-- (Sync This Column) agar muncul otomatis di tab Invoice.
-- =====================================================================

ALTER TABLE C_Invoice
    ADD COLUMN STEV_FakturPajakKode VARCHAR(2);

COMMENT ON COLUMN C_Invoice.STEV_FakturPajakKode IS
    'Kode transaksi Faktur Pajak (mis. 07 = PPN Tidak Dipungut, fasilitas FTZ Batam)';

-- Opsional: kolom referensi balik agar Invoice bisa ditelusuri ke SoF sumbernya
ALTER TABLE C_Invoice
    ADD COLUMN STEV_StatementOfFact_ID NUMERIC(10);

ALTER TABLE C_Invoice
    ADD CONSTRAINT STEV_Invoice_SoF_FK FOREIGN KEY (STEV_StatementOfFact_ID)
        REFERENCES STEV_StatementOfFact(STEV_StatementOfFact_ID);

-- Kolom referensi balik yang sama pada M_InOut, agar delivery jasa
-- bisa ditelusuri ke SoF sumbernya.
ALTER TABLE M_InOut
    ADD COLUMN STEV_StatementOfFact_ID NUMERIC(10);

ALTER TABLE M_InOut
    ADD CONSTRAINT STEV_InOut_SoF_FK FOREIGN KEY (STEV_StatementOfFact_ID)
        REFERENCES STEV_StatementOfFact(STEV_StatementOfFact_ID);
