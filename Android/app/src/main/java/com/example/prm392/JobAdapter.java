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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking_history, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.bind(job);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onJobClick(job);
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView tvServiceName, tvBookingDate, tvTimeSlot, tvAddress, tvTotalPrice, tvStatus, tvCleanerName;
        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvTimeSlot = itemView.findViewById(R.id.tvTimeSlot);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvCleanerName = itemView.findViewById(R.id.tvCleanerName);
        }
        public void bind(Job job) {
            tvServiceName.setText(job.getServiceName());
            tvBookingDate.setText("Ngày: " + (job.getBookingDate() != null ? job.getBookingDate() : ""));
            tvTimeSlot.setText("Giờ: " + (job.getTimeSlot() != null ? job.getTimeSlot() : ""));
            tvAddress.setText("Địa chỉ: " + (job.getAddress() != null ? job.getAddress() : ""));
            tvTotalPrice.setText("Tổng tiền: " + String.format("%,.0f", job.getTotalPrice()) + " đ");
            tvStatus.setText(job.getStatus());
            if (job.getCleanerName() != null && !job.getCleanerName().isEmpty()) {
                tvCleanerName.setVisibility(View.VISIBLE);
                tvCleanerName.setText("Nhân viên: " + job.getCleanerName());
            } else {
                tvCleanerName.setVisibility(View.GONE);
            }
        }
    }
} 