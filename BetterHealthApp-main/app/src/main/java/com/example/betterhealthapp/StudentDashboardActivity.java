package com.example.betterhealthapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentDashboardActivity extends AppCompatActivity {

    CardView  LogOut, BookApointment, EmergencyServices, MyBookings, AppointmentHostory;
    Button Profile;
    FirebaseUser user;
    TextView DashBoardName;
    private DatabaseReference mDatabase;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_dashboard);
        DashBoardName=findViewById(R.id.Student_Dashboard_Name);
        Profile=findViewById(R.id.Student_Dashboard_EditProfile);
        LogOut=findViewById(R.id.Logout_Btn);
        BookApointment=findViewById(R.id.Book_An_Appointment_btn);
        EmergencyServices=findViewById(R.id.Emergency_Btn);
        MyBookings=findViewById(R.id.My_Appointments_Btn);
        AppointmentHostory=findViewById(R.id.Appointment_History_Btn);


        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get current user ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Retrieve user information from the database
        mDatabase.child("userInfo").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserProfileInfo userProfileInfo = dataSnapshot.getValue(UserProfileInfo.class);
                    if (userProfileInfo != null) {
                        String userName = userProfileInfo.getFullName();
                        String surname = userProfileInfo.getSurname();
                        DashBoardName.setText(userName +" "+ surname);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(StudentDashboardActivity.this, User_Type_Picker_Activity.class);
                startActivity(intent);
                finish();

            }
        });

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentDashboardActivity.this, UserProfileActivity.class);
                startActivity(intent);

            }
        });
        AppointmentHostory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentDashboardActivity.this, MyBookingHistory.class);
                startActivity(intent);


            }
        });
        BookApointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentDashboardActivity.this, BookingAnAppointmentActivity.class);
                startActivity(intent);

            }
        });
        MyBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentDashboardActivity.this, MyBookingsActivity.class);
                startActivity(intent);

            }
        });
        EmergencyServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentDashboardActivity.this, EmergencyActivity.class);
                startActivity(intent);

            }
        });
    }
}