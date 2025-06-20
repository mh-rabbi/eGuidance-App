package com.vrgc.eguidance.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vrgc.eguidance.R;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private ImageView profileImageView;
    private TextView nameView, phoneView, addressView, ageView, userNameView, emailView;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImageView = view.findViewById(R.id.profile_pic);
        nameView = view.findViewById(R.id.txt_fullName);
        phoneView = view.findViewById(R.id.txt_phone);
        addressView = view.findViewById(R.id.txt_address);
        ageView = view.findViewById(R.id.txt_age);
        userNameView = view.findViewById(R.id.user_name);
        emailView = view.findViewById(R.id.user_email);

        loadUserProfile();

        return view;
    }

    private void loadUserProfile() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Load from 'userprofile' node
        DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference("userprofile").child(uid);
        userProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    nameView.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                    phoneView.setText(Objects.requireNonNull(snapshot.child("phone").getValue()).toString());
                    addressView.setText(Objects.requireNonNull(snapshot.child("address").getValue()).toString());
                    ageView.setText(Objects.requireNonNull(snapshot.child("age").getValue()).toString());

                    String base64Image = Objects.requireNonNull(snapshot.child("profileImage").getValue()).toString();
                    if (!base64Image.isEmpty()) {
                        byte[] decoded = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                        profileImageView.setImageBitmap(bitmap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        // Load username and email from 'users' node
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userNameView.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                    emailView.setText(Objects.requireNonNull(snapshot.child("email").getValue()).toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}

