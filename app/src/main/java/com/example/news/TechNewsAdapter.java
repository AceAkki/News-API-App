package com.example.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TechNewsAdapter extends RecyclerView.Adapter<TechNewsAdapter.TechNewsViewHolder> {
    private List<NewsArticle> techNewsArticles;
    private final Context context;

    public TechNewsAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<NewsArticle> techNewsArticles) {
        this.techNewsArticles = techNewsArticles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TechNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_tech_news, parent, false);
        return new TechNewsViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull TechNewsViewHolder holder, int position) {
        NewsArticle techNewsArticle = techNewsArticles.get(position);

        holder.techTitle.setText(techNewsArticle.getTitle());
        holder.techAuthor.setText(techNewsArticle.getAuthor());


        holder.itemView.setOnClickListener(v -> openArticleInBrowser(techNewsArticle.getUrl()));
    }

    @Override
    public int getItemCount() {
        return techNewsArticles != null ? techNewsArticles.size() : 0;
    }

    public static class TechNewsViewHolder extends RecyclerView.ViewHolder {
        public TextView techTitle;
        public TextView techAuthor;



        public TechNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            techTitle = itemView.findViewById(R.id.techNewsTitleTextView);
            techAuthor = itemView.findViewById(R.id.techNewsAuthorTextView);

        }
    }

    private void openArticleInBrowser(String url) {
        if (url != null && !url.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        }
    }
}
