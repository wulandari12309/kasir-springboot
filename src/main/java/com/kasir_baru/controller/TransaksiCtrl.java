package com.kasir_baru.controller;

import com.kasir_baru.dto.DetailTransaksiDTO;
import com.kasir_baru.dto.TransaksiRequest;
import com.kasir_baru.entity.BarangEnt;
import com.kasir_baru.entity.DetailTransaksi;
import com.kasir_baru.entity.KasirEnt;
import com.kasir_baru.entity.TransaksiEnt;
import com.kasir_baru.repo.BarangRepo;
import com.kasir_baru.repo.DetailTransaksiRepo;
import com.kasir_baru.repo.TransaksiRepo;
import com.kasir_baru.service.TransaksiServ;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class TransaksiCtrl {
    @Autowired
    private TransaksiServ transaksiService;
    @Autowired
    private TransaksiRepo transaksiRepository;
    @Autowired
    private BarangRepo barangRepository;
    @Autowired
    private DetailTransaksiRepo detailTransaksiRepository;

    @PostMapping("/transaksi")
    public ResponseEntity<Map<String, String>> createTransaction(@RequestBody TransaksiRequest transaksiRequest) {
        try {
            transaksiService.simpanTransaksi(transaksiRequest);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Transaksi berhasil disimpan");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/transaksi-nama-kasir")
    public String transaksiPage(HttpSession session, Model model) {
        KasirEnt loggedInKasir = (KasirEnt) session.getAttribute("loggedInKasir");

        if (loggedInKasir != null) {
            model.addAttribute("kasirName", loggedInKasir.getUsername());
        } else {
            return "redirect:/login-page";
        }
        return "transaksi";
    }

    @PostMapping("/simpan-transaksi")
    public ResponseEntity<Map<String, String>> simpanTransaksi(@RequestBody TransaksiRequest transaksiRequest) {
        try {
            transaksiService.simpanTransaksi(transaksiRequest);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Transaksi berhasil disimpan");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/laporan")
    public ResponseEntity<List<TransaksiEnt>> getLaporanTransaksi() {
        List<TransaksiEnt> transaksiList = transaksiRepository.findAll();
        return ResponseEntity.ok(transaksiList);
    }

    @GetMapping("/laporan/detail")
    public ResponseEntity<List<DetailTransaksi>> getDetailTransaksi(@RequestParam String nota) {
        Optional<TransaksiEnt> transaksi = transaksiRepository.findByNota(nota);

        if (transaksi.isPresent()) {
            List<DetailTransaksi> details = detailTransaksiRepository.findByTransaksiEnt(transaksi.get());
            return ResponseEntity.ok(details);
        } else {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/laporan/keuntungan-harian")
    public ResponseEntity<?> getKeuntunganHarian() {
        try {
            List<Map<String, Object>> keuntunganHarian = transaksiRepository.getKeuntunganHarian();

            return ResponseEntity.ok(keuntunganHarian);
        } catch (Exception e) {
            e.printStackTrace();  // Cetak error ke log
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }


    @GetMapping("laporan/keuntungan-bulanan")
    public ResponseEntity<?> getKeuntunganBulanan() {
        try {
            List<Map<String, Object>> keuntunganBulanan = transaksiRepository.getKeuntunganBulanan();

            if (keuntunganBulanan.isEmpty()) {
                return ResponseEntity.ok(Collections.singletonMap("message", "Tidak ada data keuntungan bulanan."));
            }

            return ResponseEntity.ok(keuntunganBulanan);
        } catch (Exception e) {
            e.printStackTrace(); // Cetak error ke log
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/keuntungan-harian/download/{tanggal}")
    public ResponseEntity<?> downloadKeuntunganHarianPerTanggal(@PathVariable String tanggal) {
        try {
            List<Map<String, Object>> keuntunganHarian = transaksiRepository.getKeuntunganHarian();

            Map<String, Object> data = keuntunganHarian.stream()
                    .filter(row -> row.get("tanggal").toString().equals(tanggal))
                    .findFirst()
                    .orElse(null);

            if (data == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "Data tidak ditemukan untuk tanggal " + tanggal));
            }

            String csvData = "Tanggal,Produk Terjual,Keuntungan,Total Pendapatan\n" +
                    data.get("tanggal") + "," +
                    data.get("produk_terjual") + "," +
                    data.get("keuntungan") + "," +
                    data.get("total_pendapatan") + "\n";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=keuntungan tanggal " + tanggal + ".csv")
                    .header(HttpHeaders.CONTENT_TYPE, "text/csv")
                    .body(csvData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/keuntungan-bulanan/download/{bulan}")
    public ResponseEntity<?> downloadKeuntunganBulananPerBulan(@PathVariable String bulan) {
        try {
            List<Map<String, Object>> keuntunganBulanan = transaksiRepository.getKeuntunganBulanan();

            Map<String, Object> data = keuntunganBulanan.stream()
                    .filter(row -> row.get("bulan").equals(bulan))
                    .findFirst()
                    .orElse(null);

            if (data == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "Data tidak ditemukan untuk bulan " + bulan));
            }

            String csvData = "Bulan,Produk Terjual,Keuntungan,Total Pendapatan\n" +
                    data.get("bulan") + "," +
                    data.get("produk_terjual") + "," +
                    data.get("keuntungan") + "," +
                    data.get("total_pendapatan") + "\n";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=keuntungan bulan " + bulan + ".csv")
                    .header(HttpHeaders.CONTENT_TYPE, "text/csv")
                    .body(csvData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }






}
