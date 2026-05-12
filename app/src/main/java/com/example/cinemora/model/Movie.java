package com.example.cinemora.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Movie {

    private String title;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("genre_ids")
    private List<Integer> genreIds; // Menambahkan list ID genre dari API

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }
}