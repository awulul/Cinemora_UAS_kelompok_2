package com.example.cinemora.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.cinemora.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * MainActivity adalah Activity utama yang berfungsi sebagai "Host" atau wadah
 * bagi Fragment-Fragment (Home, Explore, Account) dalam aplikasi Cinemora.
 */
public class MainActivity extends AppCompatActivity {

    // Metode yang dipanggil pertama kali saat aplikasi dijalankan
    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);
        // Menentukan layout XML yang digunakan oleh activity ini
        setContentView(R.layout.activity_main);

        // Inisialisasi BottomNavigationView (Menu Navigasi Bawah) dari layout
        BottomNavigationView nav = findViewById(R.id.bottomNav);

        // Menampilkan Fragment Home sebagai tampilan awal saat aplikasi pertama kali dibuka
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new HomeFragment()).commit();

        // Mengatur logika ketika salah satu menu di navigasi bawah diklik oleh pengguna
        nav.setOnItemSelectedListener(i->{
            Fragment f=null; // Variabel sementara untuk menampung fragment yang dipilih
            
            // Cek ID menu yang diklik dan buat objek fragment yang sesuai
            if(i.getItemId()==R.id.menu_home) f=new HomeFragment();      // Jika menu Home diklik
            if(i.getItemId()==R.id.menu_explore) f=new ExploreFragment(); // Jika menu Explore diklik
            if(i.getItemId()==R.id.menu_account) f=new AccountFragment(); // Jika menu Account diklik

            // Melakukan transaksi untuk mengganti tampilan di dalam 'container' dengan fragment terpilih
            if (f != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container,f).commit();
            }
            return true; // Mengembalikan nilai true agar menu terlihat seperti "terpilih" (ter-highlight)
        });
    }
}