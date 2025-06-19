package com.vrgc.eguidance.Activity.Doctor;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vrgc.eguidance.Adapter.DoctorAnonAdapter;
import com.vrgc.eguidance.R;

import java.util.ArrayList;
import java.util.List;

public class FreeSupportActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<String> anonymousIds = new ArrayList<>();
    List<String> displayNames = new ArrayList<>();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_free_support);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseDatabase.getInstance().getReference("free_chat")
                .addValueEventListener(new ValueEventListener() {
                    @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                        anonymousIds.clear();
                        displayNames.clear();
                        int index = 1;
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            String anonId = snap.getKey();
                            anonymousIds.add(anonId);
                            displayNames.add("Anonymous " + index++);
                        }
                        recyclerView.setAdapter(new DoctorAnonAdapter(displayNames, anonymousIds, FreeSupportActivity.this));
                    }
                    @Override public void onCancelled(@NonNull DatabaseError error) {}
                });
    }
}