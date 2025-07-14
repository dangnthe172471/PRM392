package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.Retrofit.AccountManager;
import com.example.prm392.adapter.ServiceAdapter;
import com.example.prm392.database.ServiceRepository;
import com.example.prm392.model.ServiceModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private RecyclerView recyclerServices;
    private ServiceAdapter serviceAdapter;
    private EditText etSearch;
    private List<ServiceModel> fullServiceList;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        // Ánh xạ View
        recyclerServices = findViewById(R.id.recyclerServices);
        etSearch = findViewById(R.id.etSearch);

        recyclerServices.setLayoutManager(new LinearLayoutManager(this));

        // Load dữ liệu dịch vụ
        loadServiceData();
    }

    @Override
    protected void setupListeners() {
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
    }

    @Override
    protected int getDefaultSelectedTab() {
        return R.id.nav_home;
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
