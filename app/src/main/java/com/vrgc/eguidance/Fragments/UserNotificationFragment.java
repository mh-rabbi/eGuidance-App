package com.vrgc.eguidance.Fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vrgc.eguidance.Adapter.NotificationAdapter;
import com.vrgc.eguidance.Model.NotificationModel;
import com.vrgc.eguidance.R;

import java.util.ArrayList;
import java.util.List;

public class UserNotificationFragment extends Fragment {

    RecyclerView recyclerView;
    List<NotificationModel> notifList = new ArrayList<>();
    NotificationAdapter adapter;
    DatabaseReference notifRef;
    FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_notifications, container, false);

        recyclerView = view.findViewById(R.id.notificationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        notifRef = FirebaseDatabase.getInstance().getReference("notifications").child(uid);

        adapter = new NotificationAdapter(notifList, getContext());
        recyclerView.setAdapter(adapter);

        notifRef.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notifList.clear();
                String latestMessage = null;

                for (DataSnapshot snap : snapshot.getChildren()) {
                    NotificationModel n = snap.getValue(NotificationModel.class);
                    notifList.add(0, n); // newest on top

                    // Save the latest unread message
                    if (latestMessage == null) {
                        latestMessage = n.message;
                    }
                }
                adapter.notifyDataSetChanged();

                //Mark Notification as Seen
                notifRef.get().addOnSuccessListener(seenSnap -> {
                    for (DataSnapshot snap : seenSnap.getChildren()) {
                        snap.getRef().child("seen").setValue(true);
                    }
                });


                // Show a floating bubble-style message
                if (getActivity() != null && latestMessage != null) {
                    Toast toast = Toast.makeText(getActivity(), latestMessage, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.END, 50, 50); // bottom-right corner
                    toast.show();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        return view;
    }
}
