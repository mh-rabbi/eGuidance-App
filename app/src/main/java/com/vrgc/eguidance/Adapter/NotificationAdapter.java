package com.vrgc.eguidance.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vrgc.eguidance.Model.NotificationModel;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotifHolder> {
    List<NotificationModel> notifList;
    Context context;

    public NotificationAdapter(List<NotificationModel> notifList, Context context) {
        this.notifList = notifList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotifHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotifHolder(LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotifHolder holder, int position) {
        holder.txt.setText(notifList.get(position).message);
    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }

    static class NotifHolder extends RecyclerView.ViewHolder {
        TextView txt;
        public NotifHolder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(android.R.id.text1);
        }
    }
}
