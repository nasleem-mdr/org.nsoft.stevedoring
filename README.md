# org.nsoft.stevedoring

Plugin OSGi iDempiere untuk modul **Stevedoring** (bongkar-muat kapal),
mengikuti alur: SPK (`C_Order`) → Vessel Schedule → Tally Sheet/Line →
Statement of Fact → Auto-Adjustment & Delivery (`M_InOut` + `C_Invoice`).

## Alur Dokumen

```
C_Order (SPK)  ──┐
                 ▼
     STEV_VesselSchedule  ──ETA/ETB/ETD, ATA/ATB/ATD, dermaga
                 │
                 ▼
     STEV_TallySheet ──► STEV_TallyLine (per shift/palka/alat/produk)
                 │
                 ▼
     STEV_StatementOfFact  ──akumulasi TotalQtyRealized, idle time, TTD
                 │  (saat DocStatus → Completed)
                 ▼
     SoFFinanceService.processCompletion()
        1. C_OrderLine.QtyOrdered = realisasi SoF
        2. M_InOut (Shipment, Jasa) — Complete otomatis
        3. C_Invoice (Draft) + STEV_FakturPajakKode = "07" (FTZ Batam)
```

## Struktur Kode

| File | Peran |
|---|---|
| `sql/01_DDL_STEV_TABLES.sql` | DDL fisik 5 tabel custom |
| `sql/02_DDL_ALTER_C_INVOICE.sql` | Kolom tambahan di `C_Invoice`/`M_InOut` untuk Faktur Pajak & jejak balik ke SoF |
| `model/MStevVessel.java` | Master kapal |
| `model/MStevVesselSchedule.java` | Jadwal & realisasi sandar kapal |
| `model/MStevTallySheet.java`, `MStevTallyLine.java` | Pencatatan lapangan per shift |
| `model/MStevStatementOfFact.java` | **Dokumen inti** — implements `DocAction`, memicu proses saat Complete |
| `service/SoFFinanceService.java` | Business logic Auto-Adjustment & Delivery (dipanggil dari `completeIt()`) |
| `validator/StevedoringDocumentValidator.java` | Sinkronisasi status `STEV_VesselSchedule` setelah SoF Completed |
| `base/StevedoringModelFactory.java`, `StevedoringValidatorFactory.java` | Registrasi OSGi (`IModelFactory`, `IModelValidatorFactory`) |

## Langkah Deployment

1. **Jalankan DDL**: `01_DDL_STEV_TABLES.sql` lalu `02_DDL_ALTER_C_INVOICE.sql` langsung ke database.
2. **Sinkronisasi Application Dictionary**: buka menu *Table and Column*
   di iDempiere, jalankan *Synchronize Database* (atau via About window)
   untuk masing-masing tabel baru — ini otomatis membuat `AD_Table` &
   `AD_Column`.
3. **Generate Model** (`X_STEV_*.java`) via *Generate Model* pada tab
   `AD_Table`, lalu copy hasilnya ke package `org.nsoft.stevedoring.model`
   (superclass dari class manual di plugin ini).
4. **Build & deploy bundle** OSGi ini (`org.nsoft.stevedoring`) ke folder
   plugins server iDempiere, lalu restart / update bundle via Felix
   console (`ss`, `update <bundle-id>`).
5. **Buat Window/Tab** untuk `STEV_Vessel`, `STEV_VesselSchedule`,
   `STEV_TallySheet` (+ tab detail `STEV_TallyLine`), dan
   `STEV_StatementOfFact` seperti window custom lain — DocAction toolbar
   otomatis muncul untuk `STEV_StatementOfFact` karena mengimplementasikan
   `DocAction`.
6. (Opsional, direkomendasikan) set `M_SysConfig`:
   - `STEV_SHIPMENT_DOCTYPE_NAME` — nama Doc Type Shipment "Jasa" khusus,
     kalau tidak ingin memakai default Shipment SPK.
   - `STEV_INVOICE_DOCTYPE_NAME` — nama Doc Type Invoice khusus FTZ.

## Catatan Desain

- **Kenapa `STEV_StatementOfFact` implements `DocAction` langsung** (bukan
  murni via `ModelValidator` seperti pola `SalesPriceAutoUpdateValidator`
  di plugin autoprice)? Karena SoF adalah dokumen *utama* dengan siklus
  hidup sendiri (Draft → In Progress → Completed/Voided) dan butuh tombol
  Complete/Void/Reactivate standar di toolbar — ini butuh implementasi
  `DocAction` penuh, bukan sekadar reaksi terhadap Complete dokumen lain.
- `ModelValidator` tetap dipakai (`StevedoringDocumentValidator`) untuk
  efek samping lintas-entitas yang sifatnya sekunder (update status Vessel
  Schedule), menjaga `completeIt()`/`SoFFinanceService` tetap fokus pada
  finance/inventory saja.
- Invoice sengaja dibuat **Draft**, tidak langsung Complete — sesuai
  requirement, tim Finance mereview dulu sebelum posting resmi
  (perhitungan pajak fasilitas FTZ perlu verifikasi manual sebelum GL
  posting).
- Kode Faktur Pajak `07` di-hardcode sebagai konstanta
  `SoFFinanceService.FAKTUR_PAJAK_KODE_FTZ_BATAM` — kalau nanti perlu
  transaksi non-FTZ juga (kode `01`, dst.), pindahkan resolusi kode ini
  ke parameter/SysConfig per `C_BPartner` atau `C_Order`, mengikuti pola
  configurable-first yang sudah dipakai di plugin autoprice.
- Asumsi 1 line jasa per SPK (`getServiceOrderLine` ambil line pertama) —
  perlu disesuaikan bila SPK ke depan bisa multi-komoditas dalam satu
  kontrak.
