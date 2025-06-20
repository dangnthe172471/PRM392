package com.example.prm392;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
Connection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                TextView textView = findViewById(R.id.textView);
                ConSQL conSQL = new ConSQL();
                connection = conSQL.conclass();
                if(conSQL != null) {
                    try {
                        List<String> list = new ArrayList<>();
                        String sql = "SELECT * FROM Users";
                        Statement stmt = connection.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);
                        while (rs.next()) {
                            list.add(rs.getString(2));
                        }
                        textView.setText("Data: " + list.toString());
                        connection.close();
                    }catch (Exception e){
                        textView.setText("Error: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}