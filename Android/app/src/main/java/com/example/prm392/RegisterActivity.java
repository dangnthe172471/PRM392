package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.prm392.model.RegisterRequest;
import com.example.prm392.model.LoginResponse;
import com.example.prm392.Retrofit.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

public class RegisterActivity extends BaseActivity {
    private EditText edtEmail, edtPassword, edtName, edtPhone, edtAddress;
    private Button btnRegister;
    private TextView tvLogin;
    private ApiService apiService;
    private Spinner spinnerRole;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initViews() {
        // Initialize views
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        spinnerRole = findViewById(R.id.spinnerRole);

        apiService = ApiService.api;

        // Setup spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, 
            new String[]{"Người dùng", "Người dọn vệ sinh"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);
    }

    @Override
    protected void setupListeners() {
        // Register button click
        btnRegister.setOnClickListener(v -> performRegister());

        // Login link click
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected int getDefaultSelectedTab() {
        return R.id.nav_profile;
    }

    private void performRegister() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String role = spinnerRole.getSelectedItemPosition() == 0 ? "user" : "cleaner";

        // Validation
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Vui lòng nhập email");
            edtEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không hợp lệ");
            edtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Vui lòng nhập mật khẩu");
            edtPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            edtPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            edtPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(name)) {
            edtName.setError("Vui lòng nhập họ tên");
            edtName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            edtPhone.setError("Vui lòng nhập số điện thoại");
            edtPhone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(address)) {
            edtAddress.setError("Vui lòng nhập địa chỉ");
            edtAddress.requestFocus();
            return;
        }

        // Show loading state
        setButtonLoading(true);

        // API call
        RegisterRequest request = new RegisterRequest(email, password, name, phone, address, role);
        apiService.register(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                setButtonLoading(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    String errorMsg = "Đăng ký thất bại!";
                    if (response.errorBody() != null) {
                        try {
                            errorMsg += ": " + response.errorBody().string();
                        } catch (Exception e) {
                            // ignore
                        }
                    }
                    Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }
            
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                setButtonLoading(false);
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setButtonLoading(boolean isLoading) {
        btnRegister.setEnabled(!isLoading);
        if (isLoading) {
            btnRegister.setText("Đang đăng ký...");
            btnRegister.setBackgroundResource(R.drawable.button_primary_disabled);
            btnRegister.setTextColor(getResources().getColor(R.color.status_default));
        } else {
            btnRegister.setText("Đăng ký");
            btnRegister.setBackgroundResource(R.drawable.button_gradient);
            btnRegister.setTextColor(getResources().getColor(R.color.white));
        }
    }
} 