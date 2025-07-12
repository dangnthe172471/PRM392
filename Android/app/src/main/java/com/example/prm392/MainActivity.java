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

import com.example.prm392.adapter.ServiceAdapter;
import com.example.prm392.database.ServiceRepository;
import com.example.prm392.model.ServiceModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerServices;
    private ServiceAdapter serviceAdapter;
    private EditText etSearch;
    private BottomNavigationView bottomNavigationView;
    private List<ServiceModel> fullServiceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
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
                // startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
            }

            return false;
        });


        // Set mặc định tab Home được chọn
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
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
