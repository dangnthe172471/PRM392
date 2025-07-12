package com.example.prm392;

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
import com.example.prm392.model.UpdateJobStatusRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class MyJobsFragment extends Fragment implements JobAdapter.OnJobClickListener {
    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private JobAdapter jobAdapter;
    private List<Job> jobList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_jobs, container, false);
        recyclerView = view.findViewById(R.id.recyclerMyJobs);
        emptyView = view.findViewById(R.id.emptyViewMyJobs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        jobAdapter = new JobAdapter(jobList, this);
        recyclerView.setAdapter(jobAdapter);
        loadJobs();
        return view;
    }

    private void loadJobs() {
        int cleanerId = AccountManager.getUserId(requireContext());
        ApiService.api.getMyJobs(cleanerId, "all").enqueue(new Callback<List<Job>>() {
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
        int cleanerId = AccountManager.getUserId(requireContext());
        String nextStatus = null;
        if ("confirmed".equalsIgnoreCase(job.getStatus())) {
            nextStatus = "in_progress";
        } else if ("in_progress".equalsIgnoreCase(job.getStatus())) {
            nextStatus = "completed";
        } else {
            Toast.makeText(getContext(), "Không thể cập nhật trạng thái cho công việc này!", Toast.LENGTH_SHORT).show();
            return;
        }
        UpdateJobStatusRequest request = new UpdateJobStatusRequest(nextStatus);
        ApiService.api.updateJobStatus(job.getId(), cleanerId, request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Cập nhật trạng thái thành công!", Toast.LENGTH_SHORT).show();
                    loadJobs();
                } else {
                    Toast.makeText(getContext(), "Không thể cập nhật trạng thái!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối khi cập nhật trạng thái!", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 