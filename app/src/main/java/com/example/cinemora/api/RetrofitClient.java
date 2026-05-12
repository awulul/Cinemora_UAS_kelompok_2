package com.example.cinemora.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String SUPABASE_BASE_URL = "https://qzshdotwjkergzrnmnno.supabase.co/";

    private static Retrofit retrofitTmdb;
    private static Retrofit retrofitSupabase;

    // Default client (TMDB)
    public static Retrofit getClient() {
        return getTmdbClient();
    }

    // Client untuk mengambil data Film (TMDB)
    public static Retrofit getTmdbClient() {
        if (retrofitTmdb == null) {
            retrofitTmdb = new Retrofit.Builder()
                    .baseUrl(TMDB_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitTmdb;
    }

    // Client untuk Autentikasi User (Supabase)
    public static Retrofit getSupabaseClient() {
        if (retrofitSupabase == null) {
            retrofitSupabase = new Retrofit.Builder()
                    .baseUrl(SUPABASE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitSupabase;
    }
}
