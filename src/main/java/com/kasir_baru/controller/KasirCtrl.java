package com.kasir_baru.controller;

import com.kasir_baru.entity.BarangEnt;
import com.kasir_baru.entity.KasirEnt;
import com.kasir_baru.repo.BarangRepo;
import com.kasir_baru.repo.KasirRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class KasirCtrl {
    @Autowired
    private KasirRepo kasirRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/daftar-kasir")
    public ResponseEntity<?> daftarKasir(@RequestBody KasirEnt kasirEnt) {
        if (kasirEnt.getUsername() == null || kasirEnt.getUsername().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username tidak boleh kosong");
        } else if (kasirEnt.getEmail() == null || kasirEnt.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email tidak boleh kosong");
        }

        kasirEnt.setPassword(passwordEncoder.encode(kasirEnt.getPassword()));
        KasirEnt saveKasir = kasirRepo.save(kasirEnt);
        return ResponseEntity.ok("Berhasil menambah kasir " + saveKasir.getUsername());
    }

    @PostMapping("/login-kasir")
    public ResponseEntity<?> loginKasir(@RequestBody KasirEnt kasirEnt, HttpSession session) {
        KasirEnt cariKasir = kasirRepo.findByUsername(kasirEnt.getUsername());
        if (cariKasir != null && passwordEncoder.matches(kasirEnt.getPassword(), cariKasir.getPassword())) {
            // Simpan informasi kasir di sesi
            session.setAttribute("loggedInKasir", cariKasir);
            return ResponseEntity.ok("Berhasil login " + kasirEnt.getUsername());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gagal menemukan data kasir");
        }
    }

    @GetMapping("/daftar-page")
    public String daftarPage() {
        return "daftar-kasir";
    }

    @GetMapping("/login-page")
    public String loginPage() {
        return "login-kasir";
    }

    @GetMapping("/dashboard-kasir")
    public String dashboardPage(HttpSession session, Model model) {
        KasirEnt loggedInKasir = (KasirEnt) session.getAttribute("loggedInKasir");
        if (loggedInKasir != null) {
            model.addAttribute("kasirName", loggedInKasir.getUsername());
        } else {
            return "redirect:/login-page"; // Redirect jika belum login
        }
        return "dashboard-page";
    }

    @GetMapping("/transaksi-page")
    public String transaksiPage(HttpSession session, Model model) {
        // Ambil data kasir dari sesi
        KasirEnt loggedInKasir = (KasirEnt) session.getAttribute("loggedInKasir");

        if (loggedInKasir != null) {
            // Tambahkan nama kasir ke model
            model.addAttribute("kasirName", loggedInKasir.getUsername());
        } else {
            // Redirect ke halaman login jika belum login
            return "redirect:/login-page";
        }
        return "transaksi";
    }

    @GetMapping("/laporan-page")
    public String laporanPage() {
        return "laporan";
    }

    @GetMapping("/pengaturan-page")
    public String pengaturanPage() {
        return "pengaturan";
    }

}