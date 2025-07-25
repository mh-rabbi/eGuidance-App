package com.vrgc.eguidance.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.vrgc.eguidance.Activity.Admin.AddDoctorActivity;
import com.vrgc.eguidance.Activity.Admin.AdminManageBookingSessions;
import com.vrgc.eguidance.Activity.Admin.ManageUserActivity;
import com.vrgc.eguidance.R;

public class AdminHomeFragment extends Fragment {
    public CardView cardAdminBooking, cardAddDoctor, cardManageUser, comingSoon1, comingSoon2, comingSoon3;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        cardAddDoctor = view.findViewById(R.id.addDoctor);
        cardAdminBooking = view.findViewById(R.id.adminBooking);
        cardManageUser = view.findViewById(R.id.manageUser);
        comingSoon1 = view.findViewById(R.id.comingSoon1);


        cardAddDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddDoctorActivity.class);
                startActivity(intent);
            }
        });

        cardAdminBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdminManageBookingSessions.class);
                startActivity(intent);
            }
        });

        cardManageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ManageUserActivity.class);
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
