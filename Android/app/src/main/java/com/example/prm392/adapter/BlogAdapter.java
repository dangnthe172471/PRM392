package com.example.prm392.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392.R;
import com.example.prm392.model.BlogModel;

import java.util.ArrayList;
import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {

    private final Context context;
    private List<BlogModel> blogList;

    public BlogAdapter(Context context, List<BlogModel> blogList) {
        this.context = context;
        this.blogList = new ArrayList<>(blogList);
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_blog_card, parent, false);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        BlogModel blog = blogList.get(position);

        holder.tvDate.setText(blog.getPublishDate());
        holder.tvReadTime.setText(blog.getReadTime());
        holder.tvTitle.setText(blog.getTitle());
        holder.tvViews.setText(blog.getViews() + " views");
        holder.tvLikes.setText(blog.getLikes() + " likes");
        holder.tvComments.setText(blog.getComments() + " comments");

        // Load ảnh từ URL bằng Glide
        Glide.with(context)
                .load(blog.getImageUrl())
                .placeholder(R.drawable.sample_blog_image) // hiển thị tạm khi đang load
                .error(R.drawable.sample_blog_image)       // hiển thị khi lỗi
                .into(holder.imgBlog);
    }


    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public void updateData(List<BlogModel> newList) {
        this.blogList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBlog;
        TextView tvDate, tvReadTime, tvTitle, tvViews, tvLikes, tvComments;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
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
