package com.vrgc.eguidance.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vrgc.eguidance.Model.SupportCardModel;
import com.vrgc.eguidance.R;

import java.util.List;

public class SupportCardAdapter extends RecyclerView.Adapter<SupportCardAdapter.SupportViewHolder> {

    List<SupportCardModel> list;
    Context context;

    public SupportCardAdapter(List<SupportCardModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SupportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SupportViewHolder(LayoutInflater.from(context).inflate(R.layout.item_support_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SupportViewHolder holder, int position) {
        SupportCardModel item = list.get(position);
        holder.title.setText(item.title);
        holder.description.setText(item.description);
        holder.image.setImageResource(item.imageResId);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class SupportViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, description;

        public SupportViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.supportImage);
            title = itemView.findViewById(R.id.supportTitle);
            description = itemView.findViewById(R.id.supportDescription);
        }
    }
}
