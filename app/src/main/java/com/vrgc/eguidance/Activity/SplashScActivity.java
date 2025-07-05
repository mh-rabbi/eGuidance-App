package com.vrgc.eguidance.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vrgc.eguidance.Activity.Admin.AdminDashboardActivity;
import com.vrgc.eguidance.Activity.Doctor.DoctorHomeActivity;
import com.vrgc.eguidance.Activity.User.HomeActivity;
import com.vrgc.eguidance.R;

public class SplashScActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_sc);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000); // Wait for 2 seconds
                    runOnUiThread(() -> {
                        SharedPreferences prefs = getSharedPreferences("v_guidance_prefs", MODE_PRIVATE);
                        boolean isFirstTime = prefs.getBoolean("isFirstTime", true);

                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                        if (isFirstTime) {
                            startActivity(new Intent(SplashScActivity.this, IntroActivity.class));
                        } else if (currentUser != null) {
                            String uid = currentUser.getUid();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(uid);

                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String role = snapshot.child("role").getValue(String.class);
                                    Intent intent;
                                    switch (role) {
                                        case "Admin":
                                            intent = new Intent(SplashScActivity.this, AdminDashboardActivity.class);
                                            break;
                                        case "Doctor":
                                            intent = new Intent(SplashScActivity.this, DoctorHomeActivity.class);
                                            break;
                                        default:
                                            intent = new Intent(SplashScActivity.this, HomeActivity.class); // patient
                                    }
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    startActivity(new Intent(SplashScActivity.this, MainActivity.class));
                                    finish();
                                }
                            });
                        } else {
                            startActivity(new Intent(SplashScActivity.this, MainActivity.class));
                            finish();
                        }

                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
