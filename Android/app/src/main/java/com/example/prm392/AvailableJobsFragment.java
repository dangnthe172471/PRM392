package com.example.prm392;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.LinearLayout;

import com.example.prm392.Retrofit.AccountManager;
import com.example.prm392.Retrofit.ApiService;
import com.example.prm392.model.Job;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class AvailableJobsFragment extends Fragment implements JobAdapter.OnJobClickListener {
    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private JobAdapter jobAdapter;
    private List<Job> jobList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_available_jobs, container, false);
        recyclerView = view.findViewById(R.id.recyclerAvailableJobs);
        emptyView = view.findViewById(R.id.emptyViewAvailableJobs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        jobAdapter = new JobAdapter(jobList, this);
        recyclerView.setAdapter(jobAdapter);
        loadJobs();
        return view;
    }

    private void loadJobs() {
        ApiService.api.getAvailableJobs().enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                jobList.clear();
                if (response.isSuccessful() && response.body() != null) {
                    jobList.addAll(response.body());
                }
                updateUI();
            }
            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                updateUI();
            }
        });
    }

    private void updateUI() {
        if (jobList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
        jobAdapter.notifyDataSetChanged();
    }

    @Override
    public void onJobClick(Job job) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận nhận việc")
                .setMessage("Bạn có chắc chắn muốn nhận công việc này không?")
                .setPositiveButton("Xác nhận", (dialog, which) -> {
                    // Gọi API nhận việc như cũ
                    int cleanerId = AccountManager.getUserId(requireContext());
                    ApiService.api.acceptJob(job.getId(), cleanerId).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Nhận việc thành công!", Toast.LENGTH_SHORT).show();
                                loadJobs(); // Refresh lại danh sách
                            } else {
                                Toast.makeText(getContext(), "Không thể nhận việc này!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(getContext(), "Lỗi kết nối khi nhận việc!", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .show();
    }
} 