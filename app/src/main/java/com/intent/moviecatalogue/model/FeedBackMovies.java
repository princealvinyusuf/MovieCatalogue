package com.intent.moviecatalogue.model;

import com.google.gson.annotations.SerializedName;
import com.intent.moviecatalogue.model.Movie;

import java.util.ArrayList;

public class FeedBackMovies {


    @SerializedName("results")
    private ArrayList<Movie> movies;

    public ArrayList<Movie> getCatalogues() {
        return movies;
    }
}
