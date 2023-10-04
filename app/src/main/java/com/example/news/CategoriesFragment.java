package com.example.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesFragment extends Fragment {
    private Spinner categorySpinner;
    private Button searchButton;
    private NewsAdapter newsAdapter;
    private List<NewsArticle> newsArticles;
    private static final String COUNTRY_CODE = "us"; // Set the country code to "us" for all categories

    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        categorySpinner = view.findViewById(R.id.categorySpinner);
        searchButton = view.findViewById(R.id.searchButton);

        // Initialize ArrayAdapter for category selection
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.category_options, // Reference to the category_options array
                android.R.layout.simple_spinner_item
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Initialize RecyclerView for displaying search results
        RecyclerView recyclerView = view.findViewById(R.id.categoriesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsAdapter = new NewsAdapter(getContext());
        recyclerView.setAdapter(newsAdapter);

        // Initialize the list to store search results
        newsArticles = new ArrayList<>();

        // Set up click listener for the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performCategorySearch();
            }
        });

        return view;
    }

    private void performCategorySearch() {
        String apiKey = "d3e5abf2bd454e63b6d226b88b492878"; // Replace with your News API key
        String selectedCategory = categorySpinner.getSelectedItem().toString().toLowerCase();
        String countryCode = "us"; // Set the country code to "de" for Germany, you can change this as needed

        // Make an API request to fetch news articles based on the selected category and country
        NewsService newsService = RetrofitClient.getClient();
        newsService.getNewsByCategory(apiKey, countryCode, selectedCategory).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Successfully fetched category results
                    newsArticles = response.body().getArticles();
                    newsAdapter.setData(newsArticles); // Update the RecyclerView
                } else {
                    // Handle API error
                    Toast.makeText(getContext(), "Failed to fetch category results", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                // Handle network or request failure
                Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
