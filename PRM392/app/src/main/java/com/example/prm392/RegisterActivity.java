package com.example.prm392;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegisterActivity extends AppCompatActivity {
    EditText edtName, edtEmail, edtPassword, edtConfirmPassword, edtPhone, edtAddress;
    Button btnRegister;
    TextView tvLogin;
    ProgressBar progressBar;
    ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        progressBar = findViewById(R.id.progressBar);
        imgLogo = findViewById(R.id.imgLogo);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                registerUser();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void registerUser() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải từ 6 ký tự trở lên", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.PHONE.matcher(phone).matches() || phone.length() < 9) {
            Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        btnRegister.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection con = new ConSQL().conclass();
                    if (con == null) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            btnRegister.setEnabled(true);
                            Toast.makeText(RegisterActivity.this, "Không thể kết nối server", Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }
                    // Kiểm tra email đã tồn tại chưa
                    String checkSql = "SELECT Id FROM Users WHERE Email = ?";
                    PreparedStatement checkPs = con.prepareStatement(checkSql);
                    checkPs.setString(1, email);
                    ResultSet rs = checkPs.executeQuery();
                    if (rs.next()) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            btnRegister.setEnabled(true);
                            Toast.makeText(RegisterActivity.this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                        });
                        rs.close();
                        checkPs.close();
                        con.close();
                        return;
                    }
                    rs.close();
                    checkPs.close();
                    // Thêm user mới
                    String sql = "INSERT INTO Users (Name, Email, Password, Phone, Address, Role, Status) VALUES (?, ?, ?, ?, ?, 'user', 'active')";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, name);
                    ps.setString(2, email);
                    ps.setString(3, password);
                    ps.setString(4, phone);
                    ps.setString(5, address);
                    int rows = ps.executeUpdate();
                    ps.close();
                    con.close();
                    if (rows > 0) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            btnRegister.setEnabled(true);
                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        });
                    } else {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            btnRegister.setEnabled(true);
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnRegister.setEnabled(true);
                        Toast.makeText(RegisterActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
            }
        }).start();
    }
} 