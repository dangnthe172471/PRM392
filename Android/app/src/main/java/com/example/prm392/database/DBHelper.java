package com.example.prm392.database;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBHelper {
    private static final String ip = "10.0.2.2";
    private static final String port = "1433";
    private static final String dbName = "CareU";
    private static final String username = "sa";
    private static final String password = "123";

    public static Connection getConnection() {
        Connection connection = null;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connectionUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName
                    + ";user=" + username + ";password=" + password + ";";
            connection = DriverManager.getConnection(connectionUrl);
            Log.i("DB", "Connected successfully");
        } catch (Exception e) {
            Log.e("DB_ERROR", e.getMessage());
        }

        return connection;
    }
}

