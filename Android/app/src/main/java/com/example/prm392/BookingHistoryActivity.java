package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.prm392.Retrofit.ApiService;
import com.example.prm392.adapter.BookingHistoryAdapter;
import com.example.prm392.model.BookingResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookingHistoryAdapter adapter;
    private List<BookingResponse> bookingList;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ApiService apiService;
    private int userId; // Không hardcode nữa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        // Lấy userId từ AccountManager
        userId = com.example.prm392.Retrofit.AccountManager.getUserId(this);

        // Initialize views
        initViews();
        setupToolbar();
        setupRecyclerView();
        setupSwipeRefresh();
        
        // Initialize API service
        apiService = ApiService.api;
        
        // Load booking history
        loadBookingHistory();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewBookingHistory);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Lịch sử đặt lịch");
        }
    }

    private void setupRecyclerView() {
        bookingList = new ArrayList<>();
        adapter = new BookingHistoryAdapter(bookingList, new BookingHistoryAdapter.OnBookingClickListener() {
            @Override
            public void onBookingClick(BookingResponse booking) {
                // Open booking detail
                Intent intent = new Intent(BookingHistoryActivity.this, BookingDetailActivity.class);
                intent.putExtra("booking_id", booking.getId());
                startActivity(intent);
            }
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadBookingHistory();
            }
        });
    }

    private void loadBookingHistory() {
        showLoading(true);
        
        Call<List<BookingResponse>> call = apiService.getUserBookings("all", userId);
        call.enqueue(new Callback<List<BookingResponse>>() {
            @Override
            public void onResponse(Call<List<BookingResponse>> call, Response<List<BookingResponse>> response) {
                showLoading(false);
                swipeRefreshLayout.setRefreshing(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    bookingList.clear();
                    bookingList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    
                    if (bookingList.isEmpty()) {
                        showEmpty(true);
                    } else {
                        showEmpty(false);
                    }
                } else {
                    String errorMsg = "Không thể tải lịch sử đặt lịch";
                    if (response.errorBody() != null) {
                        try {
                            errorMsg += ": " + response.errorBody().string();
                        } catch (Exception e) {
                            // ignore
                        }
                    }
                    Toast.makeText(BookingHistoryActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<BookingResponse>> call, Throwable t) {
                showLoading(false);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(BookingHistoryActivity.this, 
                    "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showEmpty(boolean show) {
        tvEmpty.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 