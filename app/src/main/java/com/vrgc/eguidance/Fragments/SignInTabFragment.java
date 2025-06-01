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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vrgc.eguidance.Activity.HomeActivity;
import com.vrgc.eguidance.Activity.MainActivity;
import com.vrgc.eguidance.R;

public class SignInTabFragment extends Fragment {
    private EditText edtEmail, edtPassword;
    private Button btnSignIn;
    Spinner roleSpinner;
    String selectedRole;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin_tab, container, false);

        edtPassword = view.findViewById(R.id.signin_password);
        btnSignIn = view.findViewById(R.id.signin_button);
        edtEmail = view.findViewById(R.id.signin_user);
        roleSpinner = view.findViewById(R.id.user_role_spinner);

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
                selectedRole = "Patient"; // default
            }
        });

        btnSignIn.setOnClickListener(v -> handleSignin());

        return view;
    }
    private void handleSignin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        Intent i = new Intent(getActivity(), HomeActivity.class);
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
