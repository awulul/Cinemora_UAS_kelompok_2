package com.example.cinemora.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemora.R;
import com.example.cinemora.adapter.FilmAdapter;
import com.example.cinemora.model.Film;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {

    private boolean isGridView = true;
    private RecyclerView rv;
    private ImageView btnToggle;
    private TextView chipGenre, chipYear;
    
    private List<Film> fullList = new ArrayList<>();
    private List<Film> filteredList = new ArrayList<>();
    private FilmAdapter adapter;

    private String selectedGenre = "All";
    private String selectedYear = "All";

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup c, Bundle b) {
        View v = i.inflate(R.layout.fragment_explore, c, false);

        // 1. Inisialisasi View
        rv = v.findViewById(R.id.rvExplore);
        btnToggle = v.findViewById(R.id.btnToggleLayout);
        View btnOpenCalculator = v.findViewById(R.id.btnOpenCalculator);
        chipGenre = v.findViewById(R.id.chipGenre);
        chipYear = v.findViewById(R.id.chipYear);

        // 2. Data Master
        initData();
        filteredList.addAll(fullList);

        // 3. Set Adapter
        adapter = new FilmAdapter(filteredList);
        rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rv.setAdapter(adapter);

        // 4. Logika Toggle List/Grid
        btnToggle.setOnClickListener(view -> {
            isGridView = !isGridView;
            if (isGridView) {
                rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
                btnToggle.setImageResource(R.drawable.list);
                adapter.setGridMode(true);
            } else {
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                btnToggle.setImageResource(R.drawable.grid);
                adapter.setGridMode(false);
            }
        });

        // 5. Logika Filter Genre
        chipGenre.setOnClickListener(view -> showGenreDialog());

        // 6. Logika Filter Year
        chipYear.setOnClickListener(view -> showYearDialog());

        // 7. Logika Kalkulator
        btnOpenCalculator.setOnClickListener(view -> showCalculatorDialog());

        return v;
    }

    private void initData() {
        fullList.clear();
        fullList.add(new Film("Jumbo", "2024", "Anak & Keluarga", R.drawable.jumbo,5,"1h 45m"));
        fullList.add(new Film("Senin Harga Naik", "2023", "Drama", R.drawable.seninharga,4.8, "1h 30m"));
        fullList.add(new Film("Siksa Kubur", "2024", "Horor", R.drawable.siksakubur,3,"1h 45m"));
        fullList.add(new Film("Parasite", "2019", "Thriller", R.drawable.parasite,3.5,"2h 12m"));
        fullList.add(new Film("Spiderman", "2021", "Hollywood", R.drawable.spiderman,5,"2h 28m"));
        fullList.add(new Film("Agak Laen 2", "2023", "Komedi", R.drawable.agaklaen,5,"1h 45m"));
        fullList.add(new Film("Blood and Gold", "2020", "Laga", R.drawable.bloodgold,4.4,"2h 12m"));
        fullList.add(new Film("Single in Seoul", "2022", "Korea", R.drawable.singleinseoul,3,"1h 45m"));
        fullList.add(new Film("A Whisker Away", "2019", "Anime", R.drawable.awhiskeraway,3.5,"1h 40m"));
        fullList.add(new Film("Sumala", "2024", "Horror", R.drawable.sumala,4.3,"1h 45m"));
        fullList.add(new Film("Don't Move", "2024", "Thriller", R.drawable.dontmove,4.5,"1h 45m"));
        fullList.add(new Film("Extraction 2", "2023", "Laga", R.drawable.extraction2,4.7,"2h 23m"));
        fullList.add(new Film("The Deepest Breath", "2023", "Thriller", R.drawable.thedeepestbreath,4.3,"1h 45m"));
        fullList.add(new Film("The Gray Man", "2022", "Laga", R.drawable.thegrayman,4.4,"2h 28m"));
        fullList.add(new Film("A Man Called Otto", "2022", "Drama", R.drawable.amancalledotto,4.9,"2h 12m"));
        fullList.add(new Film("The Adam Project", "2022", "Laga", R.drawable.theadamproject,4.6,"2h 28m"));
        fullList.add(new Film("Don't Look Up", "2021", "Komedi", R.drawable.dontlookup,4.9,"1h 45m"));
        fullList.add(new Film("Penyalin Cahaya", "2021", "Drama", R.drawable.penyalincahaya,4.7,"1h 45m"));
        fullList.add(new Film("A Perfect Fit", "2021", "Drama", R.drawable.aperfectfit,4.5,"1h 45m"));
        fullList.add(new Film("The Trial Of The Chicago 7", "2020", "Thriller", R.drawable.thetrial,4.5,"1h 45m"));
        fullList.add(new Film("Guru-Guru Gokil", "2020", "Komedi", R.drawable.gurugokil,4.8,"1h 45m"));
        fullList.add(new Film("The Old Guard", "2020", "Laga", R.drawable.theoldguard,4.6,"2h 28m"));
        fullList.add(new Film("Dua Garis Biru", "2019", "Drama", R.drawable.duagarisbiru,4.7,"1h 45m"));
        fullList.add(new Film("Ratu Ilmu Hitam", "2019", "Horror", R.drawable.ratuilmuhitam,4.9,"1h 45m"));
    }

    private void showGenreDialog() {
        String[] genres = {"All", "Anak & Keluarga", "Anime", "Drama", "Horor", "Hollywood", "Korea", "Thriller", "Laga", "Komedi"};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Pilih Genre");
        builder.setItems(genres, (dialog, which) -> {
            selectedGenre = genres[which];
            chipGenre.setText(selectedGenre);
            applyFilter();
        });
        builder.show();
    }

    private void showYearDialog() {
        String[] years = {"All", "2024", "2023", "2022", "2021", "2020", "2019"};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Pilih Tahun");
        builder.setItems(years, (dialog, which) -> {
            selectedYear = years[which];
            chipYear.setText(selectedYear);
            applyFilter();
        });
        builder.show();
    }

    private void applyFilter() {
        filteredList.clear();
        for (Film film : fullList) {
            boolean genreMatch = selectedGenre.equals("All") || film.getGenre().equalsIgnoreCase(selectedGenre);
            boolean yearMatch = selectedYear.equals("All") || film.getTahun().equals(selectedYear);
            
            if (genreMatch && yearMatch) {
                filteredList.add(film);
            }
        }
        adapter.notifyDataSetChanged();
        
        // Update UI Chip (Warna)
        updateChipUI(chipGenre, !selectedGenre.equals("All"));
        updateChipUI(chipYear, !selectedYear.equals("All"));
    }

    private void updateChipUI(TextView chip, boolean isActive) {
        if (isActive) {
            chip.setBackgroundResource(R.drawable.bg_chip_selected);
            chip.setTextColor(getResources().getColor(R.color.white));
        } else {
            chip.setBackgroundResource(R.drawable.bg_chip_unselected);
            chip.setTextColor(getResources().getColor(R.color.textColor));
        }
    }

    private void showCalculatorDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_marathon, null);

        EditText etJumlah = dialogView.findViewById(R.id.etJml);
        EditText etDurasi = dialogView.findViewById(R.id.etDrs);
        Button btnHitung = dialogView.findViewById(R.id.btnHitung);
        TextView tvHasil = dialogView.findViewById(R.id.tvHasil);

        btnHitung.setOnClickListener(v -> {
            String jmlStr = etJumlah.getText().toString();
            String durStr = etDurasi.getText().toString();

            if (!jmlStr.isEmpty() && !durStr.isEmpty()) {
                int jumlah = Integer.parseInt(jmlStr);
                int durasi = Integer.parseInt(durStr);
                int totalMenit = jumlah * durasi;

                int jam = totalMenit / 60;
                int menit = totalMenit % 60;

                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.add(java.util.Calendar.MINUTE, totalMenit);
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
                String waktuSelesai = sdf.format(cal.getTime());

                String hasil = "Total Waktu: " + jam + " Jam " + menit + " Menit\n" +
                        "Estimasi Selesai: Pukul " + waktuSelesai;

                tvHasil.setText(hasil);
                tvHasil.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getContext(), "Isi semua data!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.setContentView(dialogView);
        dialog.show();
    }
}