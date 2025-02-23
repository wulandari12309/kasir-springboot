package com.kasir_baru.repo;

import com.kasir_baru.entity.DetailTransaksi;
import com.kasir_baru.entity.TransaksiEnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailTransaksiRepo extends JpaRepository<DetailTransaksi, Long> {
    List<DetailTransaksi> findByTransaksiEnt(TransaksiEnt transaksi);
}
