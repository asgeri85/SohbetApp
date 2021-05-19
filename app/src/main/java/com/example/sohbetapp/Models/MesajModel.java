package com.example.sohbetapp.Models;

public class MesajModel {
    private String Date,from,mesajText,textType;
    private boolean okunduMu;

    public MesajModel(String date, String from, String mesajText, String textType, boolean okunduMu) {
        Date = date;
        this.from = from;
        this.mesajText = mesajText;
        this.textType = textType;
        this.okunduMu = okunduMu;
    }

    public MesajModel() {
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMesajText() {
        return mesajText;
    }

    public void setMesajText(String mesajText) {
        this.mesajText = mesajText;
    }

    public String getTextType() {
        return textType;
    }

    public void setTextType(String textType) {
        this.textType = textType;
    }

    public boolean isOkunduMu() {
        return okunduMu;
    }

    public void setOkunduMu(boolean okunduMu) {
        this.okunduMu = okunduMu;
    }

    @Override
    public String toString() {
        return "MesajModel{" +
                "Date='" + Date + '\'' +
                ", from='" + from + '\'' +
                ", mesajText='" + mesajText + '\'' +
                ", textType='" + textType + '\'' +
                ", okunduMu=" + okunduMu +
                '}';
    }
}
