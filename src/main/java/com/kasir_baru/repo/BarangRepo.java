package com.kasir_baru.repo;

import com.kasir_baru.entity.BarangEnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BarangRepo extends JpaRepository<BarangEnt, Long> {
    List<BarangEnt> findAll();
    Optional<BarangEnt> findById(Long id);
    @Query("SELECT MAX(p.kodeProduk) FROM BarangEnt p WHERE p.kodeProduk LIKE 'BRG%'")
    String findLastKodeProduk();
    List<BarangEnt> findByNamaProdukContainingIgnoreCase(String namaProduk);
    Optional<BarangEnt> findByKodeProduk(String kodeProduk);
}
