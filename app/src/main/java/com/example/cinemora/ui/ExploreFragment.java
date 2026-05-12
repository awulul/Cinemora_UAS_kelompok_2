package com.example.cinemora.ui; // Menentukan lokasi paket (folder) dari file ini

// Import berbagai library Android dan pihak ketiga yang dibutuhkan
import android.os.Bundle; // Untuk menyimpan status data antar perubahan siklus hidup fragment
import android.view.LayoutInflater; // Untuk mengubah file XML menjadi objek View
import android.view.View; // Kelas dasar untuk semua komponen antarmuka (UI)
import android.view.ViewGroup; // Wadah untuk menampung satu atau lebih View
import android.widget.Button; // Komponen tombol standar
import android.widget.EditText; // Komponen input teks dari pengguna
import android.widget.ImageView; // Komponen untuk menampilkan gambar
import android.widget.TextView; // Komponen untuk menampilkan teks
import android.widget.Toast; // Pesan munculan singkat di layar

import androidx.appcompat.app.AlertDialog; // Dialog peringatan/pilihan dari pustaka pendukung
import androidx.fragment.app.Fragment; // Kelas dasar untuk membuat bagian modular dari UI (Fragment)
import androidx.recyclerview.widget.GridLayoutManager; // Pengatur tata letak RecyclerView dalam bentuk kotak (grid)
import androidx.recyclerview.widget.LinearLayoutManager; // Pengatur tata letak RecyclerView dalam bentuk daftar linear
import androidx.recyclerview.widget.RecyclerView; // Komponen daftar yang efisien untuk memuat banyak data

import com.example.cinemora.R; // Referensi otomatis ke semua resource aplikasi (layout, id, dll)
import com.example.cinemora.adapter.FilmAdapter; // Adapter kustom untuk menghubungkan data film ke RecyclerView
import com.example.cinemora.api.ApiService; // Antarmuka (interface) untuk mendefinisikan endpoint API
import com.example.cinemora.api.RetrofitClient; // Klien untuk melakukan permintaan jaringan ke internet
import com.example.cinemora.model.Film; // Model data film buatan sendiri untuk internal aplikasi
import com.example.cinemora.model.Movie; // Model data satu film hasil tangkapan dari API
import com.example.cinemora.model.MovieResponse; // Model pembungkus hasil list film dari API
import com.google.android.material.bottomsheet.BottomSheetDialog; // Dialog yang muncul dari bawah layar

import java.util.ArrayList; // Struktur data daftar dinamis
import java.util.List; // Antarmuka untuk koleksi daftar
import java.util.Random; // Generator angka acak

import retrofit2.Call; // Objek permintaan Retrofit yang bisa dikirim
import retrofit2.Callback; // Antarmuka untuk menangani hasil balasan dari internet
import retrofit2.Response; // Objek yang berisi data balasan dari server

/**
 * ExploreFragment: Mengelola halaman jelajah film dengan fitur filter, toggle tampilan, dan kalkulator.
 */
public class ExploreFragment extends Fragment {

    private boolean isGridView = true; // Status awal tampilan (true berarti kotak/grid)
    private RecyclerView rv; // Komponen daftar film
    private ImageView btnToggle; // Tombol untuk ganti tampilan grid/list
    private TextView chipGenre, chipYear; // Tombol chip untuk filter genre dan tahun

    private List<Film> fullList = new ArrayList<>(); // Gudang semua data film yang diambil dari API
    private List<Film> filteredList = new ArrayList<>(); // Daftar film yang sudah disaring untuk ditampilkan
    private FilmAdapter adapter; // Jembatan antara data film dan tampilan RecyclerView

    private String selectedGenre = "All"; // Menyimpan genre yang sedang dipilih pengguna (default: Semua)
    private String selectedYear = "All"; // Menyimpan tahun yang sedang dipilih pengguna (default: Semua)

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup c, Bundle b) {
        // Mengubah layout XML fragment_explore menjadi tampilan Java
        View v = i.inflate(R.layout.fragment_explore, c, false);

        // 1. Inisialisasi: Menghubungkan variabel dengan komponen di XML berdasarkan ID
        rv = v.findViewById(R.id.rvExplore); // Inisialisasi RecyclerView
        btnToggle = v.findViewById(R.id.btnToggleLayout); // Inisialisasi tombol ganti layout
        View btnOpenCalculator = v.findViewById(R.id.btnOpenCalculator); // Inisialisasi tombol kalkulator
        chipGenre = v.findViewById(R.id.chipGenre); // Inisialisasi tombol filter genre
        chipYear = v.findViewById(R.id.chipYear); // Inisialisasi tombol filter tahun

        // 2. Persiapan Awal RecyclerView
        adapter = new FilmAdapter(filteredList); // Membuat adapter dengan daftar yang difilter (awalnya kosong)
        rv.setLayoutManager(new GridLayoutManager(getContext(), 2)); // Set tampilan awal 2 kolom (Grid)
        rv.setAdapter(adapter); // Pasang adapter ke RecyclerView

        // 3. Logika Klik Tombol Toggle (Grid vs List)
        btnToggle.setOnClickListener(view -> {
            isGridView = !isGridView; // Balikkan status tampilan
            if (isGridView) {
                // Jika status grid: ubah layout manager ke Grid 2 kolom
                rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
                btnToggle.setImageResource(R.drawable.list); // Ganti ikon menjadi ikon list
                adapter.setGridMode(true); // Beritahu adapter untuk mode grid
            } else {
                // Jika status list: ubah layout manager ke Linear (1 kolom)
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                btnToggle.setImageResource(R.drawable.grid); // Ganti ikon menjadi ikon grid
                adapter.setGridMode(false); // Beritahu adapter untuk mode list
            }
        });

        // 4. Logika Klik Filter Genre
        chipGenre.setOnClickListener(view -> showGenreDialog());

        // 5. Logika Klik Filter Tahun
        chipYear.setOnClickListener(view -> showYearDialog());

        // 6. Logika Klik Tombol Kalkulator Maraton
        if (btnOpenCalculator != null) {
            btnOpenCalculator.setOnClickListener(view -> showCalculatorDialog());
        }

        // 7. Proses Utama: Ambil data film dari internet (API TMDB)
        loadMoviesFromApi();

        return v; // Mengembalikan tampilan yang sudah siap
    }

    /**
     * loadMoviesFromApi: Mengambil data film populer dari server internet.
     */
    private void loadMoviesFromApi() {
        // Membuat layanan API melalui Retrofit
        ApiService apiService = RetrofitClient.getTmdbClient().create(ApiService.class);
        // Memanggil endpoint popular movies dengan API Key
        Call<MovieResponse> call = apiService.getPopularMovies("17a371ceb7024f246d53bf2803eedc73");

        // Menjalankan permintaan secara asinkron (di latar belakang)
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                // Jika server memberikan respon sukses dan ada datanya
                if (response.isSuccessful() && response.body() != null) {
                    fullList.clear(); // Bersihkan daftar lama

                    // Persiapan data acak untuk simulasi filter dan durasi di demo
                    String[] dummyGenres = {"Action", "Drama", "Horror", "Comedy", "Thriller", "Animation", "Sci-Fi"};
                    String[] dummyYears = {"2026", "2025", "2006", "2000"};
                    // Daftar durasi acak agar tidak seragam "2h" semua
                    String[] dummyDurations = {"1h 45m", "2h 10m", "1h 55m", "2h 30m", "1h 30m", "2h 15m"};

                    Random random = new Random();

                    // Iterasi (perulangan) untuk setiap film dari API
                    for (Movie m : response.body().getResults()) {
                        // Secara acak memberikan tahun, genre, dan durasi dari daftar dummy
                        String randomYear = dummyYears[random.nextInt(dummyYears.length)];
                        String randomGenre = dummyGenres[random.nextInt(dummyGenres.length)];
                        String randomDuration = dummyDurations[random.nextInt(dummyDurations.length)];

                        // Membuat objek Film kustom dengan data asli API dan data acak simulasi
                        Film film = new Film(
                                m.getTitle(), // Ambil judul asli dari API
                                randomYear, // Gunakan tahun acak (simulasi)
                                randomGenre, // Gunakan genre acak (simulasi)
                                m.getPosterPath(), // Ambil alamat gambar asli dari API
                                m.getVoteAverage(), // Ambil rating asli dari API
                                randomDuration // ✅ Durasi acak agar bervariasi di layar
                        );
                        fullList.add(film); // Masukkan ke gudang data utama
                    }
                    applyFilter(); // Terapkan penyaringan untuk pertama kali
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                // Jika gagal terhubung ke internet atau server
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Gagal memuat data API", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * showGenreDialog: Menampilkan kotak pilihan genre.
     */
    private void showGenreDialog() {
        String[] genres = {"All", "Action", "Drama", "Horror", "Comedy", "Thriller", "Animation", "Sci-Fi"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Pilih Genre");
        builder.setItems(genres, (dialog, which) -> {
            selectedGenre = genres[which]; // Simpan genre yang dipilih
            chipGenre.setText(selectedGenre); // Ubah teks tombol chip sesuai pilihan
            applyFilter(); // Jalankan fungsi penyaringan
        });
        builder.show(); // Tampilkan dialog ke layar
    }

    /**
     * showYearDialog: Menampilkan kotak pilihan tahun.
     */
    private void showYearDialog() {
        String[] years = {"All", "2026", "2025", "2006", "2000"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Pilih Tahun");
        builder.setItems(years, (dialog, which) -> {
            selectedYear = years[which]; // Simpan tahun yang dipilih
            chipYear.setText(selectedYear); // Ubah teks tombol chip sesuai pilihan
            applyFilter(); // Jalankan fungsi penyaringan
        });
        builder.show(); // Tampilkan dialog ke layar
    }

    /**
     * applyFilter: Menyaring data film berdasarkan pilihan genre dan tahun pengguna.
     */
    private void applyFilter() {
        filteredList.clear(); // Kosongkan daftar tampilan sementara
        // Perulangan untuk mengecek setiap film di gudang data (fullList)
        for (Film film : fullList) {
            // Cek apakah genre cocok atau pengguna memilih "All"
            boolean genreMatch = selectedGenre.equals("All") || film.getGenre().equalsIgnoreCase(selectedGenre);
            // Cek apakah tahun cocok atau pengguna memilih "All"
            boolean yearMatch = selectedYear.equals("All") || film.getTahun().equals(selectedYear);

            // Jika kedua kriteria cocok, masukkan film ke daftar tampilan
            if (genreMatch && yearMatch) {
                filteredList.add(film);
            }
        }
        adapter.notifyDataSetChanged(); // Beritahu adapter agar layar diperbarui

        // Perbarui warna tombol chip (biru jika aktif, abu-abu jika "All")
        updateChipUI(chipGenre, !selectedGenre.equals("All"));
        updateChipUI(chipYear, !selectedYear.equals("All"));
    }

    /**
     * updateChipUI: Mengubah gaya visual tombol filter.
     */
    private void updateChipUI(TextView chip, boolean isActive) {
        if (isActive) {
            // Jika filter aktif: beri latar biru dan teks putih
            chip.setBackgroundResource(R.drawable.bg_chip_selected);
            chip.setTextColor(getResources().getColor(R.color.white));
        } else {
            // Jika filter tidak aktif: beri latar abu-abu terang dan teks hitam
            chip.setBackgroundResource(R.drawable.bg_chip_unselected);
            chip.setTextColor(getResources().getColor(R.color.black));
        }
    }

    /**
     * showCalculatorDialog: Menampilkan dialog kalkulator maraton film.
     */
    private void showCalculatorDialog() {
        // Munculkan BottomSheetDialog dari bawah
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        View sheet = getLayoutInflater().inflate(R.layout.dialog_marathon, null);
        dialog.setContentView(sheet);

        // Inisialisasi komponen di dalam BottomSheet (menggunakan ID dari dialog_marathon.xml)
        EditText etCount = sheet.findViewById(R.id.etJml);
        EditText etDuration = sheet.findViewById(R.id.etDrs);
        Button btnCalc = sheet.findViewById(R.id.btnHitung);
        TextView tvRes = sheet.findViewById(R.id.tvHasil);

        // Logika saat tombol Hitung ditekan
        btnCalc.setOnClickListener(v -> {
            String inputCount = etCount.getText().toString();
            String inputDuration = etDuration.getText().toString();
            
            if (!inputCount.isEmpty()) {
                int count = Integer.parseInt(inputCount);
                int durationPerMovie = inputDuration.isEmpty() ? 120 : Integer.parseInt(inputDuration);
                
                int totalMinutes = count * durationPerMovie;
                int hours = totalMinutes / 60;
                int mins = totalMinutes % 60;

                // Tampilkan hasil perhitungan
                tvRes.setText("Estimasi Waktu: " + hours + " jam " + mins + " menit");
                tvRes.setVisibility(View.VISIBLE);
            } else {
                // Beri peringatan jika input kosong
                Toast.makeText(getContext(), "Masukkan jumlah film!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show(); // Tampilkan dialog
    }
}
