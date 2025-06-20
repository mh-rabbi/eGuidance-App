package com.vrgc.eguidance.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class OurServicesActivity extends AppCompatActivity {

    RecyclerView recyclerView, supportRecycler;
    Button btnGetHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_services);

        recyclerView = findViewById(R.id.servicesRecyclerView);
        supportRecycler = findViewById(R.id.supportRecyclerView);

        btnGetHelp = findViewById(R.id.btnGetHelp);

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


        btnGetHelp.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));
    }
}
