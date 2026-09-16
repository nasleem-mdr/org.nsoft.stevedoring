-- =====================================================================
-- Module: Stevedoring (org.nsoft.stevedoring)
-- File  : 05_DDL_AD_PROCESS_REALISASI.sql
-- Tujuan: Registrasi 3 AD_Process tombol realisasi ATA/ATB/ATD di window
--         STEV_VesselSchedule.
--
-- CATATAN: Ganti nilai AD_Client_ID (di sini dicontohkan 0 = System
-- client, SESUAIKAN ke client Anda) dan jalankan setelah AD_Table
-- STEV_VesselSchedule sudah tersinkronisasi. Setelah proses ini dibuat,
-- tambahkan sebagai Process button di tab STEV_VesselSchedule lewat
-- Application Dictionary GUI (Tab > Process), atau via 2Pack terpisah.
-- =====================================================================

INSERT INTO AD_Process
    (AD_Process_ID, AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
     Value, Name, Description, Classname, AccessLevel, EntityType, IsReport)
VALUES
    (nextval('AD_Process_Sysconfig_SEQ' /* ganti ke sequence AD_Process yang sesuai di instance Anda, mis. AD_Process_ID_SEQ*/),
     0, 0, 'Y', now(), 0, now(), 0,
     'STEV_RecordVesselArrival', 'Kapal Tiba (Record Vessel Arrival)',
     'Mencatat ATA (Actual Time of Arrival) dengan waktu klik sebenarnya',
     'org.nsoft.stevedoring.process.STEV_RecordVesselArrival', '3', 'U', 'N');

INSERT INTO AD_Process
    (AD_Process_ID, AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
     Value, Name, Description, Classname, AccessLevel, EntityType, IsReport)
VALUES
    (nextval('AD_Process_Sysconfig_SEQ'),
     0, 0, 'Y', now(), 0, now(), 0,
     'STEV_RecordBerthing', 'Mulai Sandar (Record Berthing)',
     'Mencatat ATB (Actual Time of Berthing) dengan waktu klik sebenarnya; butuh ATA terisi lebih dulu',
     'org.nsoft.stevedoring.process.STEV_RecordBerthing', '3', 'U', 'N');

INSERT INTO AD_Process
    (AD_Process_ID, AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
     Value, Name, Description, Classname, AccessLevel, EntityType, IsReport)
VALUES
    (nextval('AD_Process_Sysconfig_SEQ'),
     0, 0, 'Y', now(), 0, now(), 0,
     'STEV_RecordDeparture', 'Kapal Berangkat (Record Departure)',
     'Mencatat ATD (Actual Time of Departure) dengan waktu klik sebenarnya; butuh ATB terisi lebih dulu',
     'org.nsoft.stevedoring.process.STEV_RecordDeparture', '3', 'U', 'N');

-- =====================================================================
-- PENTING: query nextval() di atas pakai nama sequence CONTOH — cek dulu
-- nama sequence AD_Process_ID yang benar di instance Anda (biasanya
-- "AD_Process_ID_SEQ" atau via fungsi NEXT_ID('AD_Process')), lalu
-- ganti sebelum dijalankan. Cara paling aman: buat AD_Process lewat GUI
-- Application Dictionary (menu "Process"), supaya ID & sequence
-- ditangani otomatis oleh iDempiere, lalu isi Classname sesuai di atas.
-- =====================================================================
