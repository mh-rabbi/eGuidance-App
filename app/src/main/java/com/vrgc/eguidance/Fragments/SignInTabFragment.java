package com.vrgc.eguidance.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class SignInTabFragment extends Fragment {
    private EditText edtEmail, edtPassword;
    private Button btnSignIn;
    private Spinner roleSpinner;
    private String selectedRole;

    private FirebaseAuth auth;
    private DatabaseReference userRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin_tab, container, false);

        edtEmail = view.findViewById(R.id.signin_user);
        edtPassword = view.findViewById(R.id.signin_password);
        btnSignIn = view.findViewById(R.id.signin_button);
        roleSpinner = view.findViewById(R.id.user_role_spinner);

        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");

        // Setup role spinner
        String[] roles = {"Patient", "Doctor", "Admin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, roles);
        roleSpinner.setAdapter(adapter);
        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRole = roles[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedRole = "Patient";
            }
        });

        btnSignIn.setOnClickListener(v -> handleSignin());

        return view;
    }

    private void handleSignin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (!isValidEmail(email)) {
            edtEmail.setError("Invalid email format");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Password is required");
            return;
        }

        // Authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        String uid = user.getUid();

                        userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String savedRole = snapshot.child("role").getValue(String.class);
                                    String name = snapshot.child("name").getValue(String.class);

                                    // Role check
                                    if (!savedRole.equals(selectedRole)) {
                                        Toast.makeText(getActivity(), "Role mismatch! Please select correct role.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    // Navigate to HomeActivity
                                   // Intent intent = new Intent(getActivity(), HomeActivity.class);
                                    // Navigate to appropriate activity based on role
                                    Intent intent;
                                    switch (savedRole) {
                                        case "Admin":
                                            intent = new Intent(getActivity(), AdminDashboardActivity.class);
                                            break;
                                        case "Doctor":
                                            intent = new Intent(getActivity(), DoctorHomeActivity.class);
                                            break;
                                        case "Patient":
                                            intent = new Intent(getActivity(), HomeActivity.class);
                                            break;
                                        default:
                                            throw new IllegalStateException("Unexpected value: " + savedRole);
                                    }
                                    intent.putExtra("userName", name);
                                    intent.putExtra("userRole", savedRole);
                                    startActivity(intent);
                                    requireActivity().finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getActivity(), "Failed to read user data", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(getActivity(), "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
