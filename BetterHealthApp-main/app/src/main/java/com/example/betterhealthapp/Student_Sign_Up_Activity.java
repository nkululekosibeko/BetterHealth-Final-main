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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Student_Sign_Up_Activity extends AppCompatActivity {

    EditText studentEmail_Sign_Up_TextEdit, Password_Sign_Up_TextEdit, Password2_Sign_Up_TextEdit;

    Button std_btn_Sign_Up;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_up);

        mAuth = FirebaseAuth.getInstance();

        studentEmail_Sign_Up_TextEdit = findViewById(R.id.studentNo_Sign_Up_TextEdit);
        Password_Sign_Up_TextEdit = findViewById(R.id.program_Major_Sign_Up_TextEdit);
        Password2_Sign_Up_TextEdit = findViewById(R.id.LevelOfStudy_Sign_Up_TextEdit);

        std_btn_Sign_Up = findViewById(R.id.std_btn_Sign_Up);

        // Set click listener for the button
        std_btn_Sign_Up.setOnClickListener(v1 -> signUp());
    }

    private void signUp() {
        String email = studentEmail_Sign_Up_TextEdit.getText().toString().trim();
        String password = Password_Sign_Up_TextEdit.getText().toString().trim();
        String confirmPassword = Password2_Sign_Up_TextEdit.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(Student_Sign_Up_Activity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(Student_Sign_Up_Activity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success
                            Toast.makeText(Student_Sign_Up_Activity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Student_Sign_Up_Activity.this, Common_Info_SignUP_Activity.class);
                            startActivity(intent);
                            finish();
                            // You can add further actions here like navigating to another activity
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Student_Sign_Up_Activity.this, "Sign up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

