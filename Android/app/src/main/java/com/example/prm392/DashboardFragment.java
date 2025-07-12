package com.example.prm392;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.prm392.Retrofit.AccountManager;
import com.example.prm392.Retrofit.ApiService;
import com.example.prm392.model.CleanerDashboardStats;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {
    private TextView tvWelcome, tvAvailableJobs, tvMyJobs, tvCompletedJobs, tvTotalEarnings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        tvWelcome = view.findViewById(R.id.tvWelcome);
        tvAvailableJobs = view.findViewById(R.id.tvAvailableJobs);
        tvMyJobs = view.findViewById(R.id.tvMyJobs);
        tvCompletedJobs = view.findViewById(R.id.tvCompletedJobs);
        tvTotalEarnings = view.findViewById(R.id.tvTotalEarnings);
        setWelcomeText();
        loadDashboardStats();
        return view;
    }

    private void setWelcomeText() {
        String email = AccountManager.getEmail(requireContext());
        if (email == null || email.isEmpty()) {
            email = "Cleaner";
        } else {
            // Extract name from email (part before @)
            String[] parts = email.split("@");
            email = parts[0];
        }
        tvWelcome.setText("Xin chào, " + email + "!");
    }

    private void loadDashboardStats() {
        int cleanerId = AccountManager.getUserId(requireContext());
        ApiService.api.getCleanerDashboardStats(cleanerId).enqueue(new Callback<CleanerDashboardStats>() {
            @Override
            public void onResponse(Call<CleanerDashboardStats> call, Response<CleanerDashboardStats> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CleanerDashboardStats stats = response.body();
                    tvAvailableJobs.setText(String.valueOf(stats.getAvailableJobs()));
                    tvMyJobs.setText(String.valueOf(stats.getMyJobs()));
                    tvCompletedJobs.setText(String.valueOf(stats.getCompletedJobs()));
                    tvTotalEarnings.setText(String.format("%,.0fđ", stats.getTotalEarnings()));
                } else {
                    Toast.makeText(getContext(), "Không thể tải dữ liệu dashboard!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CleanerDashboardStats> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối khi tải dashboard!", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 