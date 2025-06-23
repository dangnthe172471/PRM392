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

public class LoginActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView tvRegister;
    ProgressBar progressBar;
    ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        progressBar = findViewById(R.id.progressBar);
        imgLogo = findViewById(R.id.imgLogo);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                loginUser();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
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

    private void loginUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        btnLogin.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection con = new ConSQL().conclass();
                    if (con == null) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            btnLogin.setEnabled(true);
                            Toast.makeText(LoginActivity.this, "Không thể kết nối server", Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }
                    String sql = "SELECT * FROM Users WHERE Email = ? AND Password = ? AND Status = 'active'";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, email);
                    ps.setString(2, password);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            btnLogin.setEnabled(true);
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    } else {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            btnLogin.setEnabled(true);
                            Toast.makeText(LoginActivity.this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                        });
                    }
                    rs.close();
                    ps.close();
                    con.close();
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnLogin.setEnabled(true);
                        Toast.makeText(LoginActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
            }
        }).start();
    }
} 