package com.example.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TechNewsFragment extends Fragment {
    private RecyclerView techNewsRecyclerView;
    private TechNewsAdapter techNewsAdapter;

    public TechNewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tech_news, container, false);

        techNewsRecyclerView = view.findViewById(R.id.techNewsRecyclerView);
        techNewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        techNewsAdapter = new TechNewsAdapter(getContext());
        techNewsRecyclerView.setAdapter(techNewsAdapter);

        // Fetch and display tech news
        fetchTechNews();

        return view;
    }

    private void fetchTechNews() {
        String apiKey = "d3e5abf2bd454e63b6d226b88b492878"; // API key
        String techCategory = "technology"; // Category for tech news

        //  Retrofit interface for making the API request
        NewsService newsService = RetrofitClient.getClient();

        // MAPI request to fetch tech news
        Call<NewsResponse> call = newsService.getTechNews(apiKey, techCategory);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Successfully fetched tech news articles
                    List<NewsArticle> techNewsArticles = response.body().getArticles();
                    techNewsAdapter.setData(techNewsArticles);
                } else {
                    // Handle API error
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                // Handle network or request failure
            }
        });
    }
}
