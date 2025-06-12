package com.vrgc.eguidance.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vrgc.eguidance.R;

import java.util.Calendar;
import java.util.HashMap;

public class BookSessionActivity extends AppCompatActivity {

    Spinner spinnerCondition;
    EditText edtDate, edtTime;
    Button btnSubmit;
    FirebaseAuth auth;
    DatabaseReference bookingRef;

    String[] conditions = {"Anxiety", "Stress", "Depression", "Loneliness", "Career Confusion", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_session);
        spinnerCondition = findViewById(R.id.spinner_condition);
        edtDate = findViewById(R.id.edt_date);
        edtTime = findViewById(R.id.edt_time);
        btnSubmit = findViewById(R.id.btn_submit_booking);

        auth = FirebaseAuth.getInstance();
        bookingRef = FirebaseDatabase.getInstance().getReference("booked");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, conditions);
        spinnerCondition.setAdapter(adapter);

        edtDate.setOnClickListener(v -> showDatePicker());
        edtTime.setOnClickListener(v -> showTimePicker());

        btnSubmit.setOnClickListener(v -> submitBooking());
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String dateStr = dayOfMonth + "/" + (month + 1) + "/" + year;
            edtDate.setText(dateStr);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            String timeStr = String.format("%02d:%02d", hourOfDay, minute);
            edtTime.setText(timeStr);
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        dialog.show();
    }

    private void submitBooking() {
        String condition = spinnerCondition.getSelectedItem().toString();
        String date = edtDate.getText().toString();
        String time = edtTime.getText().toString();

        if (date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = auth.getCurrentUser().getUid();
        String bookingId = bookingRef.push().getKey();

        HashMap<String, Object> bookingMap = new HashMap<>();
        bookingMap.put("userId", uid);
        bookingMap.put("user_condition", condition);
        bookingMap.put("counseling_date", date);
        bookingMap.put("time", time);
        bookingMap.put("status", "pending");

        bookingRef.child(bookingId).setValue(bookingMap)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Session booked successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close activity
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Booking failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}