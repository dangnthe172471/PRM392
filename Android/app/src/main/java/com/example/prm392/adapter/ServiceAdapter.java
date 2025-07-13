package com.example.prm392.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.model.ServiceModel;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private final Context context;
    private final List<ServiceModel> serviceList;

    public ServiceAdapter(Context context, List<ServiceModel> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service_card, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceModel service = serviceList.get(position);

        holder.tvWorkerName.setText(service.getName());
        holder.tvServiceType.setText(service.getDescription());
        holder.tvPrice.setText("$" + service.getBasePrice() + "/day");


        holder.imgWorker.setImageResource(R.drawable.ic_worker);
        holder.tvRating.setText("⭐ 4.8");
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public void updateData(List<ServiceModel> newList) {
        serviceList.clear();
        serviceList.addAll(newList);
        notifyDataSetChanged();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView tvWorkerName, tvServiceType, tvPrice, tvRating;
        ImageView imgWorker;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWorkerName = itemView.findViewById(R.id.tvWorkerName);
            tvServiceType = itemView.findViewById(R.id.tvServiceType);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
            imgWorker = itemView.findViewById(R.id.imgWorker);
        }
    }
}
