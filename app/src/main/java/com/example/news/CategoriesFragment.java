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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesFragment extends Fragment {
    private Spinner categorySpinner;
    private Spinner countrySpinner;
    private Button searchButton;
    private NewsAdapter newsAdapter;
    private List<NewsArticle> newsArticles;
    private static final String DEFAULT_COUNTRY_CODE = "us"; // Default country code
    private static final Map<String, String> COUNTRY_CODE_MAP = new HashMap<String, String>() {{
        put("United States", "us");
        put("United Kingdom", "gb");
        put("United Arab Emirates", "ae");
        put("Argentina", "ar");
        put("Australia", "au");
        put("Belgium", "be");
        put("Brazil", "br");
        put("Canada", "ca");
        put("Colombia", "co");
        put("Cuba", "cu");
        put("France", "fr");
        put("Germany", "de");
        put("Hong Kong", "hk");
        put("India", "in");
        put("Israel", "il");
        put("Japan", "jp");
        put("South Korea", "kr");
        put("Mexico", "mx");
        put("Singapore", "sg");
        put("Taiwan", "tw");
        put("Ukraine", "ua");
        put("Venezuela", "ve");
        put("South Africa", "za");

    }};


    public CategoriesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        categorySpinner = view.findViewById(R.id.categorySpinner);
        countrySpinner = view.findViewById(R.id.countrySpinner);
        searchButton = view.findViewById(R.id.searchButton);


        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.category_options,
                android.R.layout.simple_spinner_item
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);


        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.country_options,
                android.R.layout.simple_spinner_item
        );
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryAdapter);

        RecyclerView recyclerView = view.findViewById(R.id.categoriesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsAdapter = new NewsAdapter(getContext());
        recyclerView.setAdapter(newsAdapter);


        newsArticles = new ArrayList<>();


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performCategorySearch();
            }
        });

        return view;
    }

    private void performCategorySearch() {
        String apiKey = "d3e5abf2bd454e63b6d226b88b492878";
        String selectedCategory = categorySpinner.getSelectedItem().toString().toLowerCase();

        // Get the selected country name and map it to the country code
        String selectedCountryName = countrySpinner.getSelectedItem().toString();
        String countryCode = COUNTRY_CODE_MAP.get(selectedCountryName);

        if (countryCode == null) {
            // Handle the case when the selected country name doesn't have a corresponding country code
            Toast.makeText(getContext(), "Invalid country selection", Toast.LENGTH_SHORT).show();
            return;
        }

        // Make an API request to fetch news articles based on the selected category and country code
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
