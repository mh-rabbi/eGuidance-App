package com.vrgc.eguidance.Activity.Admin;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vrgc.eguidance.R;

import java.util.HashMap;
import java.util.Map;

public class AssistPatientActivity extends AppCompatActivity {

    EditText meetLinkInput;
    Button postLinkBtn;
    String bookingId, userId;
    DatabaseReference bookingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assist_patient);

        meetLinkInput = findViewById(R.id.meetLinkInput);
        postLinkBtn = findViewById(R.id.btnPostLink);

        bookingId = getIntent().getStringExtra("bookingId");
        userId = getIntent().getStringExtra("userId");

        if (bookingId == null || userId == null) {
            Toast.makeText(this, "Missing booking or user ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bookingRef = FirebaseDatabase.getInstance().getReference("booked").child(bookingId);

        postLinkBtn.setOnClickListener(v -> {
            String link = meetLinkInput.getText().toString().trim();
            if (!Patterns.WEB_URL.matcher(link).matches()) {
                meetLinkInput.setError("Enter a valid Google Meet URL");
                return;
            }

            long timestamp = System.currentTimeMillis();

            Map<String, Object> update = new HashMap<>();
            update.put("meet_link", link);
            update.put("link_timestamp", timestamp);

            bookingRef.updateChildren(update).addOnSuccessListener(unused -> {
                sendNotificationToUser(userId, "Your assistance link is ready. Join using Google Meet.");
                Toast.makeText(this, "Meet link posted!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private void sendNotificationToUser(String targetId, String message) {
        DatabaseReference notifRef = FirebaseDatabase.getInstance().getReference("notifications").child(targetId);
        String notifId = notifRef.push().getKey();

        HashMap<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("timestamp", System.currentTimeMillis());

        notifRef.child(notifId).setValue(map);
    }
}
