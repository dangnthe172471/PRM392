package com.example.prm392;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;

import com.example.prm392.Retrofit.AccountManager;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Lấy thông tin người dùng từ AccountManager
        int userId = AccountManager.getUserId(this);
        String email = AccountManager.getEmail(this);
        String role = AccountManager.getRole(this);

        TextView txtText = findViewById(R.id.testIn4);
        txtText.setText("ID: " + userId + "\nEmail: " + email + "\nRole: " + role);
    }
} 