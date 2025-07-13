package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.Retrofit.AccountManager;
import com.example.prm392.adapter.ServiceAdapter;
import com.example.prm392.database.ServiceRepository;
import com.example.prm392.model.ServiceModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerServices;
    private ServiceAdapter serviceAdapter;
    private EditText etSearch;
    private BottomNavigationView bottomNavigationView;
    private List<ServiceModel> fullServiceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ View
        recyclerServices = findViewById(R.id.recyclerServices);
        etSearch = findViewById(R.id.etSearch);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        recyclerServices.setLayoutManager(new LinearLayoutManager(this));

        // Load dữ liệu dịch vụ
        loadServiceData();

        // Tìm kiếm realtime theo tên tiêu đề
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchServicesByName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // Xử lý click bottom navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Đã ở home rồi
                return true;
            } else if (id == R.id.nav_bookings) {
                startActivity(new Intent(MainActivity.this, BlogActivity.class));
                overridePendingTransition(0,0);
                finish(); // Đóng MainActivity để tránh stack quá nhiều
                return true;
            } else if (id == R.id.nav_chat) {
                // startActivity(new Intent(MainActivity.this, ChatActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                int userId = AccountManager.getUserId(this);
                if(userId != -1) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                return true;
            }

            return false;
        });


        // Hao Ngu
        // Set mặc định tab Home được chọn
//        bottomNavigationView.setSelectedItemId(R.id.nav_home);
//
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment selectedFragment = null;
//                int itemId = item.getItemId();
//                if (itemId == R.id.navigation_dashboard) {
//                    selectedFragment = new DashboardFragment();
//                } else if (itemId == R.id.navigation_available_jobs) {
//                    selectedFragment = new AvailableJobsFragment();
//                } else if (itemId == R.id.navigation_my_jobs) {
//                    selectedFragment = new MyJobsFragment();
//                } else if (itemId == R.id.navigation_profile) {
//                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
//                    return false;
//                } else {
//                    selectedFragment = new DashboardFragment(); // fallback
//                }
//                if (selectedFragment != null) {
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.fragment_container, selectedFragment)
//                            .commit();
//                }
//                return true;
//            }
//        });
//        // Mặc định mở tab Dashboard
//        navView.setSelectedItemId(R.id.navigation_dashboard);
    }

    private void loadServiceData() {
        fullServiceList = ServiceRepository.getAllServices();
        serviceAdapter = new ServiceAdapter(this, fullServiceList);
        recyclerServices.setAdapter(serviceAdapter);
    }

    private void searchServicesByName(String query) {
        if (query.isEmpty()) {
            // Nếu search query rỗng, hiển thị tất cả services
            serviceAdapter.updateData(fullServiceList);
        } else {
            // Tìm kiếm từ database
            List<ServiceModel> searchResults = ServiceRepository.searchServicesByName(query);
            serviceAdapter.updateData(searchResults);
        }
    }
}
