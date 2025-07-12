package com.example.prm392.database;

import android.util.Log;

import com.example.prm392.model.ServiceModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceRepository {
    public static List<ServiceModel> getAllServices() {
        List<ServiceModel> list = new ArrayList<>();

        Connection conn = DBHelper.getConnection();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Services WHERE IsActive = 1");

                while (rs.next()) {
                    ServiceModel s = new ServiceModel();
                    s.setId(rs.getInt("Id"));
                    s.setName(rs.getString("Name"));
                    s.setDescription(rs.getString("Description"));
                    s.setBasePrice(rs.getDouble("BasePrice"));
                    s.setDuration(rs.getString("Duration"));
                    s.setIcon(rs.getString("Icon"));

                    list.add(s);
                }

                rs.close();
                conn.close();
            } catch (Exception e) {
                Log.e("SERVICE_DB", "Error: " + e.getMessage());
            }
        }

        return list;
    }

    public static List<ServiceModel> searchServicesByName(String searchQuery) {
        List<ServiceModel> list = new ArrayList<>();

        Connection conn = DBHelper.getConnection();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                String query = "SELECT * FROM Services WHERE IsActive = 1 AND Name COLLATE Latin1_General_CI_AI LIKE N'%" + searchQuery + "%'";
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    ServiceModel s = new ServiceModel();
                    s.setId(rs.getInt("Id"));
                    s.setName(rs.getString("Name"));
                    s.setDescription(rs.getString("Description"));
                    s.setBasePrice(rs.getDouble("BasePrice"));
                    s.setDuration(rs.getString("Duration"));
                    s.setIcon(rs.getString("Icon"));

                    list.add(s);
                }

                rs.close();
                conn.close();
            } catch (Exception e) {
                Log.e("SERVICE_DB", "Error searching services: " + e.getMessage());
            }
        }

        return list;
    }
}
