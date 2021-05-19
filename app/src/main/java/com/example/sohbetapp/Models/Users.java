package com.example.sohbetapp.Models;

public class Users {
    private String adSoyad,hakkinda,profilFoto,tarih;
    private boolean durum;

    public Users(String adSoyad, String hakkinda, String profilFoto, String tarih,boolean durum) {
        this.adSoyad = adSoyad;
        this.hakkinda = hakkinda;
        this.profilFoto = profilFoto;
        this.tarih = tarih;
        this.durum=durum;
    }

    public Users() {
    }

    public String getAdSoyad() {
        return adSoyad;
    }

    public void setAdSoyad(String adSoyad) {
        this.adSoyad = adSoyad;
    }

    public String getHakkinda() {
        return hakkinda;
    }

    public void setHakkinda(String hakkinda) {
        this.hakkinda = hakkinda;
    }

    public String getProfilFoto() {
        return profilFoto;
    }

    public void setProfilFoto(String profilFoto) {
        this.profilFoto = profilFoto;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public boolean isDurum() {
        return durum;
    }

    public void setDurum(boolean durum) {
        this.durum = durum;
    }

    @Override
    public String toString() {
        return "Users{" +
                "adSoyad='" + adSoyad + '\'' +
                ", hakkinda='" + hakkinda + '\'' +
                ", profilFoto='" + profilFoto + '\'' +
                ", tarih='" + tarih + '\'' +
                '}';
    }
}
