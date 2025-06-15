package com.vrgc.eguidance.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vrgc.eguidance.Fragments.AboutUsFragment;
import com.vrgc.eguidance.Fragments.AdminHomeFragment;
import com.vrgc.eguidance.Fragments.UserNotificationFragment;
import com.vrgc.eguidance.Fragments.ProfileFragment;
import com.vrgc.eguidance.Fragments.ProfileSettingsFragment;
import com.vrgc.eguidance.R;

public class AdminDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new AdminHomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        // Load user profile data into nav_header
        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.user_name);
        TextView userEmail = headerView.findViewById(R.id.user_email);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Load name and email from 'users' node
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChild("name")) {
                        userName.setText(snapshot.child("name").getValue(String.class));
                    }
                    if (snapshot.hasChild("email")) {
                        userEmail.setText(snapshot.child("email").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        replaceFragment(new AdminHomeFragment());
    }

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            replaceFragment(new AdminHomeFragment());
        }else if (itemId == R.id.nav_profile) {
            replaceFragment(new ProfileFragment());
        }
        else if (itemId == R.id.nav_settings) {
            replaceFragment(new ProfileSettingsFragment());
        } else if (itemId == R.id.nav_notification) {
            replaceFragment(new UserNotificationFragment());
        } else if (itemId == R.id.nav_about) {
            replaceFragment(new AboutUsFragment());
        } else if (itemId == R.id.nav_logout) {
            Toast.makeText(this, "Logout Successfully!", Toast.LENGTH_SHORT).show();
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}