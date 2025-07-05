package com.vrgc.eguidance.Activity.Doctor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import com.vrgc.eguidance.R;

import java.text.SimpleDateFormat;
import java.util.*;

public class MeetPatientActivity extends AppCompatActivity {

    TextView txtPatientName, txtMeetingTime, txtMeetingDate, txtMeetLink;
    Button btnJoinMeet;
    DatabaseReference bookedRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_patient);

        txtPatientName = findViewById(R.id.txtPatientName);
        txtMeetingTime = findViewById(R.id.txtMeetingTime);
        txtMeetingDate = findViewById(R.id.txtMeetingDate);
        txtMeetLink = findViewById(R.id.txtMeetLink);
        btnJoinMeet = findViewById(R.id.btnJoinMeet);
        btnJoinMeet.setEnabled(false);

        String doctorId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        bookedRef = FirebaseDatabase.getInstance().getReference("booked");

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        long nowMillis = System.currentTimeMillis();

        bookedRef.orderByChild("status").equalTo("accepted")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean matchFound = false;

                        for (DataSnapshot snap : snapshot.getChildren()) {
                            String docId = snap.child("doctorId").getValue(String.class);
                            String date = snap.child("counseling_date").getValue(String.class);
                            String time = snap.child("time").getValue(String.class);
                            String link = snap.child("meet_link").getValue(String.class);
                            String userName = snap.child("user_name").getValue(String.class);

                            if (docId == null || date == null || time == null || link == null) continue;

                            if (!docId.equals(doctorId)) continue;
                            if (!date.equals(currentDate)) continue;

                            // Parse time to millis
                            long meetingMillis = parseTimeToMillis(date, time);
                            long durationMillis = 60 * 60 * 1000;

                            if (Math.abs(nowMillis - meetingMillis) <= durationMillis) {
                                matchFound = true;

                                txtPatientName.setText("Patient: " + userName);
                                txtMeetingDate.setText("Date: " + date);
                                txtMeetingTime.setText("Time: " + time);
                                txtMeetLink.setText("Link: " + link);

                                btnJoinMeet.setEnabled(true);
                                btnJoinMeet.setOnClickListener(v -> {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                                    startActivity(intent);
                                });

                                break; // stop after first match
                            }
                        }

                        if (!matchFound) {
                            Toast.makeText(MeetPatientActivity.this, "No meeting scheduled at this time", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MeetPatientActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private long parseTimeToMillis(String date, String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
            Date d = sdf.parse(date + " " + time);
            return d.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
