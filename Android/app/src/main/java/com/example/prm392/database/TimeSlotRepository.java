package com.example.prm392.database;

import android.util.Log;
import com.example.prm392.model.TimeSlotModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotRepository {
    public static List<TimeSlotModel> getAllTimeSlots() {
        List<TimeSlotModel> list = new ArrayList<>();
        Connection conn = DBHelper.getConnection();

        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM TimeSlots WHERE IsActive = 1");

                while (rs.next()) {
                    TimeSlotModel slot = new TimeSlotModel();
                    slot.setId(rs.getInt("Id"));
                    slot.setTimeRange(rs.getString("TimeRange"));
                    list.add(slot);
                }

                rs.close();
                conn.close();
                Log.d("TIMESLOT_DB", "Loaded: " + list.size());
            } catch (Exception e) {
                Log.e("TIMESLOT_DB", "Error: " + e.getMessage());
            }
        }
        return list;
    }
}
