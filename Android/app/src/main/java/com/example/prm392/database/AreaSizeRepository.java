package com.example.prm392.database;

import android.util.Log;
import com.example.prm392.model.AreaSizeModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AreaSizeRepository {
    public static List<AreaSizeModel> getAllAreaSizes() {
        List<AreaSizeModel> list = new ArrayList<>();
        Connection conn = DBHelper.getConnection();

        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM AreaSizes WHERE IsActive = 1");

                while (rs.next()) {
                    AreaSizeModel area = new AreaSizeModel();
                    area.setId(rs.getInt("Id"));
                    area.setName(rs.getString("Name"));
                    area.setMultiplier(rs.getDouble("Multiplier"));
                    list.add(area);
                }

                rs.close();
                conn.close();
                Log.d("AREASIZE_DB", "Loaded: " + list.size());
            } catch (Exception e) {
                Log.e("AREASIZE_DB", "Error: " + e.getMessage());
            }
        }
        return list;
    }
}
