package com.vrgc.eguidance.Adapter;


import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vrgc.eguidance.Model.MessageModel;
import com.vrgc.eguidance.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private final List<MessageModel> messageList;
    private final String currentSender; // "user" or "doctor"

    public ChatAdapter(List<MessageModel> messageList, String currentSender) {
        this.messageList = messageList;
        this.currentSender = currentSender;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).sender.equals(currentSender)) {
            return 1; // Right (sender)
        } else {
            return 0; // Left (receiver)
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_right, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_left, parent, false);
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        MessageModel msg = messageList.get(position);
        holder.msgText.setText(msg.text);

        String time = DateFormat.format("hh:mm a", msg.timestamp).toString();
        holder.timeText.setText(time);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView msgText, timeText;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            msgText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.messageTime);
        }
    }
}

