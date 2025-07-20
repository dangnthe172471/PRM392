package com.example.prm392.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392.BlogDetailActivity;
import com.example.prm392.R;
import com.example.prm392.model.BlogModel;

import java.util.ArrayList;
import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {

    private final Context context;
    private List<BlogModel> blogList;

    // Constructor
    public BlogAdapter(Context context, List<BlogModel> blogList) {
        this.context = context;
        this.blogList = new ArrayList<>(blogList);  // Copy list to avoid unexpected changes
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout (each blog card)
        View view = LayoutInflater.from(context).inflate(R.layout.item_blog_card, parent, false);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        BlogModel blog = blogList.get(position);

        // Set data to the views
        holder.tvDate.setText(blog.getPublishDate());
        holder.tvReadTime.setText(blog.getReadTime());
        holder.tvTitle.setText(blog.getTitle());
        holder.tvViews.setText(blog.getViews() + " views");
        holder.tvLikes.setText(blog.getLikes() + " likes");
        holder.tvComments.setText(blog.getComments() + " comments");

        // Load image using Glide from the URL
        Glide.with(context)
                .load(blog.getImageUrl())  // Load image URL
                .placeholder(R.drawable.sample_blog_image)  // Placeholder while loading
                .error(R.drawable.sample_blog_image)  // Image shown if error occurs
                .into(holder.imgBlog);

        // Set click listener on the item to navigate to BlogDetailActivity
        holder.itemView.setOnClickListener(v -> {
            // Create an Intent to open BlogDetailActivity and pass the blog data
            Intent intent = new Intent(context, BlogDetailActivity.class);
            intent.putExtra("BlogData", blog);  // Pass the blog object
            context.startActivity(intent);  // Start BlogDetailActivity
        });
    }

    @Override
    public int getItemCount() {
        return blogList.size();  // Return the total number of blogs in the list
    }

    // Method to update the list of blogs (for example, after search)
    public void updateData(List<BlogModel> newList) {
        this.blogList = new ArrayList<>(newList);  // Update the list with new data
        notifyDataSetChanged();  // Notify the adapter that the data has changed
    }

    // ViewHolder to bind views
    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBlog;
        TextView tvDate, tvReadTime, tvTitle, tvViews, tvLikes, tvComments;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views by their IDs
            imgBlog = itemView.findViewById(R.id.imgBlog);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvReadTime = itemView.findViewById(R.id.tvReadTime);
            tvTitle = itemView.findViewById(R.id.tvBlogTitle);
            tvViews = itemView.findViewById(R.id.tvViews);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            tvComments = itemView.findViewById(R.id.tvComments);
        }
    }
}
