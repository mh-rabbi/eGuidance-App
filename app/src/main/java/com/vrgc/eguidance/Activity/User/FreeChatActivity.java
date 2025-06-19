package com.vrgc.eguidance.Activity.User;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vrgc.eguidance.Adapter.ChatAdapter;
import com.vrgc.eguidance.Model.MessageModel;
import com.vrgc.eguidance.R;

import java.util.ArrayList;
import java.util.List;

public class FreeChatActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private EditText messageInput;
    private ImageButton sendButton;
    private TextView doctorNameText;

    private List<MessageModel> messageList = new ArrayList<>();
    private ChatAdapter adapter;

    private String anonymousId, doctorId, doctorName;
    private DatabaseReference chatRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_chat);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        doctorNameText = findViewById(R.id.doctorNameText);

        anonymousId = "anonymous_" + FirebaseAuth.getInstance().getUid();
        chatRef = FirebaseDatabase.getInstance().getReference("free_chat").child(anonymousId);

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(messageList, "user");
        chatRecyclerView.setAdapter(adapter);

        loadDoctorAndMessages();

        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void loadDoctorAndMessages() {
        chatRef.child("doctorId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    doctorId = snapshot.getValue(String.class);
                    chatRef.child("doctorName").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override public void onDataChange(@NonNull DataSnapshot dSnap) {
                            doctorName = dSnap.getValue(String.class);
                            doctorNameText.setText(doctorName);
                        }
                        @Override public void onCancelled(@NonNull DatabaseError error) {}
                    });
                    loadMessages();
                } else {
                    assignDoctor();
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void assignDoctor() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        userRef.orderByChild("role").equalTo("Doctor").limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            doctorId = snap.getKey();
                            doctorName = snap.child("name").getValue(String.class);
                            chatRef.child("doctorId").setValue(doctorId);
                            chatRef.child("doctorName").setValue(doctorName);
                            doctorNameText.setText(doctorName);
                            break;
                        }
                        loadMessages();
                    }
                    @Override public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void loadMessages() {
        chatRef.child("messages").addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    MessageModel msg = snap.getValue(MessageModel.class);
                    messageList.add(msg);
                }
                adapter.notifyDataSetChanged();
                chatRecyclerView.scrollToPosition(messageList.size() - 1);
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void sendMessage() {
        String text = messageInput.getText().toString().trim();
        if (text.isEmpty()) return;

        DatabaseReference msgRef = chatRef.child("messages").push();
        MessageModel message = new MessageModel("user", text, System.currentTimeMillis());
        msgRef.setValue(message);
        messageInput.setText("");
    }
}
