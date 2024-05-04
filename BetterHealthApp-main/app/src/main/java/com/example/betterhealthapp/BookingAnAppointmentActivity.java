package com.example.betterhealthapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class BookingAnAppointmentActivity extends AppCompatActivity {

    private EditText reasonEditText, dateEditText;
    private Spinner timeSlotSpinner;
    private Button bookNowButton;

    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference appointmentsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_an_appointment);

        mAuth = FirebaseAuth.getInstance();
        appointmentsRef = FirebaseDatabase.getInstance().getReference("appointments");
        reasonEditText = findViewById(R.id.Appointment_Reason_TextEdit);
        dateEditText = findViewById(R.id.Appointment_Date_TextEdit);
        timeSlotSpinner = findViewById(R.id.spinner_time_slots);
        bookNowButton = findViewById(R.id.Next_btn_Sign_Up);
        progressBar = findViewById(R.id.progressBar);

        dateEditText.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String cleanString = s.toString().replaceAll("[^\\d]", "");
                    String formatted = formatDateString(cleanString);
                    current = formatted;
                    dateEditText.setText(formatted);
                    dateEditText.setSelection(formatted.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Set OnClickListener for the bookNowButton
        bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookAppointment();
            }
        });
    }

    private String formatDateString(String input) {
        if (input.length() <= 2) {
            return input;
        } else if (input.length() <= 4) {
            return input.substring(0, 2) + "/" + input.substring(2);
        } else {
            return input.substring(0, 2) + "/" + input.substring(2, 4) + "/" + input.substring(4);
        }
    }

    private void bookAppointment() {
        progressBar.setVisibility(View.VISIBLE);
        String reason = reasonEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String time = timeSlotSpinner.getSelectedItem().toString();

        // Check if any of the fields are empty
        if (reason.isEmpty() || date.isEmpty() || time.isEmpty()) {
            // Display toast messages for each empty field
            if (reason.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(BookingAnAppointmentActivity.this, "Please enter a reason for the appointment", Toast.LENGTH_SHORT).show();
            }
            if (date.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(BookingAnAppointmentActivity.this, "Please select a date for the appointment", Toast.LENGTH_SHORT).show();
            }
            if (time.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(BookingAnAppointmentActivity.this, "Please select a time for the appointment", Toast.LENGTH_SHORT).show();
            }
            return; // Exit method as fields are empty
        }

        // Validate date format (mm/dd/yyyy) and range of values
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        dateFormat.setLenient(false);
        try {
            Date parsedDate = dateFormat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);

            // Extract month, day, and year
            int month = calendar.get(Calendar.MONTH) + 1; // Month is zero-based, so add 1
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int year = calendar.get(Calendar.YEAR);

            // Check if month, day, and year are within valid ranges
            if (month < 1 || month > 12 || day < 1 || day > 31 || year < 2024) {
                // Invalid date range, show error message
                progressBar.setVisibility(View.GONE);
                Toast.makeText(BookingAnAppointmentActivity.this, "Please enter a valid date within the year 2024", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ParseException e) {
            // If parsing fails, show an error message and return
            progressBar.setVisibility(View.GONE);
            Toast.makeText(BookingAnAppointmentActivity.this, "Please enter a valid date in the format MM/dd/yyyy", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            return;
        }
        String userId = currentUser.getUid();

        isSlotAvailable(date, time, new SlotAvailabilityCallback() {
            @Override
            public void onSlotAvailable() {
                String userId = mAuth.getCurrentUser().getUid();
                String appointmentId = appointmentsRef.child("appointments").push().getKey();
                Appointment appointment = new Appointment(appointmentId, userId, date, reason, time, "pending", "", "", "");

                appointmentsRef.child(appointmentId).setValue(appointment)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Log.d("Firebase", "Appointment booked successfully");
                                sendConfirmationEmail(currentUser.getEmail(), date, time, reason);
                                startActivity(new Intent(BookingAnAppointmentActivity.this, StudentDashboardActivity.class));
                                finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Log.e("Firebase", "Failed to book appointment", task.getException());
                                Toast.makeText(BookingAnAppointmentActivity.this, "Failed to book appointment", Toast.LENGTH_SHORT).show();
                            }
                        });

            }

            @Override
            public void onSlotUnavailable() {
                Toast.makeText(BookingAnAppointmentActivity.this, "There is a clash with another appointment at this time. Please select a different time slot", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void isSlotAvailable(String date, String time, SlotAvailabilityCallback callback) {
        Query query = appointmentsRef.orderByChild("date").equalTo(date);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String appointmentTime = snapshot.child("time").getValue(String.class);
                        String appointmentStatus = snapshot.child("status").getValue(String.class);
                        if (appointmentTime.equals(time) && appointmentStatus.equals("pending")) {
                            callback.onSlotUnavailable();
                            return;
                        }
                    }
                    callback.onSlotAvailable();
                } else {
                    callback.onSlotAvailable();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onSlotUnavailable();
            }
        });
    }

    interface SlotAvailabilityCallback {
        void onSlotAvailable();
        void onSlotUnavailable();
    }

    private void sendConfirmationEmail(String assignedPerson, String date, String time, String reason) {
        new SendEmailTask(assignedPerson, date, time, reason).execute();
    }

    private class SendEmailTask extends AsyncTask<Void, Void, Boolean> {
        private String assignedPerson, date, time, reason;

        public SendEmailTask(String assignedPerson, String date, String time, String reason) {
            this.assignedPerson = assignedPerson;
            this.date = date;
            this.time = time;
            this.reason = reason;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("fixitumndoni@gmail.com", "fnmcddqatwfrycuc");
                    }
                });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("fixitumndoni@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(assignedPerson));
                message.setSubject("Appointment Confirmation");
                message.setText("Hi!,\n\n" +
                        "We are pleased to inform you that your appointment has been successfully booked with us.\n\n" +
                        "Appointment Details:\n" +
                        "Date: " + date + "\n" +
                        "Time: " + time + "\n" +
                        "Reason for Appointment: " + reason + "\n\n" +
                        "We value your trust in our services and are committed to providing you with the best experience possible.\n" +
                        "If you have any questions or need to make any changes to your appointment, please feel free to contact us.\n\n" +
                        "Thank you for choosing our service. We look forward to meeting you.\n\n" +
                        "Warm regards,\n" +
                        "Better Health App");

                Transport.send(message);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }


    }
}
