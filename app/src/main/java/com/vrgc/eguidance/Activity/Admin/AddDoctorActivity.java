package com.vrgc.eguidance.Activity.Admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vrgc.eguidance.R;

import java.util.HashMap;

public class AddDoctorActivity extends AppCompatActivity {

    EditText edtName, edtEmail, edtPhone, edtPassword;
    Button btnAddDoctor;
    FirebaseAuth auth;
    DatabaseReference doctorRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);

        edtName = findViewById(R.id.edtDoctorName);
        edtEmail = findViewById(R.id.edtDoctorEmail);
        edtPhone = findViewById(R.id.edtDoctorPhone);
        edtPassword = findViewById(R.id.edtDoctorPassword);
        btnAddDoctor = findViewById(R.id.btnAddDoctor);

        auth = FirebaseAuth.getInstance();
        doctorRef = FirebaseDatabase.getInstance().getReference("users");

        btnAddDoctor.setOnClickListener(v -> addDoctor());
    }

    private void addDoctor() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String uid = auth.getCurrentUser().getUid();
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("email", email);
                map.put("phone", phone);
                map.put("role", "Doctor");
                map.put("createdAt", System.currentTimeMillis());

                doctorRef.child(uid).setValue(map).addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Doctor added", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else {
                Toast.makeText(this, "Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
