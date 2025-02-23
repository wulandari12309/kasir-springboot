package com.kasir_baru.service;

import com.kasir_baru.entity.BarangEnt;
import com.kasir_baru.repo.BarangRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BarangServ {
    @Autowired
    private BarangRepo barangRepo;

    @Transactional
    public BarangEnt tambahProduk(BarangEnt barangEnt) {
        String newKodeProduk = generateNextKode();
        barangEnt.setKodeProduk(newKodeProduk);

        return barangRepo.save(barangEnt);
    }

    public String generateNextKode() {
        String lastKode = barangRepo.findLastKodeProduk();
        if (lastKode == null || !lastKode.startsWith("BRG")) {
            return "BRG1";
        }

        try {
            int nextNumber = Integer.parseInt(lastKode.substring(3)) + 1;
            return "BRG" + nextNumber;
        } catch (NumberFormatException e) {
            return "BRG1";
        }
    }

    // Method to reduce stock based on the transaction
    public boolean updateStock(Long idBarang, int qtySold) {
        // Fetch the product by ID
        Optional<BarangEnt> barangOptional = barangRepo.findById(idBarang);

        if (barangOptional.isPresent()) {
            BarangEnt barang = barangOptional.get();

            // Check if there's enough stock
            if (barang.getStok() >= qtySold) {
                // Reduce the stock
                barang.setStok(barang.getStok() - qtySold);

                // Save the updated product entity
                barangRepo.save(barang);
                return true;  // Stock updated successfully
            } else {
                // Not enough stock available
                return false;
            }
        }

        return false;  // Product not found
    }

}
