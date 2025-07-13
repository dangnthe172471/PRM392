package com.example.prm392;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CleanerActionsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaner_actions);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        
        // Set default fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new DashboardFragment())
                .commit();

        // Setup bottom navigation listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            
            if (item.getItemId() == R.id.navigation_dashboard) {
                selectedFragment = new DashboardFragment();
            } else if (item.getItemId() == R.id.navigation_available_jobs) {
                selectedFragment = new AvailableJobsFragment();
            } else if (item.getItemId() == R.id.navigation_my_jobs) {
                selectedFragment = new MyJobsFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });
    }
} 