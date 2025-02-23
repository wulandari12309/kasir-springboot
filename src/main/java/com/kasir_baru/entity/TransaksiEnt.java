package com.kasir_baru.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "data_transaksi")
public class TransaksiEnt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nota;
    private String namaKasir;
    private LocalDate tglTransaksi;
    private double pembayaran;
    private double kembalian;
    private Double totalTransaksi;

    @OneToMany(mappedBy = "transaksiEnt", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DetailTransaksi> detailTransaksiList = new ArrayList<>();

    public TransaksiEnt() {}

    public TransaksiEnt(Long id, String nota, String namaKasir, LocalDate tglTransaksi, double pembayaran, double kembalian, Double totalTransaksi, List<DetailTransaksi> detailTransaksiList) {
        this.id = id;
        this.nota = nota;
        this.namaKasir = namaKasir;
        this.tglTransaksi = tglTransaksi;
        this.pembayaran = pembayaran;
        this.kembalian = kembalian;
        this.totalTransaksi = totalTransaksi;
        this.detailTransaksiList = detailTransaksiList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getNamaKasir() {
        return namaKasir;
    }

    public void setNamaKasir(String namaKasir) {
        this.namaKasir = namaKasir;
    }

    public LocalDate getTglTransaksi() {
        return tglTransaksi;
    }

    public void setTglTransaksi(LocalDate tglTransaksi) {
        this.tglTransaksi = tglTransaksi;
    }

    public double getPembayaran() {
        return pembayaran;
    }

    public void setPembayaran(double pembayaran) {
        this.pembayaran = pembayaran;
    }

    public double getKembalian() {
        return kembalian;
    }

    public void setKembalian(double kembalian) {
        this.kembalian = kembalian;
    }

    public Double getTotalTransaksi() {
        return totalTransaksi;
    }

    public void setTotalTransaksi(Double totalTransaksi) {
        this.totalTransaksi = totalTransaksi;
    }

    public List<DetailTransaksi> getDetailTransaksiList() {
        return detailTransaksiList;
    }

    public void setDetailTransaksiList(List<DetailTransaksi> detailTransaksiList) {
        if (detailTransaksiList == null) {
            this.detailTransaksiList = new ArrayList<>();
        } else {
            this.detailTransaksiList = detailTransaksiList;
        }    }
}
