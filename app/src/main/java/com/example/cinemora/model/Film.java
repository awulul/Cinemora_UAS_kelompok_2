package com.example.cinemora.model;

import java.io.Serializable;

/**
 * Model data Film yang mendukung dua sumber gambar:
 * 1. Gambar lokal (int drawable resource)
 * 2. Gambar API (String path dari TMDB)
 */
public class Film implements Serializable {

    private String judul;
    private String tahun;
    private String genre;
    private double rating;
    private String durasi;

    private int gambar;          // Untuk R.drawable.nama_file (int)
    private String posterPath;   // Untuk path API, misal: /v9X7RR.jpg (String)

    // ✅ CONSTRUCTOR UNTUK DATA LOKAL (Gunakan int untuk gambar)
    public Film(String judul, String tahun, String genre, int gambar, double rating, String durasi) {
        this.judul = judul;
        this.tahun = tahun;
        this.genre = genre;
        this.gambar = gambar;
        this.rating = rating;
        this.durasi = durasi;
    }

    // ✅ CONSTRUCTOR UNTUK DATA API (Gunakan String untuk posterPath)
    public Film(String judul, String tahun, String genre, String posterPath, double rating, String durasi) {
        this.judul = judul;
        this.tahun = tahun;
        this.genre = genre;
        this.posterPath = posterPath;
        this.rating = rating;
        this.durasi = durasi;
    }

    // Getter
    public String getJudul() { return judul; }
    public String getTahun() { return tahun; }
    public String getGenre() { return genre; }
    public double getRating() { return rating; }
    public String getDurasi() { return durasi; }
    public int getGambar() { return gambar; }
    public String getPosterPath() { return posterPath; }
}