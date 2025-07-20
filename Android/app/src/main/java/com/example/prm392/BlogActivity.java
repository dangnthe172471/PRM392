package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import com.example.prm392.R;


import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.Retrofit.AccountManager;
import com.example.prm392.adapter.BlogAdapter;
import com.example.prm392.database.BlogRepository;
import com.example.prm392.model.BlogModel;

import java.util.ArrayList;
import java.util.List;

public class BlogActivity extends BaseActivity {

    private RecyclerView recyclerBlogs;
    private BlogAdapter blogAdapter;
    private EditText etSearchBlog;
    private List<BlogModel> fullBlogList;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_blog;
    }

    @Override
    protected void initViews() {
        // Ánh xạ view
        recyclerBlogs = findViewById(R.id.recyclerBlogs);
        etSearchBlog = findViewById(R.id.etSearchBlog);

        recyclerBlogs.setLayoutManager(new LinearLayoutManager(this));

        // Load dữ liệu
        loadBlogData();
    }

    @Override
    protected void setupListeners() {
        // Tìm kiếm realtime
        etSearchBlog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchBlogsByTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    @Override
    protected int getDefaultSelectedTab() {
        return R.id.nav_chat;
    }

    private void loadBlogData() {
        fullBlogList = BlogRepository.getAllBlogs();
        blogAdapter = new BlogAdapter(this, fullBlogList);
        recyclerBlogs.setAdapter(blogAdapter);
    }

    private void searchBlogsByTitle(String query) {
        List<BlogModel> filtered = BlogRepository.searchBlogsByTitle(query);
        blogAdapter.updateData(filtered);
    }
}
