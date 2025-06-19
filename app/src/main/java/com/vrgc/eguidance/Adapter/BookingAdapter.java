package com.vrgc.eguidance.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vrgc.eguidance.Model.Booking;
import com.vrgc.eguidance.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    List<Booking> bookingList;
    Context context;

    public BookingAdapter(List<Booking> bookingList, Context context) {
        this.bookingList = bookingList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.txtInfo.setText("Condition: " + booking.user_condition +
                "\nDate: " + booking.counseling_date +
                "\nTime: " + booking.time +
                "\nStatus: " + booking.status);

        if ("pending".equals(booking.status)) {
            holder.btnAssign.setVisibility(View.VISIBLE);
            holder.btnAssign.setOnClickListener(v -> {
                showDoctorDialog(booking);
            });
        } else {
            holder.btnAssign.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    private void showDoctorDialog(Booking booking) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference bookedRef = FirebaseDatabase.getInstance().getReference("booked");

        userRef.orderByChild("role").equalTo("Doctor").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> doctorNames = new ArrayList<>();
                List<String> doctorIds = new ArrayList<>();
                List<Boolean> availability = new ArrayList<>();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    String doctorId = snap.getKey();
                    String name = snap.child("name").getValue(String.class);
                    if (name != null) {
                        doctorNames.add(name);
                        doctorIds.add(doctorId);
                    }
                }

                if (doctorNames.isEmpty()) {
                    Toast.makeText(context, "No registered doctors found.", Toast.LENGTH_SHORT).show();
                    return;
                }

                /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select a Doctor");
                builder.setItems(doctorNames.toArray(new String[0]), (dialog, which) -> {
                    String doctorName = doctorNames.get(which);
                    String doctorId = doctorIds.get(which);

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("booked").child(booking.bookingId);

                    Map<String, Object> update = new HashMap<>();
                    update.put("status", "accepted");
                    update.put("doctor_name", doctorName);
                    update.put("doctorId", doctorId);

                    ref.updateChildren(update).addOnSuccessListener(unused -> {
                        sendNotificationToUser(booking.userId, booking, doctorName);
                        sendNotificationToDoctor(doctorId, booking);
                        Toast.makeText(context, "Booking updated and doctor assigned!", Toast.LENGTH_SHORT).show();
                    });
                });
                builder.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed to load doctors.", Toast.LENGTH_SHORT).show();
            }
        });*/

                // Fetch all bookings to check conflicts
                bookedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot bookedSnap) {
                        for (String docId : doctorIds) {
                            boolean isAvailable = true;
                            for (DataSnapshot b : bookedSnap.getChildren()) {
                                String status = b.child("status").getValue(String.class);
                                String assignedDoc = b.child("doctorId").getValue(String.class);
                                String date = b.child("counseling_date").getValue(String.class);
                                String time = b.child("time").getValue(String.class);

                                if ("accepted".equals(status) &&
                                        docId.equals(assignedDoc) &&
                                        booking.counseling_date.equals(date) &&
                                        booking.time.equals(time)) {
                                    isAvailable = false;
                                    break;
                                }
                            }
                            availability.add(isAvailable);
                        }

                        showAvailableDoctorsDialog(doctorNames, doctorIds, availability, booking);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void showAvailableDoctorsDialog(List<String> names, List<String> ids, List<Boolean> availability, Booking booking) {
        String[] displayList = new String[names.size()];
        for (int i = 0; i < names.size(); i++) {
            String color = availability.get(i) ? "🟢" : "🔴";
            displayList[i] = color + " " + names.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Doctor");

        builder.setItems(displayList, (dialog, which) -> {
            if (!availability.get(which)) {
                Toast.makeText(context, "Doctor is not available at this time", Toast.LENGTH_SHORT).show();
                return;
            }

            String doctorName = names.get(which);
            String doctorId = ids.get(which);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("booked").child(booking.bookingId);

            Map<String, Object> update = new HashMap<>();
            update.put("status", "accepted");
            update.put("doctor_name", doctorName);
            update.put("doctorId", doctorId);

            ref.updateChildren(update).addOnSuccessListener(unused -> {
                sendNotificationToUser(booking.userId, booking, doctorName);
                sendNotificationToDoctor(doctorId, booking);
                Toast.makeText(context, "Doctor assigned successfully!", Toast.LENGTH_SHORT).show();
            });
        });

        builder.show();
    }

    private void sendNotificationToUser(String userId, Booking booking, String doctorName) {
        String msg = "On " + booking.counseling_date + " at " + booking.time +
                ", " + doctorName + " will assist you. Join your assistance portal.";
        sendNotification(userId, msg);
    }

    private void sendNotificationToDoctor(String doctorId, Booking booking) {
        String msg = "You've been assigned a session on " + booking.counseling_date + " at " + booking.time;
        sendNotification(doctorId, msg);
    }

    private void sendNotification(String targetId, String message) {
        DatabaseReference notifRef = FirebaseDatabase.getInstance().getReference("notifications").child(targetId);
        String notifId = notifRef.push().getKey();

        HashMap<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("timestamp", System.currentTimeMillis());

        notifRef.child(notifId).setValue(map);
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView txtInfo;
        Button btnAssign;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            txtInfo = itemView.findViewById(R.id.txtBookingInfo);
            btnAssign = itemView.findViewById(R.id.btnAssignDoctor);
        }
    }
}
