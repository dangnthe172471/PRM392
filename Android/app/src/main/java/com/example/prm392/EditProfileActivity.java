package com.example.prm392;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.Retrofit.ApiService;
import com.example.prm392.model.UserProfile;
import com.example.prm392.model.UserProfileUpdateRequest;
import com.example.prm392.Retrofit.AccountManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    private EditText edtName, edtPhone, edtAddress;
    private Button btnSave;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        edtName = findViewById(R.id.edtFullName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtEmail);
        btnSave = findViewById(R.id.btnSave);

        // Lấy userId từ AccountManager
        userId = AccountManager.getUserId(this);

        // Nhận dữ liệu profile hiện tại từ Intent (nếu có)
        String name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("phone");
        String address = getIntent().getStringExtra("address");

        edtName.setText(name != null ? name : "");
        edtPhone.setText(phone != null ? phone : "");
        edtAddress.setText(address != null ? address : "");

        btnSave.setOnClickListener(v -> updateProfile());
    }

    private void updateProfile() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        UserProfileUpdateRequest request = new UserProfileUpdateRequest(name, phone, address);

        ApiService apiService = ApiService.api;

        Call<com.example.prm392.model.UserProfile> call = apiService.updateUserProfile(request, userId);
        call.enqueue(new retrofit2.Callback<com.example.prm392.model.UserProfile>() {
            @Override
            public void onResponse(Call<com.example.prm392.model.UserProfile> call, retrofit2.Response<com.example.prm392.model.UserProfile> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.example.prm392.model.UserProfile> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 