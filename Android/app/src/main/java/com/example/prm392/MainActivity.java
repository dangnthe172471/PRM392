package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.navigation_available_jobs) {
                    selectedFragment = new AvailableJobsFragment();
                } else if (itemId == R.id.navigation_my_jobs) {
                    selectedFragment = new MyJobsFragment();
                } else if (itemId == R.id.navigation_profile) {
                    // Mở ProfileActivity thay vì fragment
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    return false; // Không thay fragment
                } else {
                    selectedFragment = new AvailableJobsFragment(); // fallback
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }
                return true;
            }

        });
        // Mặc định mở tab 1
        navView.setSelectedItemId(R.id.navigation_available_jobs);
    }
} 