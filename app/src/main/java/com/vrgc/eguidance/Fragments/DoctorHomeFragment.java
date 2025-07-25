package com.vrgc.eguidance.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vrgc.eguidance.Activity.Admin.AssistPatientActivity;
import com.vrgc.eguidance.Activity.Doctor.FreeSupportActivity;
import com.vrgc.eguidance.Activity.Doctor.MeetPatientActivity;
import com.vrgc.eguidance.Activity.Doctor.ViewScheduleActivity;
import com.vrgc.eguidance.R;


public class DoctorHomeFragment extends Fragment {
    private TextView tvWelcome;

    private CardView cardMeetPatient, cardViewSchedules, cardFreeSupport, comingSoon1, comingSoon2, comingSoon3;

    private FirebaseAuth auth;
    private DatabaseReference userRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_home, container, false);

        tvWelcome = view.findViewById(R.id.vrgc_title);
        cardMeetPatient = view.findViewById(R.id.cardMeetPatient);
        cardViewSchedules = view.findViewById(R.id.cardViewSchedules);
        cardFreeSupport = view.findViewById(R.id.cardFreeSupport);

        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (uid != null) {
            userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        String role = snapshot.child("role").getValue(String.class);
                        tvWelcome.setText("Welcome,\n" + name + " (" + role + ")");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        cardMeetPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MeetPatientActivity.class);
                startActivity(intent);
            }
        });
        cardViewSchedules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewScheduleActivity.class);
                startActivity(intent);
            }
            });
        cardFreeSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FreeSupportActivity.class);
                startActivity(intent);
            }
        });
        comingSoon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "New features coming soon!", Toast.LENGTH_SHORT).show();
            }
        });
        comingSoon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "New features coming soon!", Toast.LENGTH_SHORT).show();
            }
        });
        comingSoon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "New features coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}