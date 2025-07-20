package com.example.prm392.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.prm392.R;
import com.example.prm392.model.NewsArticle;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsArticleAdapter extends RecyclerView.Adapter<NewsArticleAdapter.NewsViewHolder> {
    public interface OnNewsActionListener {
        void onEdit(NewsArticle article);
        void onDelete(NewsArticle article);
    }

    private final Context context;
    private List<NewsArticle> newsList;
    private final OnNewsActionListener listener;

    public NewsArticleAdapter(Context context, List<NewsArticle> newsList, OnNewsActionListener listener) {
        this.context = context;
        this.newsList = newsList;
        this.listener = listener;
    }

    public void setNewsList(List<NewsArticle> newsList) {
        this.newsList = newsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news_article, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsArticle article = newsList.get(position);
        holder.tvTitle.setText(article.getTitle());
        holder.tvExcerpt.setText(article.getExcerpt() != null ? article.getExcerpt() : "");
        holder.tvCategory.setText(article.getCategory() != null ? article.getCategory().getName() : "");
        // Định dạng ngày đăng
        String dateStr = "";
        if (article.getPublishDate() != null) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(article.getPublishDate());
                dateStr = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date);
            } catch (ParseException e) {
                dateStr = article.getPublishDate();
            }
        }
        String author = article.getAuthor() != null ? article.getAuthor().getName() : "";
        holder.tvMeta.setText(dateStr + (author.isEmpty() ? "" : " • " + author));
        if (article.getImageUrl() != null && !article.getImageUrl().isEmpty()) {
            Glide.with(context)
                .load(article.getImageUrl())
                .placeholder(R.drawable.sample_blog_image)
                .into(holder.imgNews);
        } else {
            holder.imgNews.setImageResource(R.drawable.sample_blog_image);
        }
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(article));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(article));
    }

    @Override
    public int getItemCount() {
        return newsList != null ? newsList.size() : 0;
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView imgNews;
        TextView tvTitle, tvCategory, tvExcerpt, tvMeta;
        ImageButton btnEdit, btnDelete;
        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            imgNews = itemView.findViewById(R.id.imgNews);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvExcerpt = itemView.findViewById(R.id.tvExcerpt);
            tvMeta = itemView.findViewById(R.id.tvMeta);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
} 