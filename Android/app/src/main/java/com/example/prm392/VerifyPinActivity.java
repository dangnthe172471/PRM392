package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.Retrofit.ApiService;
import com.example.prm392.model.ApiResponse;
import com.example.prm392.model.VerifyPinRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyPinActivity extends AppCompatActivity {
    EditText edtPin;
    Button btnVerifyPin;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_pin);

        edtPin = findViewById(R.id.edtPin);
        btnVerifyPin = findViewById(R.id.btnVerifyPin);
        email = getIntent().getStringExtra("email");

        btnVerifyPin.setOnClickListener(v -> {
            String pin = edtPin.getText().toString().trim();
            if (pin.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã PIN", Toast.LENGTH_SHORT).show();
                return;
            }
            ApiService.api.verifyPin(new VerifyPinRequest(email, pin))
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(VerifyPinActivity.this, "Mã PIN hợp lệ!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(VerifyPinActivity.this, ResetPasswordActivity.class);
                            intent.putExtra("email", email);
                            intent.putExtra("pin", pin);
                            startActivity(intent);
                        } else {
                            Toast.makeText(VerifyPinActivity.this, "Mã PIN không hợp lệ hoặc đã hết hạn!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(VerifyPinActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                    }
                });
        });
    }
} 