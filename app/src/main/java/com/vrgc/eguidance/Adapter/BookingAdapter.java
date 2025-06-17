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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vrgc.eguidance.Model.Booking;
import com.vrgc.eguidance.R;

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
                // Simple dialog to assign doctor
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
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Assign Doctor");

        String[] doctors = {"Dr. Ayesha", "Dr. Kamal", "Dr. Shila"};
        builder.setItems(doctors, (dialog, which) -> {
            String doctorName = doctors[which];
            String doctorId = "doc_" + (which + 1);

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
