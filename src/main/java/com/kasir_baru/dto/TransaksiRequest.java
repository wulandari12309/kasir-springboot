package com.kasir_baru.dto;

import java.util.List;

public class TransaksiRequest {
    private String namaKasir;
    private double pembayaran;
    private double kembalian;
    private Double totalTransaksi;
    private List<DetailTransaksiDTO> detailTransaksiList;

    public TransaksiRequest() {}

    public TransaksiRequest(String namaKasir, double pembayaran, double kembalian, Double totalTransaksi, List<DetailTransaksiDTO> detailTransaksiList) {
        this.namaKasir = namaKasir;
        this.pembayaran = pembayaran;
        this.kembalian = kembalian;
        this.totalTransaksi = totalTransaksi;
        this.detailTransaksiList = detailTransaksiList;
    }

    public String getNamaKasir() {
        return namaKasir;
    }

    public void setNamaKasir(String namaKasir) {
        this.namaKasir = namaKasir;
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

    public List<DetailTransaksiDTO> getDetailTransaksiList() {
        return detailTransaksiList;
    }

    public void setDetailTransaksiList(List<DetailTransaksiDTO> detailTransaksiList) {
        this.detailTransaksiList = detailTransaksiList;
    }
}
