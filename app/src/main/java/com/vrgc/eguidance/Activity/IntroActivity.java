package com.vrgc.eguidance.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vrgc.eguidance.Adapter.ServiceCardAdapter;
import com.vrgc.eguidance.Adapter.SupportCardAdapter;
import com.vrgc.eguidance.Model.ServiceCardModel;
import com.vrgc.eguidance.Model.SupportCardModel;
import com.vrgc.eguidance.R;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    Button btnGetStarted;
    RecyclerView recyclerView, supportRecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        btnGetStarted = findViewById(R.id.btnGetStarted);

        recyclerView = findViewById(R.id.servicesRecyclerView);
        supportRecycler = findViewById(R.id.supportRecyclerView);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<ServiceCardModel> serviceList = new ArrayList<>();
        serviceList.add(new ServiceCardModel(R.drawable.feeling_lost, "Feeling Lost", "If you're feeling disconnected from your inner self..."));
        serviceList.add(new ServiceCardModel(R.drawable.feeling_lonely, "Feeling Lonely", "Experiencing difficulty understanding relationships..."));
        serviceList.add(new ServiceCardModel(R.drawable.trauma, "Trauma", "Connect with seasoned professionals..."));
        serviceList.add(new ServiceCardModel(R.drawable.burnout, "Burnout", "Feeling overwhelmed? Connect with professionals..."));
        serviceList.add(new ServiceCardModel(R.drawable.sadness, "Sadness", "Feeling out of touch with your joyful self..."));
        serviceList.add(new ServiceCardModel(R.drawable.anxiety, "Anxiety", "Anxious thoughts disturbing you from within..."));
        serviceList.add(new ServiceCardModel(R.drawable.stress, "Stress", "Life stresses you too fully enjoying life?"));
        serviceList.add(new ServiceCardModel(R.drawable.confusion, "Confusion", "Feeling uncertain about the direction of life?"));

        recyclerView.setAdapter(new ServiceCardAdapter(serviceList, this));

        supportRecycler.setLayoutManager(new LinearLayoutManager(this));

        List<SupportCardModel> supportList = new ArrayList<>();
        supportList.add(new SupportCardModel(R.drawable.support1, "Meet People Who Understand", "Chat rooms and forums offer accessible support for a range of concerns including illness, stress, and more."));
        supportList.add(new SupportCardModel(R.drawable.support2, "We’re Here for Teens Too", "Ages 13-17? Our listeners are here to chat with teenagers and guarantee confidentiality."));
        supportList.add(new SupportCardModel(R.drawable.support3, "Nurture Your Mental Health", "Explore personal growth and daily self-care skills with curated online help."));

        supportRecycler.setAdapter(new SupportCardAdapter(supportList, this));


        btnGetStarted.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("v_guidance_prefs", MODE_PRIVATE);
            prefs.edit().putBoolean("isFirstTime", false).apply();

            startActivity(new Intent(IntroActivity.this, MainActivity.class));
            finish();
        });
    }
}
