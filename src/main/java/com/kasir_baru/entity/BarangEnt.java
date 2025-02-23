package com.kasir_baru.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "data_barang")
public class BarangEnt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String kodeProduk;
    private String namaProduk;
    private Double hargaJual;
    private Double hargaModal;
    private Integer stok;

    public BarangEnt() {}

    public BarangEnt(Long id, String kodeProduk, String namaProduk, Double hargaJual, Double hargaModal, Integer stok) {
        this.id = id;
        this.kodeProduk = kodeProduk;
        this.namaProduk = namaProduk;
        this.hargaJual = hargaJual;
        this.hargaModal = hargaModal;
        this.stok = stok;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKodeProduk() {
        return kodeProduk;
    }

    public void setKodeProduk(String kodeProduk) {
        this.kodeProduk = kodeProduk;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public Double getHargaJual() {
        return hargaJual;
    }

    public void setHargaJual(Double hargaJual) {
        this.hargaJual = hargaJual;
    }

    public Double getHargaModal() {
        return hargaModal;
    }

    public void setHargaModal(Double hargaModal) {
        this.hargaModal = hargaModal;
    }

    public Integer getStok() {
        return stok;
    }

    public void setStok(Integer stok) {
        this.stok = stok;
    }
}
