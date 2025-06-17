package com.vrgc.eguidance.Utils;

import androidx.annotation.NonNull;
import com.google.firebase.database.*;

public class FirebaseUtils {

    public interface BadgeCallback {
        void onCountFetched(int count);
    }

    public static void getUnseenNotificationCount(String uid, final BadgeCallback callback) {
        DatabaseReference notifRef = FirebaseDatabase.getInstance().getReference("notifications").child(uid);
        notifRef.orderByChild("seen").equalTo(false).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = (int) snapshot.getChildrenCount();
                callback.onCountFetched(count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
