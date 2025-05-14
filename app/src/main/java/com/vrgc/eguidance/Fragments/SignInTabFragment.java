package com.vrgc.eguidance.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vrgc.eguidance.Activity.MainActivity;
import com.vrgc.eguidance.R;

public class SignInTabFragment extends Fragment {
    private EditText edtEmail, edtPassword;
    private Button btnSignIn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin_tab, container, false);

        edtPassword = view.findViewById(R.id.signin_password);
        btnSignIn = view.findViewById(R.id.signin_button);
        edtEmail = view.findViewById(R.id.signin_user);

        btnSignIn.setOnClickListener(v -> handleSignin());


        return super.onCreateView(inflater, container, savedInstanceState);
    }
    private void handleSignin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);

        if (!isValidEmail(email)) {
            edtEmail.setError("Invalid email format");
            return;
        }

        if (!isComplexPassword(password)) {
            edtPassword.setError("Password must be 8+ chars, 1 upper, 1 number, 1 symbol");
        }
    }
    private boolean isComplexPassword(String password) {
        // At least 8 chars, 1 upper, 1 number, 1 special
        return password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
    }
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
