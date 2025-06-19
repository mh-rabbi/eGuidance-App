package com.vrgc.eguidance.Activity.User;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vrgc.eguidance.R;

public class AssistanceActivity extends AppCompatActivity {

    TextView meetLinkTextView, statusTextView;
    FirebaseAuth auth;
    DatabaseReference bookedRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistance);

        meetLinkTextView = findViewById(R.id.meetLinkTextView);
        statusTextView = findViewById(R.id.statusTextView);

        auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        bookedRef = FirebaseDatabase.getInstance().getReference("booked");

        fetchUserSession(userId);
    }

    private void fetchUserSession(String userId) {
        bookedRef.orderByChild("userId").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean found = false;

                        for (DataSnapshot snap : snapshot.getChildren()) {
                            String status = snap.child("status").getValue(String.class);
                            if (!"accepted".equals(status)) continue;

                            String meetLink = snap.child("meet_link").getValue(String.class);
                            Long linkTime = snap.child("link_timestamp").getValue(Long.class);

                            if (meetLink != null && linkTime != null) {
                                long now = System.currentTimeMillis();
                                long diff = now - linkTime;

                                if (diff <= 60 * 60 * 1000) { // within 60 minutes
                                    if (Patterns.WEB_URL.matcher(meetLink).matches()) {
                                        showMeetLink(meetLink);
                                        found = true;
                                        break;
                                    }
                                }
                            }
                        }

                        if (!found) {
                            meetLinkTextView.setVisibility(View.GONE);
                            statusTextView.setText("No active assistance session found.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AssistanceActivity.this, "Error loading session", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showMeetLink(String link) {
        statusTextView.setText("Your session is active. Join below:");
        meetLinkTextView.setText(link);
        meetLinkTextView.setVisibility(View.VISIBLE);
        meetLinkTextView.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(i);
        });
    }
}