package com.vrgc.eguidance.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

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

                        Intent intent;
                        if (isFirstTime) {
                            intent = new Intent(SplashScActivity.this, IntroActivity.class);
                        } else {
                            intent = new Intent(SplashScActivity.this, MainActivity.class);
                        }
                        startActivity(intent);
                        finish();
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
