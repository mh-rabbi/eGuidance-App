package com.vrgc.eguidance.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.vrgc.eguidance.Activity.User.AssistanceActivity;
import com.vrgc.eguidance.Activity.User.OurServicesActivity;
import com.vrgc.eguidance.R;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.vrgc.eguidance.Activity.User.BookSessionActivity;
import com.vrgc.eguidance.Activity.User.FreeChatActivity;

public class HomeFragment extends Fragment {

    private TextView tvWelcome;

    // Card images
    private ImageView imgAssistance, imgBookSession, imgFreeChat, imgMentalTest, imgEmotionalTest, imgOurServices;

    private FirebaseAuth auth;
    private DatabaseReference userRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvWelcome = view.findViewById(R.id.vrgc_title);
        imgAssistance = view.findViewById(R.id.assistance);
        imgBookSession = view.findViewById(R.id.booking);
        imgFreeChat = view.findViewById(R.id.freeChat);
        imgMentalTest = view.findViewById(R.id.mentalTest);
        imgEmotionalTest = view.findViewById(R.id.emotionalTest);
        imgOurServices = view.findViewById(R.id.ourServices);

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

        // Set click listeners
        imgAssistance.setOnClickListener(v -> startActivity(new Intent(getActivity(), AssistanceActivity.class)));

        imgBookSession.setOnClickListener(v -> startActivity(new Intent(getActivity(), BookSessionActivity.class)));

        imgFreeChat.setOnClickListener(v -> startActivity(new Intent(getActivity(), FreeChatActivity.class)));

        imgMentalTest.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Mental Test coming soon!", Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(getActivity(), MentalTestActivity.class));
        });

        imgEmotionalTest.setOnClickListener(v -> {
                    Toast.makeText(getActivity(), "Emotional Test coming soon!", Toast.LENGTH_SHORT).show();
                    // startActivity(new Intent(getActivity(), EmotionalTestActivity.class));
        });

        imgOurServices.setOnClickListener(v -> startActivity(new Intent(getActivity(), OurServicesActivity.class)));

        return view;
    }
}
