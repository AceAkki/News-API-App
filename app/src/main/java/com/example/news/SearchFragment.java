package com.example.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private EditText searchEditText;
    private Spinner sortSpinner;
    private Button searchButton;
    private NewsAdapter newsAdapter;
    private List<NewsArticle> newsArticles;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchEditText = view.findViewById(R.id.searchEditText);
        sortSpinner = view.findViewById(R.id.sortSpinner);
        searchButton = view.findViewById(R.id.searchButton);

        // Initialize ArrayAdapter for the sorting options
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.sort_options, // Reference to the sort_options array
                android.R.layout.simple_spinner_item
        );
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);

        // Initialize RecyclerView for displaying search results
        RecyclerView recyclerView = view.findViewById(R.id.searchRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsAdapter = new NewsAdapter(getContext());
        recyclerView.setAdapter(newsAdapter);

        // Initialize the list to store search results
        newsArticles = new ArrayList<>();

        newsAdapter = new NewsAdapter(getContext());
        recyclerView.setAdapter(newsAdapter);

        // Set up click listener for the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        return view;
    }

    private void performSearch() {
        String apiKey = "d3e5abf2bd454e63b6d226b88b492878"; 
        String query = searchEditText.getText().toString().trim();
        String selectedSort = sortSpinner.getSelectedItem().toString().toLowerCase();

        if (!query.isEmpty()) {
            
            NewsService newsService = RetrofitClient.getClient();
            newsService.getNewsByKeyword(apiKey, query, selectedSort).enqueue(new Callback<NewsResponse>() {
                @Override
                public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Successfully fetched search results
                        newsArticles = response.body().getArticles();
                        sortNewsArticles(selectedSort); // Sort the articles
                        newsAdapter.setData(newsArticles); // Update the RecyclerView
                    } else {
                        // Handle API error
                        Toast.makeText(getContext(), "Failed to fetch search results", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                    // Handle network or request failure
                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Show a message if the search query is empty
            Toast.makeText(getContext(), "Please enter a search query", Toast.LENGTH_SHORT).show();
        }
    }

    private void sortNewsArticles(String selectedSort) {
        
        if ("relevance".equals(selectedSort)) {
            
            Collections.sort(newsArticles, new Comparator<NewsArticle>() {
                @Override
                public int compare(NewsArticle article1, NewsArticle article2) {
                    
                    int relevance1 = calculateRelevance(article1);
                    int relevance2 = calculateRelevance(article2);

                    
                    return Integer.compare(relevance2, relevance1);
                }

                private int calculateRelevance(NewsArticle article) {
                    String query = searchEditText.getText().toString().toLowerCase();
                    String title = article.getTitle().toLowerCase();
                    String description = article.getDescription().toLowerCase();

                
                    int titleMatchCount = countMatches(title, query);
                    int descriptionMatchCount = countMatches(description, query);

                    
                    return titleMatchCount * 2 + descriptionMatchCount;
                }

                private int countMatches(String text, String query) {
                    int count = 0;
                    int index = text.indexOf(query);
                    while (index != -1) {
                        count++;
                        index = text.indexOf(query, index + query.length());
                    }
                    return count;
                }
            });

        } else if ("latest".equals(selectedSort)) {
            // Sort by latest (you can implement your sorting logic here)
            Collections.sort(newsArticles, new Comparator<NewsArticle>() {
                @Override
                public int compare(NewsArticle article1, NewsArticle article2) {
                    // Compare articles by publication date to determine relevance
                    // Parse the publication date into Date objects
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    Date date1, date2;
                    try {
                        date1 = dateFormat.parse(article1.getPublishedAt());
                        date2 = dateFormat.parse(article2.getPublishedAt());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0; // Handle parsing errors
                    }

                    // Compare the publication dates
                    return date2.compareTo(date1); // Sort in descending order for relevance
                }
            });

              }

    }
}
