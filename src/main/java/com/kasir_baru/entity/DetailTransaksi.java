package com.kasir_baru.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "detail_transaksi")
public class DetailTransaksi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String namaProduk;
    private Integer qty;
    private Double harga;
    private Double subtotal;

    @ManyToOne
    @JoinColumn(name = "transaksi_id", nullable = false)
    @JsonBackReference
    private TransaksiEnt transaksiEnt;

    @ManyToOne
    @JoinColumn(name = "barang_id")
    @JsonBackReference
    private BarangEnt barangEnt;

    public DetailTransaksi() {}

    public DetailTransaksi(Long id, String namaProduk, Integer qty, Double harga, Double subtotal, TransaksiEnt transaksiEnt, BarangEnt barangEnt) {
        this.id = id;
        this.namaProduk = namaProduk;
        this.qty = qty;
        this.harga = harga;
        this.subtotal = subtotal;
        this.transaksiEnt = transaksiEnt;
        this.barangEnt = barangEnt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
        if (this.harga != null) {
            this.subtotal = qty * this.harga;
        }
    }

    public Double getHarga() {
        return harga;
    }

    public void setHarga(Double harga) {
        this.harga = harga;
        if (this.qty != null) {
            this.subtotal = this.qty * harga;
        }
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public TransaksiEnt getTransaksiEnt() {
        return transaksiEnt;
    }

    public void setTransaksiEnt(TransaksiEnt transaksiEnt) {
        this.transaksiEnt = transaksiEnt;
    }

    public BarangEnt getBarangEnt() {
        return barangEnt;
    }

    public void setBarangEnt(BarangEnt barangEnt) {
        this.barangEnt = barangEnt;
    }
}
