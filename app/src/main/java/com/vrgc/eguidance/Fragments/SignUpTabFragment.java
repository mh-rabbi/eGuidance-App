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

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vrgc.eguidance.R;

import java.util.HashMap;

public class SignUpTabFragment extends Fragment {

    EditText emailInput, passwordInput, confirmPasswordInput, nameInput;
    TextInputLayout passwordLayout, confirmPasswordLayout;
    Button signBtn;
    FirebaseAuth auth;
    DatabaseReference dbRef;

    String email, password, confirmPassword, name;
    final String defaultRole = "Patient";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_tab, container, false);

        emailInput = view.findViewById(R.id.signup_email);
        passwordInput = view.findViewById(R.id.signup_password);
        confirmPasswordInput = view.findViewById(R.id.signup_confirm_password);
        nameInput = view.findViewById(R.id.signup_name);
        signBtn = view.findViewById(R.id.signup_button);

        passwordLayout = view.findViewById(R.id.password_layout);
        confirmPasswordLayout = view.findViewById(R.id.confirm_password_layout);

        auth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("users");

        //  Show error when password field is focused and invalid
        passwordInput.setOnFocusChangeListener((v, hasFocus) -> {
            String pwd = passwordInput.getText().toString();
            if (hasFocus && !isComplexPassword(pwd)) {
                passwordLayout.setError("Password must be 8+ chars, 1 uppercase, 1 number, 1 symbol");
            } else {
                passwordLayout.setError(null);
            }
        });

        //  Show error when confirm password field is focused and doesn't match
        confirmPasswordInput.setOnFocusChangeListener((v, hasFocus) -> {
            String pwd = passwordInput.getText().toString();
            String confirmPwd = confirmPasswordInput.getText().toString();
            if (hasFocus && !confirmPwd.equals(pwd)) {
                confirmPasswordLayout.setError("Passwords do not match");
            } else {
                confirmPasswordLayout.setError(null);
            }
        });

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
            passwordLayout.setError("Password must be 8+ chars, 1 uppercase, 1 number, 1 symbol");
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordLayout.setError("Passwords do not match");
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
        if (getView() != null) {
            getView().post(() -> {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void clearFields() {
        nameInput.setText("");
        emailInput.setText("");
        passwordInput.setText("");
        confirmPasswordInput.setText("");
        passwordLayout.setError(null);
        confirmPasswordLayout.setError(null);
    }
}
