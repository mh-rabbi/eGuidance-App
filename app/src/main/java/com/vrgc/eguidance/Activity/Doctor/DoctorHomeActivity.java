package com.vrgc.eguidance.Activity.Doctor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

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
import com.vrgc.eguidance.Activity.MainActivity;
import com.vrgc.eguidance.Activity.User.HomeActivity;
import com.vrgc.eguidance.Fragments.AboutUsFragment;
import com.vrgc.eguidance.Fragments.DoctorHomeFragment;
import com.vrgc.eguidance.Fragments.ProfileFragment;
import com.vrgc.eguidance.Fragments.ProfileSettingsFragment;
import com.vrgc.eguidance.Fragments.UserNotificationFragment;
import com.vrgc.eguidance.R;
import com.vrgc.eguidance.Utils.FirebaseUtils;

public class DoctorHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // For notification count show to the nav drawer menu
        Menu menu = navigationView.getMenu();
        MenuItem notifItem = menu.findItem(R.id.nav_notification);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new DoctorHomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        // Load user profile data into nav_header
        View headerView = navigationView.getHeaderView(0);
        ImageView profileImage = headerView.findViewById(R.id.user_profile);
        TextView userName = headerView.findViewById(R.id.user_name);
        TextView userEmail = headerView.findViewById(R.id.user_email);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Load profile image from 'userprofile' node
        DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference("userprofile").child(uid);
        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String base64Image = snapshot.child("profileImage").getValue(String.class);
                    if (base64Image != null && !base64Image.isEmpty()) {
                        byte[] decoded = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                        profileImage.setImageBitmap(bitmap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

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

        // Create TextView badge
        TextView actionView = new TextView(this);
        actionView.setPadding(12, 4, 12, 4);
        actionView.setTextColor(Color.WHITE);
        actionView.setTypeface(null, Typeface.BOLD);
        actionView.setBackgroundResource(R.drawable.circle_background); // badge style

        notifItem.setActionView(actionView);

        // Set the count from Firebase
        uid = FirebaseAuth.getInstance().getUid();
        FirebaseUtils.getUnseenNotificationCount(uid, count -> {
            if (count > 0) {
                actionView.setText(String.valueOf(count));
            } else {
                actionView.setText("");
            }
        });


        replaceFragment(new DoctorHomeFragment());
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
            replaceFragment(new DoctorHomeFragment());
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
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(DoctorHomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Logout Successfully!", Toast.LENGTH_SHORT).show();
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