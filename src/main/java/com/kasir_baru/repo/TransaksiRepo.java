package com.kasir_baru.repo;

import com.kasir_baru.entity.TransaksiEnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface TransaksiRepo extends JpaRepository<TransaksiEnt, Long> {
    Optional<TransaksiEnt> findByNota(String nota);
    @Query(value = """
    SELECT DATE(t.tgl_transaksi) AS tanggal, 
           COALESCE(COUNT(d.id), 0) AS produk_terjual, 
           COALESCE(SUM(d.subtotal - (b.harga_modal * d.qty)), 0) AS keuntungan,
           COALESCE(SUM(d.subtotal), 0) AS total_pendapatan
    FROM data_transaksi t
    LEFT JOIN detail_transaksi d ON t.id = d.transaksi_id
    LEFT JOIN data_barang b ON d.barang_id = b.id  -- UBAH dari "barang" ke "data_barang"
    GROUP BY DATE(t.tgl_transaksi)
    ORDER BY DATE(t.tgl_transaksi) DESC
    """, nativeQuery = true)
    List<Map<String, Object>> getKeuntunganHarian();


    @Query(value = """
    SELECT TO_CHAR(t.tgl_transaksi, 'YYYY-MM') AS bulan, 
           COALESCE(COUNT(d.id), 0) AS produk_terjual, 
           COALESCE(SUM(d.subtotal - (b.harga_modal * d.qty)), 0) AS keuntungan,
           COALESCE(SUM(d.subtotal), 0) AS total_pendapatan
    FROM data_transaksi t
    LEFT JOIN detail_transaksi d ON t.id = d.transaksi_id
    LEFT JOIN data_barang b ON d.barang_id = b.id  -- Pastikan tabelnya benar
    GROUP BY TO_CHAR(t.tgl_transaksi, 'YYYY-MM')
    ORDER BY bulan DESC
""", nativeQuery = true)
    List<Map<String, Object>> getKeuntunganBulanan();

}
