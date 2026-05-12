package com.example.cinemora.api;

import com.example.cinemora.model.MovieResponse;
import com.example.cinemora.model.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    // Endpoint untuk TMDB (Film)
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey
    );
    
    @GET("movie/now_playing")
    Call<MovieResponse> getNowPlayingMovies(@Query("api_key") String apiKey);

    @GET("movie/upcoming")
    Call<MovieResponse> getUpcomingMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    // --- Endpoint untuk Supabase (Auth) ---

    // Untuk Register: Menambahkan data ke tabel 'users'
    @POST("rest/v1/users")
    Call<Void> registerUser(
            @Header("apikey") String apiKey,
            @Header("Authorization") String bearerToken,
            @Body User user
    );

    // Untuk Login: Mengecek data di tabel 'users'
    @GET("rest/v1/users")
    Call<List<User>> loginUser(
            @Header("apikey") String apiKey,
            @Header("Authorization") String bearerToken,
            @Query("username") String username, // Nanti diisi "eq.namauser"
            @Query("password") String password  // Nanti diisi "eq.passuser"
    );

    // Untuk Update Password: Mengubah data di tabel 'users'
    @PATCH("rest/v1/users")
    Call<Void> updatePassword(
            @Header("apikey") String apiKey,
            @Header("Authorization") String bearerToken,
            @Query("username") String username, // Nanti diisi "eq.namauser"
            @Body Map<String, String> body
    );
}
