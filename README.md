# org.nsoft.stevedoring

Plugin OSGi iDempiere untuk modul **Stevedoring** (bongkar-muat kapal),
mengikuti alur: SPK (`C_Order`) → Vessel Schedule → Tally Sheet/Line →
Statement of Fact → Auto-Adjustment & Delivery (`M_InOut`). Penerbitan
`C_Invoice` TIDAK dilakukan oleh plugin ini — mengikuti prosedur/jadwal
invoicing standar iDempiere (lihat "Catatan Desain").

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
        1. C_OrderLine.QtyOrdered = realisasi SoF (per produk)
        2. M_InOut (Shipment, Jasa) — Complete otomatis
                 │
                 ▼  (di luar plugin ini — prosedur standar iDempiere)
     C_BPartner.InvoiceRule menentukan kapan di-invoice, proses batch
     "Generate Invoices" mengambil M_InOut yang sudah Complete
```

## Struktur Kode

| File | Peran |
|---|---|
| `sql/01_DDL_STEV_TABLES.sql` | DDL fisik 5 tabel custom |
| `sql/02_DDL_ALTER_C_INVOICE.sql` | Kolom tambahan opsional di `C_Invoice`/`M_InOut` (Faktur Pajak & jejak balik ke SoF) — dipakai saat invoice benar-benar terbit lewat prosedur standar, bukan oleh plugin ini |
| `model/MStevVessel.java` | Master kapal |
| `model/MStevVesselSchedule.java` | Jadwal & realisasi sandar kapal |
| `model/MStevTallySheet.java`, `MStevTallyLine.java` | Pencatatan lapangan per shift |
| `process/STEV_RecordVesselArrival.java`, `STEV_RecordBerthing.java`, `STEV_RecordDeparture.java` | Tombol aksi untuk mengisi ATA/ATB/ATD dengan waktu klik sebenarnya (bukan edit field manual), menjaga urutan realisasi |
| `process/STEV_CompleteStatementOfFact.java` | Tombol "Complete" alternatif yang lebih terlihat (opsional) — memanggil jalur `DocAction` yang sama, bukan logic baru |
| `callout/STEV_CalloutOrderBPartner.java` | Auto-isi `C_BPartner_ID` dari `C_Order_ID` di Tab Vessel Schedule, didaftarkan lewat `Activator` (bukan field "Callout" manual) |
| `model/MStevStatementOfFact.java` | **Dokumen inti** — implements `DocAction`, memicu proses saat Complete |
| `service/SoFFinanceService.java` | Business logic Auto-Adjustment & Delivery: update QtyOrdered + generate/Complete M_InOut (dipanggil dari `completeIt()`) |
| `validator/StevedoringDocumentValidator.java` | Sinkronisasi status `STEV_VesselSchedule` setelah SoF Completed |
| `base/StevedoringModelFactory.java`, `StevedoringValidatorFactory.java` | Registrasi OSGi (`IModelFactory`, `IModelValidatorFactory`) |

## Langkah Deployment

1. **Jalankan DDL**: `01_DDL_STEV_TABLES.sql`, lalu `02_DDL_ALTER_C_INVOICE.sql` (opsional, kalau ingin kolom Faktur Pajak/jejak SoF tetap ada di Invoice walau dibuat lewat proses standar).
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
6. **Setup invoicing standar** (bukan bagian dari plugin ini, tapi perlu
   dipastikan sudah dikonfigurasi supaya M_InOut hasil SoF benar-benar
   ter-invoice): set `C_BPartner.InvoiceRule` sesuai kebutuhan tiap
   customer (mis. "Customer Scheduled After Delivery" untuk yang minta
   invoice digabung bulanan), lalu jalankan proses batch *Generate
   Invoices (Manual)* secara berkala (atau scheduler).
7. (Opsional) set `M_SysConfig`:
   - `STEV_SHIPMENT_DOCTYPE_NAME` — nama Doc Type Shipment "Jasa" khusus,
     kalau tidak ingin memakai default Shipment SPK.

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
  delivery saja.
- **`C_Invoice` sengaja TIDAK dibuat oleh plugin ini.** Awalnya
  didesain SoF langsung generate draft Invoice, tapi ini dibatalkan
  supaya tidak memaksakan asumsi "1 SoF = 1 Invoice" — sebagian customer
  minta invoice digabung batch/bulanan (banyak SoF/M_InOut jadi 1
  Invoice), yang cuma bisa didukung dengan lepas tangan ke mekanisme
  `C_BPartner.InvoiceRule` + proses Generate Invoices bawaan iDempiere.
  Kode Faktur Pajak (mis. `07` untuk fasilitas FTZ Batam, kolom custom
  `C_Invoice.STEV_FakturPajakKode` di `sql/02_DDL_ALTER_C_INVOICE.sql`)
  karena itu juga TIDAK di-set otomatis dari sini — kalau perlu otomatis
  terisi saat invoice benar-benar di-generate, sarankan didefault dari
  flag di `C_BPartner` (mis. "Customer FTZ Batam") lewat validator kecil
  terpisah di `C_Invoice`, bukan tanggung jawab plugin Stevedoring ini.
- **Multi-produk per SPK ditangani per-produk, bukan asumsi 1 line**:
  `SoFFinanceService` memakai `MStevStatementOfFact.getRealizedQtyByProduct()`
  (agregasi `STEV_TallyLine` dikelompokkan per `M_Product_ID`) untuk
  menentukan berapa banyak `M_InOutLine` yang dibuat dan `C_OrderLine`
  mana yang di-update `QtyOrdered`-nya. Kalau SPK punya 2 produk (mis.
  batubara & nikel) dan keduanya tercatat di Tally Line, SoF yang sama
  akan menghasilkan 1 `M_InOut` (header tunggal) dengan **2 baris**
  (satu per produk). Kalau ada produk yang tercatat di Tally Line tapi
  tidak ada line-nya di SPK, proses akan gagal dengan pesan error
  eksplisit (bukan diam-diam di-skip).

## Realisasi ATA/ATB/ATD via Tombol Proses (bukan edit field manual)

Field `ATA`/`ATB`/`ATD` pada `STEV_VesselSchedule` sebaiknya **tidak**
dibuat editable langsung di form. Sediakan 3 Process button di tab
`STEV_VesselSchedule` (Application Dictionary > Tab > Process):

| Tombol | Class | Efek |
|---|---|---|
| Kapal Tiba | `STEV_RecordVesselArrival` | `ATA = now()`, `DocStatus → In Progress` |
| Mulai Sandar | `STEV_RecordBerthing` | `ATB = now()` (butuh ATA terisi) |
| Kapal Berangkat | `STEV_RecordDeparture` | `ATD = now()` (butuh ATB terisi) |

Registrasi `AD_Process`-nya ada contoh SQL di
`sql/05_DDL_AD_PROCESS_REALISASI.sql`, tapi **cara paling aman** tetap
lewat GUI Application Dictionary (menu Process) supaya ID/sequence
ditangani otomatis — SQL di file itu cuma referensi kolom yang perlu
diisi (`Classname`, dsb).

Keuntungan pendekatan tombol dibanding field bebas edit:
- Timestamp akurat (waktu klik = waktu asli), tidak bergantung ketik manual
- Urutan ATA → ATB → ATD terjaga di level proses (bukan cuma validasi `beforeSave`)
- Bisa dibatasi lewat Role/Process Access, mis. hanya role "Port Ops"

## Kepemilikan Alat Berat: Milik Sendiri vs Sewa

`STEV_TallyLine.S_Resource_ID` merujuk ke **`S_Resource`** (tabel inti
iDempiere untuk resource yang bisa dijadwalkan — sama dengan yang dipakai
plugin booking ruangan meeting), **bukan** `A_Asset` (terlalu terikat ke
akuntansi/depresiasi) dan bukan tabel baru dari nol (supaya otomatis
kebagian mekanisme ketersediaan/booking yang sudah ada).

Karena tidak semua alat berat adalah milik sendiri (banyak yang sewaan),
dibuat tabel satelit `STEV_EquipmentDetail` (`sql/06_DDL_STEV_EQUIPMENT_DETAIL.sql`)
1:1 terhadap `S_Resource`:

| OwnershipType | Wajib diisi | Tidak boleh diisi |
|---|---|---|
| `OWN` (Milik Sendiri) | `A_Asset_ID` (dimensi akuntansi NF13) | `C_BPartner_ID` |
| `RENT` (Sewa) | `C_BPartner_ID` (vendor) + data kontrak | `A_Asset_ID` |

Validasi ini dijaga di 2 lapis: `CHECK` constraint di database, dan
`MStevEquipmentDetail.beforeSave()` untuk pesan error yang lebih jelas.

Catatan penting: menghubungkan `A_Asset_ID` di sini **tidak otomatis**
membuat biaya operasional alat muncul di laporan akuntansi — dimensi itu
baru "hidup" kalau diisi di baris transaksi yang benar-benar di-posting
ke `Fact_Acct` (mis. `GL_JournalLine` untuk jurnal biaya bensin/service),
yang saat ini di luar cakupan plugin Stevedoring (murni proses Finance).

**Catatan penamaan kolom FK**: kolom di `STEV_TallyLine` yang menunjuk ke
`S_Resource` HARUS bernama persis `S_Resource_ID` (bukan `Equipment_ID`
atau nama bebas lain), karena iDempiere mendeteksi Table Reference saat
Generate Model dari pola nama `<TableName>_ID`. Kalau instance Anda
sempat terlanjur pakai nama lama `Equipment_ID`, jalankan
`sql/07_DDL_RENAME_EQUIPMENT_TO_SRESOURCE.sql` lalu Synchronize Database
+ Generate Model ulang.

## Tombol "Complete" yang Lebih Terlihat (opsional, di samping Document Action bawaan)

Field bawaan **"Document Action"** (dropdown Complete/Void/Close di
toolbar) tetap berfungsi normal — tidak dihapus/diganti. Kalau ingin
tombol yang lebih menonjol di form (mis. buat user lapangan yang jarang
pakai iDempiere), tambahkan `process/STEV_CompleteStatementOfFact.java`
sebagai tombol tambahan:

1. **AD_Process**: buat baru, `Name` = "Complete Statement of Fact",
   `Classname` = `org.nsoft.stevedoring.process.STEV_CompleteStatementOfFact`.
2. **AD_Column** di Tab `STEV_StatementOfFact`: buat kolom baru
   `Reference = Button`, `Process` = process di atas, isi field
   **"Virtual Column"** dengan nilai dummy (mis. `'1'`) — supaya tidak
   perlu kolom fisik di database (lihat penjelasan Virtual Column di
   percakapan sebelumnya).
3. Tambahkan sebagai Field di Tab, taruh `SeqNo` di posisi menonjol
   (misalnya paling atas form), beri `Display Logic` = `@DocStatus@!=CO`
   supaya tombol hilang otomatis setelah SoF Completed (menghindari klik
   ganda — proses ini juga sudah menolak run kalau `DocStatus` sudah
   `CO`, jadi ini lapis kedua, bukan satu-satunya penjaga).

**Penting**: tombol ini TIDAK menduplikasi business logic —
`STEV_CompleteStatementOfFact.doIt()` cuma memanggil
`sof.processIt(DocAction.ACTION_Complete)`, jalur yang **persis sama**
dengan yang dipicu dropdown Document Action bawaan
(`processIt() → DocumentEngine → completeIt() → SoFFinanceService`).
Jadi tidak ada risiko dua implementasi Complete yang berbeda perilaku —
ini murni alternatif pemicu (trigger), bukan logic baru.

## Callout: Auto-isi C_BPartner_ID dari C_Order_ID

`STEV_CalloutOrderBPartner.java` — begitu user memilih SPK (`C_Order_ID`)
di Tab `STEV_VesselSchedule`, otomatis isi `C_BPartner_ID` (Agen
Shipping/Customer) dari SPK yang dipilih.

**Registrasi TIDAK lewat field "Callout" di Application Dictionary**
(cara klasik yang butuh isi manual nama class di kolom `Callout` pada
layar Column) — karena pada bundle OSGi, `Class.forName()` yang dipakai
mekanisme klasik itu bisa gagal menemukan class plugin akibat isolasi
classloader antar bundle. Sebagai gantinya, dipakai mekanisme resmi
**`IMappedColumnCalloutFactory`** (fitur NF9 iDempiere untuk OSGi
Column Callout): mapping table+column ke instance callout didaftarkan
langsung di `Activator.start()`:

```java
Core.getMappedColumnCalloutFactory().addMapping(
        MStevVesselSchedule.Table_Name,
        MStevVesselSchedule.COLUMNNAME_C_Order_ID,
        STEV_CalloutOrderBPartner::new);
```

**Tidak ada konfigurasi GUI apa pun yang perlu dilakukan** untuk callout
ini — begitu bundle `org.nsoft.stevedoring` ter-install & aktif,
callout otomatis jalan setiap kali field `C_Order_ID` berubah di Tab
`STEV_VesselSchedule`. Kalau ke depan mau tambah callout lain (mis. auto
isi field lain dari master Vessel/Berth), tinggal tambah baris
`addMapping(...)` lagi di `Activator` yang sama — tidak perlu bikin
`OSGI-INF` XML component terpisah untuk tiap callout.

## Integrasi Inaportnet PBM (persiapan, belum aktif)

`sql/04_DDL_INAPORTNET_PLACEHOLDER.sql` menambahkan 4 kolom di
`STEV_VesselSchedule` sebagai tempat menyimpan nomor referensi dokumen
Inaportnet (**PKK**, **RKBM**, **PPKB**) dan status approval pihak
Otoritas Pelabuhan/Syahbandar, diisi manual untuk sementara.

Ini sengaja **hanya kolom penyimpanan**, tanpa logic pengiriman data —
karena Inaportnet bukan API publik (integrasi butuh registrasi resmi ke
Ditjen Perhubungan Laut dan spesifikasi teknis yang biasanya diberikan
langsung, bukan dokumentasi terbuka). Begitu spesifikasi teknis resmi
didapat, integrasi sebaiknya dibuat sebagai modul terpisah
`org.nsoft.stevedoring.integration.inaportnet` (adapter/mapping saja),
supaya module inti Stevedoring ini tidak bergantung pada format yang
bisa berubah sewaktu-waktu dari sisi pemerintah.
