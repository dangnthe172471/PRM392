package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.Retrofit.ApiService;
import com.example.prm392.model.ApiResponse;
import com.example.prm392.model.ForgotPasswordRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText edtEmail;
    Button btnSendPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edtEmail = findViewById(R.id.edtEmail);
        btnSendPin = findViewById(R.id.btnSendPin);

        btnSendPin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                return;
            }
            btnSendPin.setEnabled(false); // Disable nút gửi khi đang gửi request
            ApiService.api.forgotPassword(new ForgotPasswordRequest(email))
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        btnSendPin.setEnabled(true); // Enable lại khi có kết quả
                        if (response.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Mã PIN đã gửi về email!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgotPasswordActivity.this, VerifyPinActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "Email không tồn tại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        btnSendPin.setEnabled(true); // Enable lại khi có lỗi
                        Toast.makeText(ForgotPasswordActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                    }
                });
        });
    }
} 