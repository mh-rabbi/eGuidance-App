package com.vrgc.eguidance.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vrgc.eguidance.Activity.Doctor.DoctorChatActivity;

import java.util.List;

public class DoctorAnonAdapter extends RecyclerView.Adapter<DoctorAnonAdapter.Holder> {

    List<String> anonNames, anonKeys;
    Context context;

    public DoctorAnonAdapter(List<String> anonNames, List<String> anonKeys, Context context) {
        this.anonNames = anonNames;
        this.anonKeys = anonKeys;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.txt.setText(anonNames.get(position));
        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, DoctorChatActivity.class);
            i.putExtra("anonymousId", anonKeys.get(position));
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return anonNames.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView txt;
        public Holder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(android.R.id.text1);
        }
    }
}
