package com.vrgc.eguidance.Activity.Admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vrgc.eguidance.Adapter.UserManageAdapter;
import com.vrgc.eguidance.Model.UserModel;
import com.vrgc.eguidance.R;

import java.util.ArrayList;
import java.util.List;

public class ManageUserActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<UserModel> userList = new ArrayList<>();
    UserManageAdapter adapter;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);

        recyclerView = findViewById(R.id.userRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userRef = FirebaseDatabase.getInstance().getReference("users");

        adapter = new UserManageAdapter(userList, this);
        recyclerView.setAdapter(adapter);

        loadUsers();
    }

    private void loadUsers() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    UserModel user = snap.getValue(UserModel.class);
                    user.uid = snap.getKey();
                    userList.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageUserActivity.this, "Error loading users", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
