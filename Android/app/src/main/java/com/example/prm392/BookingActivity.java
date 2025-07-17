package com.example.prm392;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.Nullable;
import com.example.prm392.Retrofit.ApiService;
import com.example.prm392.model.CreateBookingRequest;
import com.example.prm392.model.BookingResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Calendar;

public class BookingActivity extends BaseActivity {
    private Spinner spinnerService, spinnerAreaSize, spinnerTimeSlot;
    private EditText etBookingDate, etAddressDistrict, etAddressDetail, etContactName, etContactPhone, etNotes;
    private Button btnCreateBooking;

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

        // TODO: Lấy dữ liệu thực tế từ API, tạm hardcode cho demo
        String[] services = {"Dịch vụ 1", "Dịch vụ 2"};
        int[] serviceIds = {1, 2};
        String[] areaSizes = {"<50m2", "50-100m2"};
        int[] areaSizeIds = {1, 2};
        String[] timeSlots = {"08:00-10:00", "10:00-12:00"};
        int[] timeSlotIds = {1, 2};

        spinnerService.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, services));
        spinnerAreaSize.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, areaSizes));
        spinnerTimeSlot.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, timeSlots));

        etBookingDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(BookingActivity.this, (view, year, month, dayOfMonth) -> {
                String date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                etBookingDate.setText(date);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });

        btnCreateBooking.setOnClickListener(v -> {
            int serviceId = serviceIds[spinnerService.getSelectedItemPosition()];
            int areaSizeId = areaSizeIds[spinnerAreaSize.getSelectedItemPosition()];
            int timeSlotId = timeSlotIds[spinnerTimeSlot.getSelectedItemPosition()];
            String bookingDate = etBookingDate.getText().toString();
            String addressDistrict = etAddressDistrict.getText().toString();
            String addressDetail = etAddressDetail.getText().toString();
            String contactName = etContactName.getText().toString();
            String contactPhone = etContactPhone.getText().toString();
            String notes = etNotes.getText().toString();

            CreateBookingRequest request = new CreateBookingRequest(
                serviceId, areaSizeId, timeSlotId, bookingDate,
                addressDistrict, addressDetail, contactName, contactPhone, notes
            );

            ApiService.api.createBooking(request, 1).enqueue(new Callback<BookingResponse>() {
                @Override
                public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(BookingActivity.this, "Đặt lịch thành công!", Toast.LENGTH_SHORT).show();
                        finish();
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
} 