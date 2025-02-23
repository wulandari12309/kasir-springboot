package com.kasir_baru.controller;

import com.kasir_baru.entity.BarangEnt;
import com.kasir_baru.entity.KasirEnt;
import com.kasir_baru.repo.BarangRepo;
import com.kasir_baru.service.BarangServ;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class BarangCtrl {
    @Autowired
    private BarangRepo barangRepo;
    @Autowired
    private BarangServ barangServ;

    @GetMapping("/stok-page")
    public String stokBarangPage(HttpSession session, Model model) {
        KasirEnt loggedInKasir = (KasirEnt) session.getAttribute("loggedInKasir");

        if (loggedInKasir != null) {
            List<BarangEnt> barangList = barangRepo.findAll();
            model.addAttribute("barangList", barangList);
            model.addAttribute("kasirName", loggedInKasir.getUsername());
            model.addAttribute("barang", new BarangEnt());
        } else {
            return "redirect:/login-page";
        }
        return "stok";
    }

    @PostMapping("/tambah-barang")
    public String tambahBarang(@RequestParam String kodeProduk,
                               @RequestParam String namaProduk,
                               @RequestParam double hargaModal,
                               @RequestParam double hargaJual,
                               @RequestParam int stok) {
        BarangEnt barang = new BarangEnt();
        barang.setKodeProduk(kodeProduk);
        barang.setNamaProduk(namaProduk);
        barang.setHargaModal(hargaModal);
        barang.setHargaJual(hargaJual);
        barang.setStok(stok);

        barangRepo.save(barang);

        return "redirect:/stok-page";
    }

    @DeleteMapping("/hapus-barang/{id}")
    public ResponseEntity<?> deleteBarang(@PathVariable Long id) {
        Optional<BarangEnt> barang = barangRepo.findById(id);
        if (barang.isPresent()) {
            barangRepo.deleteById(id);
            return ResponseEntity.ok().body("Barang berhasil dihapus");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Barang tidak ditemukan");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarangEnt> getBarangById(@PathVariable Long id) {
        Optional<BarangEnt> barang = barangRepo.findById(id);
        return barang.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateBarang(@PathVariable Long id, @RequestBody BarangEnt updatedBarang) {
        return barangRepo.findById(id).map(barang -> {
            barang.setNamaProduk(updatedBarang.getNamaProduk());
            barang.setHargaJual(updatedBarang.getHargaJual());
            barang.setHargaModal(updatedBarang.getHargaModal());
            barang.setStok(updatedBarang.getStok());
            barangRepo.save(barang);
            return ResponseEntity.ok("Barang berhasil diperbarui");
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/tambah")
    public ResponseEntity<BarangEnt> tambahProduk(@RequestBody BarangEnt barangEnt) {
        BarangEnt savedBarang = barangServ.tambahProduk(barangEnt);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBarang);
    }

    @GetMapping("/next-kode")
    public ResponseEntity<Map<String, String>> getNextKode() {
        try {
            String kodeProduk = barangServ.generateNextKode();
            Map<String, String> response = new HashMap<>();
            response.put("kodeProduk", kodeProduk);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/search-barang")
    @ResponseBody
    public ResponseEntity<List<BarangEnt>> searchBarang(@RequestParam("keyword") String keyword) {
        List<BarangEnt> barangList = barangRepo.findByNamaProdukContainingIgnoreCase(keyword);

        if (barangList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(barangList, HttpStatus.OK);
    }

    // Endpoint to handle stock reduction after a transaction
    @PutMapping("/reduce-stock/{idBarang}")
    public String reduceStock(@PathVariable Long idBarang, @RequestParam int qtySold) {
        boolean success = barangServ.updateStock(idBarang, qtySold);

        if (success) {
            return "Stock updated successfully.";
        } else {
            return "Failed to update stock. Not enough stock or product not found.";
        }
    }








}
