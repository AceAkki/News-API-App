package com.example.news;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {
    @GET("top-headlines")
    Call<NewsResponse> getTopHeadlines(
            @Query("apiKey") String apiKey,
            @Query("country") String country
    );

    @GET("top-headlines")
    Call<NewsResponse> getTechNews(
            @Query("apiKey") String apiKey,
            @Query("category") String category
    );

    @GET("top-headlines")
    Call<NewsResponse> getNewsByCategory(
            @Query("apiKey") String apiKey,
            @Query("country") String country,
            @Query("category") String category

    );



    @GET("everything")
    Call<NewsResponse> getNewsByKeyword(
            @Query("apiKey") String apiKey,
            @Query("q") String keyword,
            @Query("sortBy") String sortBy
    );

}
