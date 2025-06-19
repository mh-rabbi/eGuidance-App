package com.vrgc.eguidance.Activity.Doctor;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class DoctorChatActivity extends AppCompatActivity {


    private RecyclerView chatRecyclerView;
    private EditText messageInput;
    private ImageButton sendButton;
    private TextView participantNameText;

    private final List<MessageModel> messageList = new ArrayList<>();
    private ChatAdapter adapter;

    private String anonymousId, doctorId, doctorName;
    private DatabaseReference chatRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_chat);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        participantNameText = findViewById(R.id.participantNameText);


        anonymousId = getIntent().getStringExtra("anonymousId");
        chatRef = FirebaseDatabase.getInstance().getReference("free_chat").child(anonymousId);

        participantNameText.setText("Chatting with Anonymous");

        adapter = new ChatAdapter(messageList, "doctor");
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(adapter);

        loadMessages();

        sendButton.setOnClickListener(v -> sendMessage());
    }
    private void loadMessages() {
        chatRef.child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    MessageModel msg = snap.getValue(MessageModel.class);
                    messageList.add(msg);
                }
                adapter.notifyDataSetChanged();
                chatRecyclerView.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DoctorChatActivity.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String text = messageInput.getText().toString().trim();
        if (text.isEmpty()) return;

        DatabaseReference msgRef = chatRef.child("messages").push();
        MessageModel message = new MessageModel("doctor", text, System.currentTimeMillis());
        msgRef.setValue(message);
        messageInput.setText("");
    }



}