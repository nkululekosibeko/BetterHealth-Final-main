package com.example.betterhealthapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Sign_In_Activity extends AppCompatActivity {
    EditText User_Email, Password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        User_Email = findViewById(R.id.email_Sign_In_TextEdit);
        Password = findViewById(R.id.password_Sign_In_TextEdit);

        // Get reference to the sign-in button
        Button signInButton = findViewById(R.id.Landing_btn_Sign_In);

        // Set click listener for the sign-in button
        signInButton.setOnClickListener(v -> signIn());
    }

    private void signIn() {
        String email = User_Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        // Check if email or password is empty
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(Sign_In_Activity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return; // Exit the method early
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Toast.makeText(Sign_In_Activity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Sign_In_Activity.this, StudentDashboardActivity.class);
                            startActivity(intent);
                            finish();
                            // You can add further actions here like navigating to another activity
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Sign_In_Activity.this, "Sign in failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
