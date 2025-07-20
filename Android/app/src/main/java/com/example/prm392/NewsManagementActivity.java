package com.example.prm392;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.prm392.adapter.NewsArticleAdapter;
import com.example.prm392.model.NewsArticle;
import com.example.prm392.Retrofit.ApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.appbar.MaterialToolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.prm392.model.NewsListResponse;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import com.example.prm392.model.CreateNewsArticleRequest;

public class NewsManagementActivity extends AppCompatActivity {
    private NewsArticleAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private List<NewsArticle> newsList = new ArrayList<>();
    private static final int PICK_IMAGE_REQUEST = 1002;
    private Uri selectedImageUri;
    private Dialog createDialog;
    private List<String> categoryNames = new ArrayList<>();
    private List<com.example.prm392.model.NewsArticle.Category> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_management);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_news);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quản lý bài viết");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerNews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsArticleAdapter(this, newsList, new NewsArticleAdapter.OnNewsActionListener() {
            @Override
            public void onEdit(NewsArticle article) {
                Toast.makeText(NewsManagementActivity.this, "Sửa: " + article.getTitle(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onDelete(NewsArticle article) {
                Toast.makeText(NewsManagementActivity.this, "Xóa: " + article.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this::loadNews);

        FloatingActionButton fab = findViewById(R.id.fabAddNews);
        fab.setOnClickListener(v -> showCreateNewsDialog());

        loadNews();
    }

    private void loadNews() {
        swipeRefreshLayout.setRefreshing(true);
        ApiService.api.getNewsArticles(1, 50).enqueue(new Callback<NewsListResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsListResponse> call, @NonNull Response<NewsListResponse> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    newsList = response.body().getItems();
                    adapter.setNewsList(newsList);
                } else {
                    Toast.makeText(NewsManagementActivity.this, "Không thể tải danh sách bài viết", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<NewsListResponse> call, @NonNull Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(NewsManagementActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCreateNewsDialog() {
        createDialog = new Dialog(this);
        createDialog.setContentView(R.layout.dialog_create_news_article);
        createDialog.setCancelable(true);
        Window window = createDialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
        EditText edtTitle = createDialog.findViewById(R.id.edtTitle);
        EditText edtExcerpt = createDialog.findViewById(R.id.edtExcerpt);
        EditText edtContent = createDialog.findViewById(R.id.edtContent);
        Spinner spinnerCategory = createDialog.findViewById(R.id.spinnerCategory);
        Button btnPickImage = createDialog.findViewById(R.id.btnPickImage);
        ImageView imgPreview = createDialog.findViewById(R.id.imgPreview);
        Button btnSave = createDialog.findViewById(R.id.btnSave);
        Button btnCancel = createDialog.findViewById(R.id.btnCancel);
        btnPickImage.setOnClickListener(v -> pickImageFromGallery());
        btnCancel.setOnClickListener(v -> createDialog.dismiss());
        // Gọi API lấy danh mục
        ApiService.api.getCategories().enqueue(new retrofit2.Callback<List<com.example.prm392.model.NewsArticle.Category>>() {
            @Override
            public void onResponse(retrofit2.Call<List<com.example.prm392.model.NewsArticle.Category>> call, retrofit2.Response<List<com.example.prm392.model.NewsArticle.Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList = response.body();
                    categoryNames.clear();
                    for (com.example.prm392.model.NewsArticle.Category c : categoryList) {
                        categoryNames.add(c.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(NewsManagementActivity.this, android.R.layout.simple_spinner_item, categoryNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(retrofit2.Call<List<com.example.prm392.model.NewsArticle.Category>> call, Throwable t) {
                Toast.makeText(NewsManagementActivity.this, "Không thể tải danh mục", Toast.LENGTH_SHORT).show();
            }
        });
        btnSave.setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            String excerpt = edtExcerpt.getText().toString().trim();
            String content = edtContent.getText().toString().trim();
            int selectedPos = spinnerCategory.getSelectedItemPosition();
            com.example.prm392.model.NewsArticle.Category selectedCategory = (selectedPos >= 0 && selectedPos < categoryList.size()) ? categoryList.get(selectedPos) : null;
            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content) || selectedCategory == null) {
                Toast.makeText(this, "Vui lòng nhập đủ tiêu đề, nội dung, chọn danh mục", Toast.LENGTH_SHORT).show();
                return;
            }
            btnSave.setEnabled(false);
            CreateNewsArticleRequest req = new CreateNewsArticleRequest();
            req.setTitle(title);
            req.setExcerpt(excerpt);
            req.setContent(content);
            req.setCategoryId(selectedCategory.getId());
            req.setImageUrl("");
            ApiService.api.createArticle(req).enqueue(new retrofit2.Callback<NewsArticle>() {
                @Override
                public void onResponse(retrofit2.Call<NewsArticle> call, retrofit2.Response<NewsArticle> response) {
                    btnSave.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(NewsManagementActivity.this, "Tạo bài viết thất bại!", Toast.LENGTH_SHORT).show();
                    } else {
                        createDialog.dismiss();
                        Toast.makeText(NewsManagementActivity.this, "Đã tạo bài viết mới!", Toast.LENGTH_SHORT).show();
                        loadNews();
                    }
                }
                @Override
                public void onFailure(retrofit2.Call<NewsArticle> call, Throwable t) {
                    btnSave.setEnabled(true);
                    Toast.makeText(NewsManagementActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        if (selectedImageUri != null) {
            imgPreview.setVisibility(View.VISIBLE);
            Glide.with(this).load(selectedImageUri).into(imgPreview);
        } else {
            imgPreview.setVisibility(View.GONE);
        }
        createDialog.show();
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            if (createDialog != null && createDialog.isShowing()) {
                ImageView imgPreview = createDialog.findViewById(R.id.imgPreview);
                imgPreview.setVisibility(View.VISIBLE);
                Glide.with(this).load(selectedImageUri).into(imgPreview);
            }
        }
    }
} 