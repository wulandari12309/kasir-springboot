package com.kasir_baru.dto;

public class DetailTransaksiDTO {
    private Long barangId;
    private Integer qty;
    private Double harga;

    public DetailTransaksiDTO() {}

    public Long getBarangId() {
        return barangId;
    }

    public void setBarangId(Long barangId) {
        this.barangId = barangId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getHarga() {
        return harga;
    }

    public void setHarga(Double harga) {
        this.harga = harga;
    }
}
