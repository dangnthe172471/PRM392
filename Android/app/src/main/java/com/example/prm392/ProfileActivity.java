package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.prm392.model.UserProfile;
import com.example.prm392.Retrofit.ApiService;
import com.example.prm392.Retrofit.AccountManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
    private TextView tvName, tvRole, tvEmail, tvPhone, tvAddress, tvStatus, tvExperience, tvCreatedAt;
    private LinearLayout layoutExperience;
    private Button btnEditProfile, btnBookingHistory, btnLogout;
    private ApiService apiService;
    private UserProfile currentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        initViews();
        
        // Initialize API service
        apiService = ApiService.api;
        
        // Load user profile
        loadUserProfile();
        
        // Set up click listeners
        setupClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile();
    }

    private void initViews() {
        tvName = findViewById(R.id.tvName);
        tvRole = findViewById(R.id.tvRole);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        tvStatus = findViewById(R.id.tvStatus);
        tvExperience = findViewById(R.id.tvExperience);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        layoutExperience = findViewById(R.id.layoutExperience);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnBookingHistory = findViewById(R.id.btnBookingHistory);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void loadUserProfile() {
        int userId = AccountManager.getUserId(this);
        
        if (userId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            logout();
            return;
        }

        apiService.getUserProfile(userId).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfile profile = response.body();
                    displayUserProfile(profile);
                } else {
                    Toast.makeText(ProfileActivity.this, "Không thể tải thông tin profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayUserProfile(UserProfile profile) {
        currentProfile = profile;
        // Set basic information
        tvName.setText(profile.getName());
        tvEmail.setText(profile.getEmail());
        tvPhone.setText(profile.getPhone());
        tvAddress.setText(profile.getAddress());
        
        // Set role with proper formatting
        String roleText = getRoleDisplayText(profile.getRole());
        tvRole.setText(roleText);
        
        // Set status
        String status = profile.getStatus();
        if (status != null && !status.isEmpty()) {
            tvStatus.setText(status);
        } else {
            tvStatus.setText("Hoạt động");
        }
        
        // Set experience (only for Cleaner role)
        if ("Cleaner".equals(profile.getRole()) && profile.getExperience() != null) {
            layoutExperience.setVisibility(View.VISIBLE);
            tvExperience.setText(profile.getExperience());
        } else {
            layoutExperience.setVisibility(View.GONE);
        }
        
        // Set created date
        if (profile.getCreatedAt() != null && !profile.getCreatedAt().isEmpty()) {
            try {
                // Parse the date string and format it
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                Date date = inputFormat.parse(profile.getCreatedAt());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                tvCreatedAt.setText(outputFormat.format(date));
            } catch (Exception e) {
                tvCreatedAt.setText(profile.getCreatedAt());
            }
        } else {
            tvCreatedAt.setText("N/A");
        }
    }

    private String getRoleDisplayText(String role) {
        switch (role) {
            case "user":
                return "Khách hàng";
            case "cleaner":
                return "Người dọn dẹp";
            case "admin":
                return "Quản trị viên";
            default:
                return role;
        }
    }

    private void setupClickListeners() {
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentProfile != null) {
                    Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                    intent.putExtra("name", currentProfile.getName());
                    intent.putExtra("phone", currentProfile.getPhone());
                    intent.putExtra("address", currentProfile.getAddress());
                    startActivity(intent);
                } else {
                    Toast.makeText(ProfileActivity.this, "Chưa có thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBookingHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, BookingHistoryActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {
        // Clear saved account data
        AccountManager.logout(ProfileActivity.this);
        
        // Show logout message
        Toast.makeText(ProfileActivity.this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
        
        // Navigate back to login screen
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
} 