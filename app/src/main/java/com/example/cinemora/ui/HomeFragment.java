package com.example.cinemora.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import android.view.*;
import android.widget.Toast;

import com.example.cinemora.R;
import com.example.cinemora.adapter.FilmAdapter;
import com.example.cinemora.api.ApiService;
import com.example.cinemora.api.RetrofitClient;
import com.example.cinemora.model.MovieResponse;
import com.example.cinemora.model.Movie;
import com.example.cinemora.model.Film;

import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private ApiService apiService;
    private final String API_KEY = "17a371ceb7024f246d53bf2803eedc73";

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup c, Bundle b) {
        View v = i.inflate(R.layout.fragment_home, c, false);

        // Perbaikan: Gunakan getClient() sesuai dengan yang ada di RetrofitClient.java
        apiService = RetrofitClient.getClient().create(ApiService.class);

        RecyclerView rvPopuler = v.findViewById(R.id.rvMostWatched);
        RecyclerView rvSedangDiputar = v.findViewById(R.id.rvNewReleases);
        RecyclerView rvMendatang = v.findViewById(R.id.rvAgeBased);
        RecyclerView rvTopRating = v.findViewById(R.id.rv2022);

        fetchMoviesFromApi(apiService.getPopularMovies(API_KEY), rvPopuler);
        fetchMoviesFromApi(apiService.getNowPlayingMovies(API_KEY), rvSedangDiputar);
        fetchMoviesFromApi(apiService.getUpcomingMovies(API_KEY), rvMendatang);
        fetchMoviesFromApi(apiService.getTopRatedMovies(API_KEY), rvTopRating);

        return v;
    }

    private void fetchMoviesFromApi(Call<MovieResponse> call, RecyclerView recyclerView) {
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> apiMovies = response.body().getResults();
                    ArrayList<Film> filmList = new ArrayList<>();
                    if (apiMovies != null) {
                        for (Movie m : apiMovies) {
                            filmList.add(new Film(
                                    m.getTitle(),
                                    m.getReleaseDate(),
                                    "Movie",
                                    m.getPosterPath(), 
                                    m.getVoteAverage(),
                                    "2h"
                            ));
                        }
                    }
                    setupRecyclerView(recyclerView, filmList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Gagal memuat API", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupRecyclerView(RecyclerView rv, ArrayList<Film> list) {
        if (rv != null) {
            rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            rv.setAdapter(new FilmAdapter(list));
        }
    }
}