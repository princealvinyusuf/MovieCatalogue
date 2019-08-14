package com.intent.moviecatalogue.interfaces;

import com.intent.moviecatalogue.model.FeedBackMovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServiceInterface {
    @GET("{catalogue}/popular")
    Call<FeedBackMovies> getType(@Path("catalogue") String catalogueName,
                                 @Query("api_key") String apiKey);

    @GET("search/{catalogue}")
    Call<FeedBackMovies> getSearch(@Path("catalogue") String catalogueName,
                                   @Query("api_key") String apiKey,
                                   @Query("query") String title);

    @GET("/3/movie/upcoming")
    Call<FeedBackMovies> getNew(@Query("api_key") String apiKey);
}
