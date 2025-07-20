package com.example.prm392;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RatingBar;
import android.widget.Button;
import android.content.Intent;
import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.prm392.Retrofit.ApiService;
import com.example.prm392.model.BookingResponse;
import com.example.prm392.model.ReviewResponse;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

public class BookingDetailActivity extends AppCompatActivity {
    private TextView tvServiceName, tvAreaSize, tvTimeSlot, tvBookingDate;
    private TextView tvAddress, tvContactName, tvContactPhone, tvNotes;
    private TextView tvTotalPrice, tvStatus, tvCleanerName, tvCreatedAt;
    private View layoutNotes, layoutCleaner;
    private ProgressBar progressBar;
    private View contentLayout;
    private ApiService apiService;
    private int bookingId;
    private int userId; // Không hardcode nữa
    private TextView tvFeedbackComment;
    private Button btnFeedback;
    private ReviewResponse currentReview;
    private BookingResponse currentBooking;
    private static final int REQUEST_FEEDBACK = 1001;
    private RatingBar ratingBarFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        // Lấy userId từ AccountManager
        userId = com.example.prm392.Retrofit.AccountManager.getUserId(this);

        // Get booking ID from intent
        bookingId = getIntent().getIntExtra("booking_id", -1);
        if (bookingId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin đặt lịch", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        initViews();
        setupToolbar();
        
        // Initialize API service
        apiService = ApiService.api;
        
        // Load booking detail
        loadBookingDetail();
        // Load feedback
        loadFeedback();
    }

    private void initViews() {
        tvServiceName = findViewById(R.id.tvServiceName);
        tvAreaSize = findViewById(R.id.tvAreaSize);
        tvTimeSlot = findViewById(R.id.tvTimeSlot);
        tvBookingDate = findViewById(R.id.tvBookingDate);
        tvAddress = findViewById(R.id.tvAddress);
        tvContactName = findViewById(R.id.tvContactName);
        tvContactPhone = findViewById(R.id.tvContactPhone);
        tvNotes = findViewById(R.id.tvNotes);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvStatus = findViewById(R.id.tvStatus);
        tvCleanerName = findViewById(R.id.tvCleanerName);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        layoutNotes = findViewById(R.id.layoutNotes);
        layoutCleaner = findViewById(R.id.layoutCleaner);
        progressBar = findViewById(R.id.progressBar);
        contentLayout = findViewById(R.id.contentLayout);
        tvFeedbackComment = findViewById(R.id.tvFeedbackComment);
        btnFeedback = findViewById(R.id.btnFeedback);
        ratingBarFeedback = findViewById(R.id.ratingBarFeedback);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chi tiết đặt lịch");
        }
    }

    private void loadBookingDetail() {
        showLoading(true);
        
        Call<BookingResponse> call = apiService.getBookingById(bookingId, userId);
        call.enqueue(new Callback<BookingResponse>() {
            @Override
            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                showLoading(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    displayBookingDetail(response.body());
                } else {
                    String errorMsg = "Không thể tải thông tin đặt lịch";
                    if (response.errorBody() != null) {
                        try {
                            errorMsg += ": " + response.errorBody().string();
                        } catch (Exception e) {
                            // ignore
                        }
                    }
                    Toast.makeText(BookingDetailActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(BookingDetailActivity.this, 
                    "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
                finish();
            }
        });
    }

    private void loadFeedback() {
        btnFeedback.setEnabled(false);
        ratingBarFeedback.setVisibility(View.GONE);
        tvFeedbackComment.setVisibility(View.GONE);
        
        ApiService.api.getReviewByBooking(bookingId).enqueue(new retrofit2.Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentReview = response.body();
                    showFeedback(currentReview);
                } else if (response.code() == 404) {
                    // Chưa có feedback - đây là trường hợp bình thường
                    currentReview = null;
                    showFeedback(null);
                } else {
                    // Lỗi khác
                    currentReview = null;
                    showFeedback(null);
                    Log.e("BookingDetail", "Error loading feedback: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                currentReview = null;
                showFeedback(null);
                Log.e("BookingDetail", "Network error loading feedback", t);
            }
        });
    }

    private void showFeedback(ReviewResponse review) {
        if (review == null) {
            ratingBarFeedback.setVisibility(View.GONE);
            tvFeedbackComment.setVisibility(View.GONE);
            btnFeedback.setText("Gửi feedback");
            btnFeedback.setEnabled(true);
            btnFeedback.setOnClickListener(v -> openFeedbackActivity(false));
        } else {
            ratingBarFeedback.setVisibility(View.VISIBLE);
            ratingBarFeedback.setRating(review.getRating());
            tvFeedbackComment.setText("Nội dung: " + review.getComment());
            tvFeedbackComment.setVisibility(View.VISIBLE);
            btnFeedback.setText("Chỉnh sửa feedback");
            btnFeedback.setEnabled(true);
            btnFeedback.setOnClickListener(v -> openFeedbackActivity(true));
        }
    }

    private void openFeedbackActivity(boolean isEdit) {
        Intent intent = new Intent(this, FeedbackActivity.class);
        intent.putExtra(FeedbackActivity.EXTRA_BOOKING_ID, bookingId);
        intent.putExtra(FeedbackActivity.EXTRA_USER_ID, userId);
        intent.putExtra(FeedbackActivity.EXTRA_IS_EDIT, isEdit);
        if (isEdit && currentReview != null) {
            intent.putExtra(FeedbackActivity.EXTRA_REVIEW_ID, currentReview.getId());
            intent.putExtra(FeedbackActivity.EXTRA_RATING, currentReview.getRating());
            intent.putExtra(FeedbackActivity.EXTRA_COMMENT, currentReview.getComment());
        }
        startActivityForResult(intent, REQUEST_FEEDBACK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FEEDBACK && resultCode == RESULT_OK) {
            // Hiển thị thông báo thành công
            Toast.makeText(this, "Feedback đã được gửi thành công!", Toast.LENGTH_SHORT).show();
            // Reload feedback
            loadFeedback();
        }
    }

    private void displayBookingDetail(BookingResponse booking) {
        currentBooking = booking;
        tvServiceName.setText(booking.getServiceName());
        tvAreaSize.setText(booking.getAreaSizeName());
        tvTimeSlot.setText(booking.getTimeSlotRange());
        
        // Format booking date
        if (booking.getBookingDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            tvBookingDate.setText(dateFormat.format(booking.getBookingDate()));
        }
        
        tvAddress.setText(booking.getAddress());
        tvContactName.setText(booking.getContactName());
        tvContactPhone.setText(booking.getContactPhone());
        
        // Handle notes
        if (booking.getNotes() != null && !booking.getNotes().isEmpty()) {
            layoutNotes.setVisibility(View.VISIBLE);
            tvNotes.setText(booking.getNotes());
        } else {
            layoutNotes.setVisibility(View.GONE);
        }
        
        // Format price
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvTotalPrice.setText(currencyFormat.format(booking.getTotalPrice()));
        
        // Set status with color
        tvStatus.setText(booking.getStatus());
        setStatusColor(booking.getStatus());
        
        // Set cleaner name if available
        if (booking.getCleanerName() != null && !booking.getCleanerName().isEmpty()) {
            layoutCleaner.setVisibility(View.VISIBLE);
            tvCleanerName.setText(booking.getCleanerName());
        } else {
            layoutCleaner.setVisibility(View.GONE);
        }
        
        // Format created date
        if (booking.getCreatedAt() != null) {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            tvCreatedAt.setText(dateTimeFormat.format(booking.getCreatedAt()));
        }

        // Sau khi hiển thị booking, kiểm tra trạng thái để enable/disable feedback
        if (booking.getStatus() != null && booking.getStatus().equalsIgnoreCase("completed")) {
            btnFeedback.setVisibility(View.VISIBLE);
        } else {
            btnFeedback.setVisibility(View.GONE);
        }
    }

    private void setStatusColor(String status) {
        int colorRes;
        switch (status.toLowerCase()) {
            case "pending":
                colorRes = R.color.status_default;
                break;
            case "confirmed":
                colorRes = R.color.status_confirmed;
                break;
            case "in_progress":
                colorRes = R.color.status_in_progress;
                break;
            case "completed":
                colorRes = R.color.status_completed;
                break;
            case "cancelled":
                colorRes = R.color.status_cancelled;
                break;
            default:
                colorRes = R.color.status_default;
                break;
        }
        tvStatus.setTextColor(getResources().getColor(colorRes));
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        contentLayout.setVisibility(show ? View.GONE : View.VISIBLE);
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