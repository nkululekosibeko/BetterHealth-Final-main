package com.example.betterhealthapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize CardViews
        CardView userAppointmentBtn = findViewById(R.id.User_Appointment_btn);
        CardView userHistoryBtn = findViewById(R.id.User_History_Btn);
        CardView signOutBtn = findViewById(R.id.Sign_Out_Btn);

        // Set onClick listeners
        userAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to UserAppointmentsActivity
                Intent intent = new Intent(AdminDashboardActivity.this, AdminUserBooking.class);
                startActivity(intent);
            }
        });

        userHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to UserHistoryActivity
                Intent intent = new Intent(AdminDashboardActivity.this, AdminUserHistory.class);
                startActivity(intent);
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out and navigate to MainActivity
                startActivity(new Intent(AdminDashboardActivity.this, User_Type_Picker_Activity.class));
                finish(); // Close the current activity
            }
        });
    }
}
