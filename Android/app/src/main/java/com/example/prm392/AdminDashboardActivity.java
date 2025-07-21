package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.example.prm392.R;

public class AdminDashboardActivity extends BaseActivity {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_admin_dashboard;
    }

    @Override
    protected void initViews() {
        int totalCustomers = 21;
        int totalCleaners = 4;
        int totalBookings = 27;
        int totalRevenue = 2988000;
        int totalBills = 0;
        int pending = 6, confirmed = 0, inProgress = 0, completed = 21, cancelled = 0;
        int pendingCleaners = 0, activeCleaners = 4;
        int recentBookings = 27, recentRevenue = 2988000;
        int revenueDNDK = 2988000, revenueDNCP = 0, revenueDPSXD = 0;

        ((TextView)findViewById(R.id.tv_total_customers)).setText(String.valueOf(totalCustomers));
        ((TextView)findViewById(R.id.tv_total_cleaners)).setText(String.valueOf(totalCleaners));
        ((TextView)findViewById(R.id.tv_total_bookings)).setText(String.valueOf(totalBookings));
        ((TextView)findViewById(R.id.tv_total_revenue)).setText(totalRevenue + " đ");
        ((TextView)findViewById(R.id.tv_total_bills)).setText(String.valueOf(totalBills));
        ((TextView)findViewById(R.id.tv_pending)).setText(String.valueOf(pending));
        ((TextView)findViewById(R.id.tv_confirmed)).setText(String.valueOf(confirmed));
        ((TextView)findViewById(R.id.tv_in_progress)).setText(String.valueOf(inProgress));
        ((TextView)findViewById(R.id.tv_completed)).setText(String.valueOf(completed));
        ((TextView)findViewById(R.id.tv_cancelled)).setText(String.valueOf(cancelled));
        ((TextView)findViewById(R.id.tv_pending_cleaners)).setText(String.valueOf(pendingCleaners));
        ((TextView)findViewById(R.id.tv_active_cleaners)).setText(String.valueOf(activeCleaners));
        ((TextView)findViewById(R.id.tv_recent_bookings)).setText(String.valueOf(recentBookings));
        ((TextView)findViewById(R.id.tv_recent_revenue)).setText(recentRevenue + " đ");
        ((TextView)findViewById(R.id.tv_revenue_dndk)).setText(revenueDNDK + " đ");
        ((TextView)findViewById(R.id.tv_revenue_dncp)).setText(revenueDNCP + " đ");
        ((TextView)findViewById(R.id.tv_revenue_dpsxd)).setText(revenueDPSXD + " đ");
    }

    @Override
    protected void setupListeners() {
        findViewById(R.id.btn_manage_news).setOnClickListener(v -> {
            Intent intent = new Intent(this, NewsManagementActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected int getDefaultSelectedTab() {
        return R.id.nav_home;
    }
} 