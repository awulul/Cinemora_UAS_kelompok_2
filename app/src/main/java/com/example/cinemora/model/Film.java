package com.example.cinemora.model;

public class Film {
    String judul, tahun, genre, durasi;
    int gambar;
    double rating;

    public Film(String judul, String tahun, String genre, int gambar, double rating) {
        this.judul = judul;
        this.tahun = tahun;
        this.genre = genre;
        this.gambar = gambar;
        this.rating = rating;
        this.durasi = "2h 0m"; // Default jika tidak diisi
    }

    public Film(String judul, String tahun, String genre, int gambar, double rating, String durasi) {
        this.judul = judul;
        this.tahun = tahun;
        this.genre = genre;
        this.gambar = gambar;
        this.rating = rating;
        this.durasi = durasi;
    }

    public String getJudul() { return judul; }
    public String getTahun() { return tahun; }
    public String getGenre() { return genre; }
    public int getGambar() { return gambar; }
    public double getRating() { return rating; }
    public String getDurasi() { return durasi; }
}