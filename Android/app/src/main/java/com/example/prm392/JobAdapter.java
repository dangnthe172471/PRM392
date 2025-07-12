package com.example.prm392;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392.model.Job;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {
    private List<Job> jobList;
    private OnJobClickListener listener;

    public interface OnJobClickListener {
        void onJobClick(Job job);
    }

    public JobAdapter(List<Job> jobList, OnJobClickListener listener) {
        this.jobList = jobList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.tvJobTitle.setText(job.getServiceName());
        holder.tvJobAddress.setText(job.getAddress());
        holder.tvJobTime.setText(job.getTimeSlot() + " | " + job.getBookingDate());
        holder.tvJobPrice.setText("Giá: " + String.format("%,.0f", job.getTotalPrice()) + "đ");
        holder.tvJobStatus.setText("Trạng thái: " + job.getStatus());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onJobClick(job);
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvJobAddress, tvJobTime, tvJobPrice, tvJobStatus;
        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvJobAddress = itemView.findViewById(R.id.tvJobAddress);
            tvJobTime = itemView.findViewById(R.id.tvJobTime);
            tvJobPrice = itemView.findViewById(R.id.tvJobPrice);
            tvJobStatus = itemView.findViewById(R.id.tvJobStatus);
        }
    }
} 