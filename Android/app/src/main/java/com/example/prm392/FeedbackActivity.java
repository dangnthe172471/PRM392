package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.prm392.Retrofit.ApiService;
import com.example.prm392.model.CreateReviewRequest;
import com.example.prm392.model.ReviewResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {
    public static final String EXTRA_BOOKING_ID = "booking_id";
    public static final String EXTRA_USER_ID = "user_id";
    public static final String EXTRA_IS_EDIT = "is_edit";
    public static final String EXTRA_REVIEW_ID = "review_id";
    public static final String EXTRA_RATING = "rating";
    public static final String EXTRA_COMMENT = "comment";

    private int bookingId, userId;
    private boolean isEdit = false;
    private RatingBar ratingBar;
    private EditText edtComment;
    private Button btnSubmit;
    private TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Feedback booking");
        }

        tvTitle = findViewById(R.id.tvTitle);
        ratingBar = findViewById(R.id.ratingBar);
        edtComment = findViewById(R.id.edtComment);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Nhận dữ liệu từ intent
        bookingId = getIntent().getIntExtra(EXTRA_BOOKING_ID, -1);
        userId = getIntent().getIntExtra(EXTRA_USER_ID, -1);
        isEdit = getIntent().getBooleanExtra(EXTRA_IS_EDIT, false);
        int rating = getIntent().getIntExtra(EXTRA_RATING, 0);
        String comment = getIntent().getStringExtra(EXTRA_COMMENT);

        if (isEdit) {
            tvTitle.setText("Chỉnh sửa feedback");
            btnSubmit.setText("Cập nhật");
            ratingBar.setRating(rating);
            edtComment.setText(comment);
        } else {
            tvTitle.setText("Gửi feedback");
            btnSubmit.setText("Gửi");
        }

        btnSubmit.setOnClickListener(v -> submitFeedback());
    }

    private void submitFeedback() {
        int rating = (int) ratingBar.getRating();
        String comment = edtComment.getText().toString().trim();

        if (rating == 0 || TextUtils.isEmpty(comment)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ đánh giá và nội dung", Toast.LENGTH_SHORT).show();
            return;
        }

        CreateReviewRequest req = new CreateReviewRequest(bookingId, rating, comment);
        btnSubmit.setEnabled(false);

        Call<ReviewResponse> call = isEdit ? 
            ApiService.api.updateReview(bookingId, req, userId) :
            ApiService.api.createReview(req, userId);

        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                btnSubmit.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    setResultOk(response.body());
                    finish();
                } else {
                    String errorMsg = isEdit ? "Lỗi cập nhật feedback" : "Lỗi gửi feedback";
                    if (response.errorBody() != null) {
                        try { errorMsg += ": " + response.errorBody().string(); } catch (Exception e) {}
                    }
                    Toast.makeText(FeedbackActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                btnSubmit.setEnabled(true);
                Toast.makeText(FeedbackActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setResultOk(ReviewResponse review) {
        Intent data = new Intent();
        data.putExtra(EXTRA_REVIEW_ID, review.getId());
        data.putExtra(EXTRA_RATING, review.getRating());
        data.putExtra(EXTRA_COMMENT, review.getComment());
        setResult(RESULT_OK, data);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 