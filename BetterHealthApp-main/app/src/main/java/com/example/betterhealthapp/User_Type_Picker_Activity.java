package com.example.betterhealthapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class User_Type_Picker_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_type_picker);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get references to the sign-in and sign-up buttons
        Button StdPickSignInButton = findViewById(R.id.Sign_Up_User_Pick_Student);
        Button admPickSignInButton = findViewById(R.id.Sign_Up_User_Pick_Admin);



        // Set click listeners for the buttons
        StdPickSignInButton.setOnClickListener(v -> {
            // Start Student Sign-Up Extra_Info Activity
            Intent stdSignUpIntent = new Intent(User_Type_Picker_Activity.this, MainActivity.class);
            startActivity(stdSignUpIntent);
        });
        admPickSignInButton.setOnClickListener(v -> {
            // Start Admin Sign-Up Extra_Info Activity
            Intent admSignUpIntent = new Intent(User_Type_Picker_Activity.this, Admin_Sign_In_Activity.class);
            startActivity(admSignUpIntent);
        });

    }
}