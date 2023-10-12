package com.example.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TechNewsFragment extends Fragment {
    private RecyclerView techNewsRecyclerView;
    private NewsAdapter newsAdapter;
    private static final String QUERY = "Artificial Intelligence";

    public TechNewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tech_news, container, false);

        techNewsRecyclerView = view.findViewById(R.id.techNewsRecyclerView);
        techNewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsAdapter = new NewsAdapter(getContext());
        techNewsRecyclerView.setAdapter(newsAdapter);

        fetchNewsArticles();

        return view;
    }

    private void fetchNewsArticles() {
        String apiKey = "d3e5abf2bd454e63b6d226b88b492878"; // API key

        // Retrofit interface for making the API request
        NewsService newsService = RetrofitClient.getClient();
        newsService.getTechNews(apiKey, QUERY).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Successfully fetched news articles
                    newsAdapter.setData(response.body().getArticles());
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
