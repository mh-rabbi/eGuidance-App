package com.vrgc.eguidance.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vrgc.eguidance.R;

import java.util.HashMap;

public class SignUpTabFragment extends Fragment {

    EditText emailInput, passwordInput, confirmPasswordInput, nameInput;
    Button signBtn;
    FirebaseAuth auth;
    DatabaseReference dbRef;

    String email, password, confirmPassword, name;
    final String defaultRole = "Patient";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_tab, container, false);

        emailInput = view.findViewById(R.id.signup_email);
        passwordInput = view.findViewById(R.id.signup_password);
        confirmPasswordInput = view.findViewById(R.id.signup_confirm_password);
        nameInput = view.findViewById(R.id.signup_name);
        signBtn = view.findViewById(R.id.signup_button);

        auth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("users");

        signBtn.setOnClickListener(v -> handleSignUp());

        return view;
    }

    private void handleSignUp() {
        email = emailInput.getText().toString().trim();
        password = passwordInput.getText().toString().trim();
        confirmPassword = confirmPasswordInput.getText().toString().trim();
        name = nameInput.getText().toString().trim();

        if (!isValidEmail(email)) {
            emailInput.setError("Invalid email format");
            return;
        }

        if (!isComplexPassword(password)) {
            passwordInput.setError("Password must be 8+ chars, 1 upper, 1 number, 1 symbol");
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordInput.setError("Passwords do not match");
            return;
        }

        if (TextUtils.isEmpty(name)) {
            nameInput.setError("Name cannot be empty");
            return;
        }

        // Firebase signup
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        String uid = firebaseUser.getUid();

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name", name);
                        userMap.put("email", email);
                        userMap.put("role", defaultRole);
                        userMap.put("joinedAt", System.currentTimeMillis());

                        dbRef.child(uid).setValue(userMap)
                                .addOnSuccessListener(unused -> {
                                    showToast("Signup successful! You can now log in.");
                                    clearFields();
                                })
                                .addOnFailureListener(e -> showToast("Signup failed: " + e.getMessage()));
                    } else {
                        showToast("Signup failed: " + task.getException().getMessage());
                    }
                });
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isComplexPassword(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
    }

    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void clearFields() {
        nameInput.setText("");
        emailInput.setText("");
        passwordInput.setText("");
        confirmPasswordInput.setText("");
    }
}