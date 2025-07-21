package com.example.prm392;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import com.example.prm392.Retrofit.ApiService;
import com.example.prm392.model.CreateBookingRequest;
import com.example.prm392.model.BookingResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Calendar;
import com.example.prm392.model.ServiceModel;
import com.example.prm392.model.AreaSizeModel;
import com.example.prm392.model.TimeSlotModel;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.prm392.model.UserProfile;
import android.content.Intent;
import androidx.fragment.app.FragmentManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import android.location.Location;
import android.location.Address;
import android.location.Geocoder;
import java.io.IOException;
import java.util.List;

public class BookingActivity extends BaseActivity {
    private Spinner spinnerService, spinnerAreaSize, spinnerTimeSlot;
    private EditText etBookingDate, etAddressDistrict, etAddressDetail, etContactName, etContactPhone, etNotes;
    private Button btnCreateBooking;
    private List<ServiceModel> serviceList = new ArrayList<>();
    private List<AreaSizeModel> areaSizeList = new ArrayList<>();
    private List<TimeSlotModel> timeSlotList = new ArrayList<>();
    private int[] serviceIds, areaSizeIds, timeSlotIds;
    private UserProfile currentUserProfile;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView tvTotalPrice;
    private RadioGroup rgPaymentMethod;
    private double calculatedPrice = 0;
    private List<TimeSlotModel> filteredTimeSlotList = new ArrayList<>();
    private ImageButton btnPickCurrentLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spinnerService = findViewById(R.id.spinner_service);
        spinnerAreaSize = findViewById(R.id.spinner_area_size);
        spinnerTimeSlot = findViewById(R.id.spinner_time_slot);
        etBookingDate = findViewById(R.id.et_booking_date);
        etAddressDistrict = findViewById(R.id.et_address_district);
        etAddressDetail = findViewById(R.id.et_address_detail);
        etContactName = findViewById(R.id.et_contact_name);
        etContactPhone = findViewById(R.id.et_contact_phone);
        etNotes = findViewById(R.id.et_notes);
        btnCreateBooking = findViewById(R.id.btn_create_booking);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        rgPaymentMethod = findViewById(R.id.rg_payment_method);
        btnPickCurrentLocation = findViewById(R.id.btn_pick_current_location);
        btnPickCurrentLocation.setOnClickListener(v -> etAddressDistrict.setText("Thạch Hòa, Thạch Thất, Hà Nội"));

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        loadReferenceData();

        etBookingDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(BookingActivity.this, (view, year, month, dayOfMonth) -> {
                Calendar selected = Calendar.getInstance();
                selected.set(year, month, dayOfMonth);
                if (selected.before(Calendar.getInstance())) {
                    Toast.makeText(BookingActivity.this, "Không thể chọn ngày trong quá khứ!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                etBookingDate.setText(date);
                filterTimeSlotsByDate(date);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dialog.show();
        });

        btnCreateBooking.setOnClickListener(v -> {
            String bookingDate = etBookingDate.getText().toString();
            if (bookingDate.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ngày!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (filteredTimeSlotList == null || filteredTimeSlotList.isEmpty()) {
                Toast.makeText(this, "Không có khung giờ hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }
            int timeSlotPos = spinnerTimeSlot.getSelectedItemPosition();
            if (timeSlotPos < 0 || timeSlotPos >= filteredTimeSlotList.size()) {
                Toast.makeText(this, "Vui lòng chọn khung giờ!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date selectedDate = sdf.parse(bookingDate);
                Calendar now = Calendar.getInstance();
                Calendar selected = Calendar.getInstance();
                selected.setTime(selectedDate);
                boolean isToday = now.get(Calendar.YEAR) == selected.get(Calendar.YEAR)
                        && now.get(Calendar.DAY_OF_YEAR) == selected.get(Calendar.DAY_OF_YEAR);
                int currentHour = now.get(Calendar.HOUR_OF_DAY);
                int currentMinute = now.get(Calendar.MINUTE);
                String timeRange = filteredTimeSlotList.get(timeSlotPos).getTimeRange();
                int slotHour = 0, slotMinute = 0;
                try {
                    String[] parts = timeRange.split("-");
                    if (parts.length > 0) {
                        String[] hm = parts[0].split(":");
                        slotHour = Integer.parseInt(hm[0]);
                    }
                } catch (Exception e) {
                    throw new Exception("Parse slot time error");
                }
                if (selected.before(now.getTime())) {
                    Toast.makeText(this, "Không thể đặt lịch cho ngày trong quá khứ!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isToday && (slotHour < currentHour || (slotHour == currentHour && slotMinute <= currentMinute))) {
                    Toast.makeText(this, "Không thể đặt lịch cho khung giờ đã qua!", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
                Toast.makeText(this, "Ngày hoặc khung giờ không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }
            String addressDetail = etAddressDetail.getText().toString().trim();
            if (addressDetail.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập số nhà, thôn/xóm!", Toast.LENGTH_SHORT).show();
                etAddressDetail.requestFocus();
                return;
            }
            int serviceId = serviceIds[spinnerService.getSelectedItemPosition()];
            int areaSizeId = areaSizeIds[spinnerAreaSize.getSelectedItemPosition()];
            int timeSlotId = timeSlotIds[spinnerTimeSlot.getSelectedItemPosition()];
            String addressDistrict = etAddressDistrict.getText().toString();
            addressDetail = etAddressDetail.getText().toString();
            String contactName = etContactName.getText().toString();
            String contactPhone = etContactPhone.getText().toString();
            String notes = etNotes.getText().toString();
            CreateBookingRequest request = new CreateBookingRequest(
                serviceId, areaSizeId, timeSlotId, bookingDate,
                addressDistrict, addressDetail, contactName, contactPhone, notes
            );
            ApiService.api.createBooking(request, com.example.prm392.Retrofit.AccountManager.getUserId(this)).enqueue(new Callback<BookingResponse>() {
                @Override
                public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(BookingActivity.this, "Đặt lịch thành công!", Toast.LENGTH_SHORT).show();
                        int bookingId = response.body().getId();
                        if (bookingId > 0) {
                            Intent intent = new Intent(BookingActivity.this, BookingDetailActivity.class);
                            intent.putExtra("booking_id", bookingId);
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(BookingActivity.this, BookingHistoryActivity.class));
                        }
                    } else {
                        Toast.makeText(BookingActivity.this, "Đặt lịch thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<BookingResponse> call, Throwable t) {
                    Toast.makeText(BookingActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                }
            });
        });
        autofillContactInfo();
        spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateTotalPrice();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spinnerAreaSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateTotalPrice();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        updateTotalPrice();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        etBookingDate.setText(sdf.format(new Date()));
    }

    private void loadReferenceData() {
        ApiService.api.getServices().enqueue(new retrofit2.Callback<List<ServiceModel>>() {
            @Override
            public void onResponse(Call<List<ServiceModel>> call, Response<List<ServiceModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    serviceList = response.body();
                    String[] serviceNames = new String[serviceList.size()];
                    serviceIds = new int[serviceList.size()];
                    for (int i = 0; i < serviceList.size(); i++) {
                        serviceNames[i] = serviceList.get(i).getName();
                        serviceIds[i] = serviceList.get(i).getId();
                    }
                    spinnerService.setAdapter(new ArrayAdapter<>(BookingActivity.this, android.R.layout.simple_spinner_dropdown_item, serviceNames));
                }
            }
            @Override
            public void onFailure(Call<List<ServiceModel>> call, Throwable t) {
                Toast.makeText(BookingActivity.this, "Lỗi tải dịch vụ!", Toast.LENGTH_SHORT).show();
            }
        });
        ApiService.api.getAreaSizes().enqueue(new retrofit2.Callback<List<AreaSizeModel>>() {
            @Override
            public void onResponse(Call<List<AreaSizeModel>> call, Response<List<AreaSizeModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    areaSizeList = response.body();
                    String[] areaNames = new String[areaSizeList.size()];
                    areaSizeIds = new int[areaSizeList.size()];
                    for (int i = 0; i < areaSizeList.size(); i++) {
                        areaNames[i] = areaSizeList.get(i).getName();
                        areaSizeIds[i] = areaSizeList.get(i).getId();
                    }
                    spinnerAreaSize.setAdapter(new ArrayAdapter<>(BookingActivity.this, android.R.layout.simple_spinner_dropdown_item, areaNames));
                }
            }
            @Override
            public void onFailure(Call<List<AreaSizeModel>> call, Throwable t) {
                Toast.makeText(BookingActivity.this, "Lỗi tải diện tích!", Toast.LENGTH_SHORT).show();
            }
        });
        ApiService.api.getTimeSlots().enqueue(new retrofit2.Callback<List<TimeSlotModel>>() {
            @Override
            public void onResponse(Call<List<TimeSlotModel>> call, Response<List<TimeSlotModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    timeSlotList = response.body();
                    String date = etBookingDate.getText().toString();
                    if (date.isEmpty()) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        date = sdf.format(new Date());
                    }
                    filterTimeSlotsByDate(date);
                }
            }
            @Override
            public void onFailure(Call<List<TimeSlotModel>> call, Throwable t) {
                Toast.makeText(BookingActivity.this, "Lỗi tải khung giờ!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void autofillContactInfo() {
        int userId = com.example.prm392.Retrofit.AccountManager.getUserId(this);
        ApiService.api.getUserProfile(userId).enqueue(new retrofit2.Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentUserProfile = response.body();
                    etContactName.setText(currentUserProfile.getName());
                    etContactPhone.setText(currentUserProfile.getPhone());
                }
            }
            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
            }
        });
    }

    private void updateTotalPrice() {
        if (serviceList == null || areaSizeList == null || serviceList.isEmpty() || areaSizeList.isEmpty()) {
            tvTotalPrice.setText("");
            calculatedPrice = 0;
            return;
        }
        int servicePos = spinnerService.getSelectedItemPosition();
        int areaPos = spinnerAreaSize.getSelectedItemPosition();
        if (servicePos < 0 || areaPos < 0 || servicePos >= serviceList.size() || areaPos >= areaSizeList.size()) {
            tvTotalPrice.setText("");
            calculatedPrice = 0;
            return;
        }
        double basePrice = serviceList.get(servicePos).getBasePrice();
        double multiplier = areaSizeList.get(areaPos).getMultiplier();
        calculatedPrice = basePrice * multiplier;
        tvTotalPrice.setText(String.format(Locale.getDefault(), "Tạm tính: %,d VNĐ", (int)calculatedPrice));
    }

    private void filterTimeSlotsByDate(String selectedDateStr) {
        filteredTimeSlotList.clear();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date selectedDate = sdf.parse(selectedDateStr);
            Calendar now = Calendar.getInstance();
            Calendar selected = Calendar.getInstance();
            selected.setTime(selectedDate);
            boolean isToday = now.get(Calendar.YEAR) == selected.get(Calendar.YEAR)
                    && now.get(Calendar.DAY_OF_YEAR) == selected.get(Calendar.DAY_OF_YEAR);
            int currentHour = now.get(Calendar.HOUR_OF_DAY);
            int currentMinute = now.get(Calendar.MINUTE);
            for (TimeSlotModel slot : timeSlotList) {
                String timeRange = slot.getTimeRange();
                String[] parts = timeRange.split("-");
                if (parts.length > 0) {
                    String start = parts[0];
                    int slotHour = 0, slotMinute = 0;
                    try {
                        String[] hm = start.split(":");
                        slotHour = Integer.parseInt(hm[0]);
                        slotMinute = (hm.length > 1) ? Integer.parseInt(hm[1]) : 0;
                    } catch (Exception ignored) {}
                    if (!isToday || (slotHour > currentHour || (slotHour == currentHour && slotMinute > currentMinute))) {
                        filteredTimeSlotList.add(slot);
                    }
                }
            }
        } catch (Exception e) {
            filteredTimeSlotList.addAll(timeSlotList);
        }
        String[] slotNames = new String[filteredTimeSlotList.size()];
        timeSlotIds = new int[filteredTimeSlotList.size()];
        for (int i = 0; i < filteredTimeSlotList.size(); i++) {
            slotNames[i] = filteredTimeSlotList.get(i).getTimeRange();
            timeSlotIds[i] = filteredTimeSlotList.get(i).getId();
        }
        spinnerTimeSlot.setAdapter(new ArrayAdapter<>(BookingActivity.this, android.R.layout.simple_spinner_dropdown_item, slotNames));
        if (filteredTimeSlotList.isEmpty()) {
            Toast.makeText(this, "Không còn khung giờ nào hợp lệ cho ngày này!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_booking;
    }
    @Override
    protected void initViews() {}
    @Override
    protected void setupListeners() {}
    @Override
    protected int getDefaultSelectedTab() {
        return R.id.nav_bookings;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
} 