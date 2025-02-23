package com.kasir_baru.service;

import com.kasir_baru.dto.DetailTransaksiDTO;
import com.kasir_baru.dto.TransaksiRequest;
import com.kasir_baru.entity.BarangEnt;
import com.kasir_baru.entity.DetailTransaksi;
import com.kasir_baru.entity.TransaksiEnt;
import com.kasir_baru.repo.BarangRepo;
import com.kasir_baru.repo.DetailTransaksiRepo;
import com.kasir_baru.repo.TransaksiRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransaksiServ {
    private static final Logger logger = LoggerFactory.getLogger(TransaksiServ.class);

    @Autowired
    private TransaksiRepo transaksiRepository;
    @Autowired
    private DetailTransaksiRepo detailTransaksiRepository;
    @Autowired
    private BarangRepo barangRepository;
    @Autowired
    private BarangServ barangService;

    @Transactional
    public TransaksiEnt simpanTransaksi(String namaKasir, List<DetailTransaksi> detailTransaksiList) {
        double totalTransaksi = detailTransaksiList.stream()
                .mapToDouble(DetailTransaksi::getSubtotal)
                .sum();

        TransaksiEnt transaksi = new TransaksiEnt();
        transaksi.setNota(generateNomorNota());
        transaksi.setTglTransaksi(LocalDate.now());
        transaksi.setNamaKasir(namaKasir);
        transaksi.setTotalTransaksi(totalTransaksi);

        transaksi = transaksiRepository.save(transaksi);


        for (DetailTransaksi detail : detailTransaksiList) {
            detail.setTransaksiEnt(transaksi);
            detailTransaksiRepository.save(detail);
        }

        return transaksi;
    }

    @Transactional
    public TransaksiEnt buatTransaksi(TransaksiEnt transaksiEnt) {
        String nomorNota = generateNomorNota();
        transaksiEnt.setNota(nomorNota);

        return transaksiRepository.save(transaksiEnt);
    }

    public String generateNomorNota() {
        String tanggalHariIni = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        List<TransaksiEnt> transaksiList = transaksiRepository.findAll();
        Optional<String> lastNotaOpt = transaksiList.stream()
                .map(TransaksiEnt::getNota)
                .filter(nota -> nota.startsWith("WK" + tanggalHariIni))
                .sorted((a, b) -> b.compareTo(a))
                .findFirst();

        int nextNumber = 1;

        if (lastNotaOpt.isPresent()) {
            String lastNota = lastNotaOpt.get();
            try {
                String lastNumberStr = lastNota.substring(10);
                nextNumber = Integer.parseInt(lastNumberStr) + 1;
            } catch (NumberFormatException e) {
                System.err.println("Error parsing lastNota: " + lastNota);
            }
        }

        return String.format("WK%s%03d", tanggalHariIni, nextNumber);
    }

    @Transactional
    public TransaksiEnt simpanTransaksi(TransaksiEnt transaksiEnt) {
        transaksiEnt.getDetailTransaksiList().forEach(detail -> detail.setTransaksiEnt(transaksiEnt));

        return transaksiRepository.save(transaksiEnt);
    }

    @Transactional
    public void simpanTransaksi(TransaksiRequest transaksiRequest) {
        try {
            TransaksiEnt transaksi = new TransaksiEnt();
            transaksi.setNamaKasir(transaksiRequest.getNamaKasir());
            transaksi.setNota(generateNomorNota());
            transaksi.setTglTransaksi(LocalDate.now());
            transaksi.setTotalTransaksi(transaksiRequest.getTotalTransaksi());
            transaksi.setPembayaran(transaksiRequest.getPembayaran());
            transaksi.setKembalian(transaksiRequest.getKembalian());

            transaksi = transaksiRepository.save(transaksi);

            List<DetailTransaksi> detailTransaksiList = new ArrayList<>();
            transaksi.setDetailTransaksiList(detailTransaksiList);

            for (DetailTransaksiDTO detail : transaksiRequest.getDetailTransaksiList()) {

                if (detail.getBarangId() == null) {
                    throw new RuntimeException("Barang ID tidak boleh null!");
                }

                BarangEnt barang = barangRepository.findById(detail.getBarangId())
                        .orElseThrow(() -> new RuntimeException("Barang dengan ID " + detail.getBarangId() + " tidak ditemukan di database!"));

                DetailTransaksi detailTransaksi = new DetailTransaksi();
                detailTransaksi.setTransaksiEnt(transaksi);
                detailTransaksi.setBarangEnt(barang);
                detailTransaksi.setNamaProduk(barang.getNamaProduk()); // Pastikan nama produk diset
                detailTransaksi.setQty(detail.getQty());
                detailTransaksi.setHarga(detail.getHarga());
                detailTransaksi.setSubtotal(detail.getQty() * detail.getHarga());

                detailTransaksiRepository.save(detailTransaksi);

                int sisaStok = barang.getStok() - detail.getQty();
                if (sisaStok < 0) {
                    throw new RuntimeException("Stok tidak mencukupi untuk barang " + barang.getNamaProduk());
                }
                barang.setStok(sisaStok);
                barangRepository.save(barang);

                detailTransaksiList.add(detailTransaksi);
            }

        } catch (Exception e) {
            throw e;
        }
    }




}
