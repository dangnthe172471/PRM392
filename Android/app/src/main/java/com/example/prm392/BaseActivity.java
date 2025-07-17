package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.Retrofit.AccountManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {
    
    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        
        // Initialize BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        if (bottomNavigationView != null) {
            setupBottomNavigation();
        }
        
        // Call child's initialization
        initViews();
        setupListeners();
    }

    /**
     * Get layout resource ID for the activity
     */
    protected abstract int getLayoutResourceId();

    /**
     * Initialize views in child activity
     */
    protected abstract void initViews();

    /**
     * Setup listeners in child activity
     */
    protected abstract void setupListeners();

    /**
     * Get the default selected tab for this activity
     */
    protected abstract int getDefaultSelectedTab();

    /**
     * Setup BottomNavigationView with common logic
     */
    private void setupBottomNavigation() {
        // Set default selected tab
        bottomNavigationView.setSelectedItemId(getDefaultSelectedTab());

        // Handle navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                if (!(this instanceof MainActivity)) {
                    startActivity(new Intent(this, MainActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                }
                return true;
            } else if (id == R.id.nav_bookings) {
                if (!(this instanceof BookingActivity)) {
                    Intent intent = new Intent(this, BookingActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
                return true;
            } else if (id == R.id.nav_chat) {
                if (!(this instanceof BlogActivity)) {
                    Intent intent = new Intent(this, BlogActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
                return true;
            } else if (id == R.id.nav_profile) {
                if (!(this instanceof LoginActivity) && !(this instanceof RegisterActivity)) {
                    int userId = AccountManager.getUserId(this);
                    if (userId != -1) {
                        startActivity(new Intent(this, ProfileActivity.class));
                    } else {
                        startActivity(new Intent(this, LoginActivity.class));
                    }
                }
                return true;
            }

            return false;
        });
    }
} 