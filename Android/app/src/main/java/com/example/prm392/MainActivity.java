package com.example.prm392;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.Retrofit.AccountManager;
import com.example.prm392.adapter.ServiceAdapter;
import com.example.prm392.database.AreaSizeRepository;
import com.example.prm392.database.ServiceRepository;
import com.example.prm392.database.TimeSlotRepository;
import com.example.prm392.model.AreaSizeModel;
import com.example.prm392.model.ServiceModel;
import com.example.prm392.model.TimeSlotModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        int userId = AccountManager.getUserId(this);
        String email = AccountManager.getEmail(this);
        String role = AccountManager.getRole(this);

        Log.d("USER_INFO", "ID: " + userId + ", Email: " + email + ", Role: " + role);

        // Ánh xạ RecyclerView
        recyclerServices = findViewById(R.id.recyclerServices);
        recyclerServices.setLayoutManager(new LinearLayoutManager(this));

        Spinner spinnerTimeSlot = findViewById(R.id.spinnerTimeSlot);
        Spinner spinnerAreaSize = findViewById(R.id.spinnerAreaSize); // đặt đúng id

        ArrayAdapter<TimeSlotModel> timeSlotAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, TimeSlotRepository.getAllTimeSlots()
        );
        timeSlotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeSlot.setAdapter(timeSlotAdapter);

        ArrayAdapter<AreaSizeModel> areaSizeAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, AreaSizeRepository.getAllAreaSizes()
        );
        areaSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAreaSize.setAdapter(areaSizeAdapter);

        // Lấy danh sách dịch vụ từ ServiceRepository
        List<ServiceModel> serviceList = ServiceRepository.getAllServices();

        if (serviceList != null && !serviceList.isEmpty()) {
            ServiceAdapter adapter = new ServiceAdapter(this, serviceList);
            recyclerServices.setAdapter(adapter);
        } else {
            Log.w("SERVICE_LIST", "Không có dịch vụ nào được trả về!");
        }
        Log.d("SERVICES_SIZE", "Total services: " + serviceList.size());
    }
}
