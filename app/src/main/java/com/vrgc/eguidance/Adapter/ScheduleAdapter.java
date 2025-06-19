package com.vrgc.eguidance.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vrgc.eguidance.Model.ScheduleModel;
import com.vrgc.eguidance.R;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    List<ScheduleModel> scheduleList;
    Context context;

    public ScheduleAdapter(List<ScheduleModel> scheduleList, Context context) {
        this.scheduleList = scheduleList;
        this.context = context;
    }

    @NonNull
    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_schedule_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.ViewHolder holder, int position) {
        ScheduleModel s = scheduleList.get(position);
        holder.date.setText(s.date);
        holder.time.setText(s.time);
        holder.user.setText(s.userName);
        holder.condition.setText(s.condition);
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, time, user, condition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.scheduleDate);
            time = itemView.findViewById(R.id.scheduleTime);
            user = itemView.findViewById(R.id.scheduleUser);
            condition = itemView.findViewById(R.id.scheduleCondition);
        }
    }
}
