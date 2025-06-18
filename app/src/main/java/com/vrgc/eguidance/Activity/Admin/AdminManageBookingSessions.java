package com.vrgc.eguidance.Activity.Admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vrgc.eguidance.Adapter.BookingAdapter;
import com.vrgc.eguidance.Model.Booking;
import com.vrgc.eguidance.R;

import java.util.ArrayList;
import java.util.List;

public class AdminManageBookingSessions extends AppCompatActivity {

        RecyclerView recyclerView;
        List<Booking> bookingList = new ArrayList<>();
        BookingAdapter adapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_admin_manage_booking_sessions);

            recyclerView = findViewById(R.id.bookingRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            adapter = new BookingAdapter(bookingList, this);
            recyclerView.setAdapter(adapter);

            loadBookings();
        }

        private void loadBookings() {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("booked");

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    bookingList.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Booking booking = snap.getValue(Booking.class);
                        booking.bookingId = snap.getKey();
                        bookingList.add(booking);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(AdminManageBookingSessions.this, "Failed to load bookings", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
