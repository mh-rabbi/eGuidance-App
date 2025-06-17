package com.vrgc.eguidance.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vrgc.eguidance.Activity.AdminManageBookingSessions;
import com.vrgc.eguidance.R;

public class AdminHomeFragment extends Fragment {
    public ImageView imgAdminBooking;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        imgAdminBooking = view.findViewById(R.id.adminBooking);

        imgAdminBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdminManageBookingSessions.class);
                startActivity(intent);
            }
            });
        return view;
    }
}
