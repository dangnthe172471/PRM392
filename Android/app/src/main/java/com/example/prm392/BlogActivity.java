package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import com.example.prm392.R;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.Retrofit.AccountManager;
import com.example.prm392.adapter.BlogAdapter;
import com.example.prm392.database.BlogRepository;
import com.example.prm392.model.BlogModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class BlogActivity extends AppCompatActivity {

    private RecyclerView recyclerBlogs;
    private BlogAdapter blogAdapter;
    private EditText etSearchBlog;
    private BottomNavigationView bottomNavigationView;
    private List<BlogModel> fullBlogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_blog);

        // Ánh xạ view
        recyclerBlogs = findViewById(R.id.recyclerBlogs);
        etSearchBlog = findViewById(R.id.etSearchBlog);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        recyclerBlogs.setLayoutManager(new LinearLayoutManager(this));

        // Load dữ liệu
        loadBlogData();

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

        // Xử lý click BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(BlogActivity.this, MainActivity.class));
                overridePendingTransition(0,0);
                finish(); // Đóng BlogActivity để tránh stack quá nhiều
                return true;
            } else if (id == R.id.nav_bookings) {
                // Đã ở bookings (blog) rồi
                return true;
            } else if (id == R.id.nav_chat) {
                // startActivity(new Intent(BlogActivity.this, ChatActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                int userId = AccountManager.getUserId(this);
                if(userId != -1) {
                    startActivity(new Intent(BlogActivity.this, ProfileActivity.class));
                } else {
                    startActivity(new Intent(BlogActivity.this, LoginActivity.class));
                }
                return true;
            }

            return false;
        });


        // Set mặc định tab Blog được chọn
        bottomNavigationView.setSelectedItemId(R.id.nav_bookings);  // hoặc R.id.nav_blog nếu bạn thêm Blog vào menu
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
