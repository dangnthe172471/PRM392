package com.example.prm392.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.model.BookingResponse;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.BookingViewHolder> {
    private List<BookingResponse> bookingList;
    private OnBookingClickListener listener;

    public interface OnBookingClickListener {
        void onBookingClick(BookingResponse booking);
    }

    public BookingHistoryAdapter(List<BookingResponse> bookingList, OnBookingClickListener listener) {
        this.bookingList = bookingList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking_history, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingResponse booking = bookingList.get(position);
        holder.bind(booking);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class BookingViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView tvServiceName;
        private TextView tvBookingDate;
        private TextView tvTimeSlot;
        private TextView tvAddress;
        private TextView tvTotalPrice;
        private TextView tvStatus;
        private TextView tvCleanerName;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvTimeSlot = itemView.findViewById(R.id.tvTimeSlot);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvCleanerName = itemView.findViewById(R.id.tvCleanerName);

            cardView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onBookingClick(bookingList.get(position));
                }
            });
        }

        public void bind(BookingResponse booking) {
            tvServiceName.setText(booking.getServiceName());
            
            // Format booking date
            if (booking.getBookingDate() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                tvBookingDate.setText("Ngày: " + dateFormat.format(booking.getBookingDate()));
            }
            
            tvTimeSlot.setText("Giờ: " + booking.getTimeSlotRange());
            tvAddress.setText("Địa chỉ: " + booking.getAddress());
            
            // Format price
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvTotalPrice.setText("Tổng tiền: " + currencyFormat.format(booking.getTotalPrice()));
            
            // Set status with color
            tvStatus.setText(booking.getStatus());
            setStatusColor(booking.getStatus());
            
            // Set cleaner name if available
            if (booking.getCleanerName() != null && !booking.getCleanerName().isEmpty()) {
                tvCleanerName.setVisibility(View.VISIBLE);
                tvCleanerName.setText("Nhân viên: " + booking.getCleanerName());
            } else {
                tvCleanerName.setVisibility(View.GONE);
            }
        }

        private void setStatusColor(String status) {
            int colorRes;
            switch (status.toLowerCase()) {
                case "pending":
                    colorRes = R.color.status_pending;
                    break;
                case "confirmed":
                    colorRes = R.color.status_confirmed;
                    break;
                case "in_progress":
                    colorRes = R.color.status_in_progress;
                    break;
                case "completed":
                    colorRes = R.color.status_completed;
                    break;
                case "cancelled":
                    colorRes = R.color.status_cancelled;
                    break;
                default:
                    colorRes = R.color.status_default;
                    break;
            }
            tvStatus.setTextColor(itemView.getContext().getResources().getColor(colorRes));
        }
    }
} 