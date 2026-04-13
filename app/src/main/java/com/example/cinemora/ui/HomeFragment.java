package com.example.cinemora.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import android.view.*;
import com.example.cinemora.R;
import com.example.cinemora.adapter.FilmAdapter;
import com.example.cinemora.model.Film;
import java.util.*;

public class HomeFragment extends Fragment {

    public View onCreateView(LayoutInflater i, ViewGroup c, Bundle b){
        View v = i.inflate(R.layout.fragment_home,c,false);

        RecyclerView rvMostWatched = v.findViewById(R.id.rvMostWatched);
        RecyclerView rvNewReleases = v.findViewById(R.id.rvNewReleases);
        RecyclerView rvAgeBased = v.findViewById(R.id.rvAgeBased);
        RecyclerView rv2022 = v.findViewById(R.id.rv2022);
        RecyclerView rv2021 = v.findViewById(R.id.rv2021);
        RecyclerView rv2020 = v.findViewById(R.id.rv2020);
        RecyclerView rv2019 = v.findViewById(R.id.rv2019);

        // DATA MASTER SEMUA FILM
        ArrayList<Film> allFilms = new ArrayList<>();
        allFilms.add(new Film("Jumbo", "2024", "Anak & Keluarga", R.drawable.jumbo,5,"1h 45m"));
        allFilms.add(new Film("Senin Harga Naik", "2023", "Drama", R.drawable.seninharga,4.8, "1h 30m"));
        allFilms.add(new Film("Siksa Kubur", "2024", "Horor", R.drawable.siksakubur,3,"1h 45m"));
        allFilms.add(new Film("Parasite", "2019", "Thriller", R.drawable.parasite,3.5,"2h 12m"));
        allFilms.add(new Film("Spiderman", "2021", "Hollywood", R.drawable.spiderman,5,"2h 28m"));
        allFilms.add(new Film("Agak Laen 2", "2023", "Komedi", R.drawable.agaklaen,5,"1h 45m"));
        allFilms.add(new Film("Blood and Gold", "2020", "Laga", R.drawable.bloodgold,4.4,"2h 12m"));
        allFilms.add(new Film("Single in Seoul", "2022", "Korea", R.drawable.singleinseoul,3,"1h 45m"));
        allFilms.add(new Film("A Whisker Away", "2019", "Anime", R.drawable.awhiskeraway,3.5,"1h 40m"));
        allFilms.add(new Film("Sumala", "2024", "Horror", R.drawable.sumala,4.3,"1h 45m"));
        allFilms.add(new Film("Don't Move", "2024", "Thriller", R.drawable.dontmove,4.5,"1h 45m"));
        allFilms.add(new Film("Extraction 2", "2023", "Laga", R.drawable.extraction2,4.7,"2h 23m"));
        allFilms.add(new Film("The Deepest Breath", "2023", "Thriller", R.drawable.thedeepestbreath,4.3,"1h 45m"));
        allFilms.add(new Film("The Gray Man", "2022", "Laga", R.drawable.thegrayman,4.4,"2h 28m"));
        allFilms.add(new Film("A Man Called Otto", "2022", "Drama", R.drawable.amancalledotto,4.9,"2h 12m"));
        allFilms.add(new Film("The Adam Project", "2022", "Laga", R.drawable.theadamproject,4.6,"2h 28m"));
        allFilms.add(new Film("Don't Look Up", "2021", "Komedi", R.drawable.dontlookup,4.9,"1h 45m"));
        allFilms.add(new Film("Penyalin Cahaya", "2021", "Drama", R.drawable.penyalincahaya,4.7,"1h 45m"));
        allFilms.add(new Film("A Perfect Fit", "2021", "Drama", R.drawable.aperfectfit,4.5,"1h 45m"));
        allFilms.add(new Film("The Trial Of The Chicago 7", "2020", "Thriller", R.drawable.thetrial,4.5,"1h 45m"));
        allFilms.add(new Film("Guru-Guru Gokil", "2020", "Komedi", R.drawable.gurugokil,4.8,"1h 45m"));
        allFilms.add(new Film("The Old Guard", "2020", "Laga", R.drawable.theoldguard,4.6,"2h 28m"));
        allFilms.add(new Film("Dua Garis Biru", "2019", "Drama", R.drawable.duagarisbiru,4.7,"1h 45m"));
        allFilms.add(new Film("Ratu Ilmu Hitam", "2019", "Horror", R.drawable.ratuilmuhitam,4.9,"1h 45m"));
        // 1. RATING TERTINGGI (Diurutkan dari yang tertinggi)
        ArrayList<Film> highRatedList = filterByRating(allFilms, 4.8);
        Collections.sort(highRatedList, (f1, f2) -> Double.compare(f2.getRating(), f1.getRating()));
        setupSection(rvMostWatched, highRatedList);

        // 2. TAHUN 2024
        setupSection(rvNewReleases, filterByYear(allFilms, "2024"));

        // 3. TAHUN 2023
        setupSection(rvAgeBased, filterByYear(allFilms, "2023"));

        // 4. TAHUN 2022
        setupSection(rv2022, filterByYear(allFilms, "2022"));

        // 5. TAHUN 2021
        setupSection(rv2021, filterByYear(allFilms, "2021"));

        // 6. TAHUN 2020
        setupSection(rv2020, filterByYear(allFilms, "2020"));

        // 7. TAHUN 2019
        setupSection(rv2019, filterByYear(allFilms, "2019"));

        return v;
    }

    private void setupSection(RecyclerView rv, ArrayList<Film> list) {
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv.setAdapter(new FilmAdapter(list));
    }

    private ArrayList<Film> filterByYear(ArrayList<Film> all, String year) {
        ArrayList<Film> filtered = new ArrayList<>();
        for (Film f : all) {
            if (f.getTahun().equals(year)) filtered.add(f);
        }
        return filtered;
    }

    private ArrayList<Film> filterByRating(ArrayList<Film> all, double minRating) {
        ArrayList<Film> filtered = new ArrayList<>();
        for (Film f : all) {
            if (f.getRating() >= minRating) filtered.add(f);
        }
        return filtered;
    }
}