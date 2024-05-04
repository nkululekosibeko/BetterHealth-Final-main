package com.example.betterhealthapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Admin_Sign_In_Activity extends AppCompatActivity {

    // Define admin credentials
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "adminpassword";

    // Declare UI elements
    private EditText usernameEditText, passwordEditText;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_in);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.DepartmentAdm_Sign_Up_TextEdit);
        passwordEditText = findViewById(R.id.Organisation_Sign_Up_TextEdit);
        signInButton = findViewById(R.id.adm_btn_Sign_In);

        // Set click listener for the sign-in button
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered username and password
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Check if email or password is empty
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Admin_Sign_In_Activity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return; // Exit the method early
                }

                // Check if the entered credentials match the admin credentials
                if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                    // If match, navigate to AdminDashboard activity
                    Intent intent = new Intent(Admin_Sign_In_Activity.this, AdminDashboardActivity.class);
                    startActivity(intent);
                } else {
                    // If not match, show error message
                    Toast.makeText(Admin_Sign_In_Activity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
