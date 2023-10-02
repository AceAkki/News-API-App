package com.example.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<NewsArticle> articles;

    private final Context context;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<NewsArticle> articles) {
        this.articles = articles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsArticle article = articles.get(position);

        holder.newsTitle.setText(article.getTitle());
        holder.newsDescription.setText(article.getDescription());
        holder.newsAuthor.setText(article.getAuthor());

        Picasso.get()
                .load(article.getUrlToImage())
                .into(holder.newsImage);

        holder.itemView.setOnClickListener(v -> openArticleInBrowser(article.getUrl()));
    }

    @Override
    public int getItemCount() {
        return articles != null ? articles.size() : 0;
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitle;
        TextView newsDescription;

        TextView newsAuthor;
        ImageView newsImage;



        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.titleTextView);
            newsDescription = itemView.findViewById(R.id.descriptionTextView);
            newsImage = itemView.findViewById(R.id.newsImage);
            newsAuthor =itemView.findViewById(R.id.authorTextView);
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
