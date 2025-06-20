package com.vrgc.eguidance.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vrgc.eguidance.Model.ServiceCardModel;
import com.vrgc.eguidance.R;

import java.util.List;

public class ServiceCardAdapter extends RecyclerView.Adapter<ServiceCardAdapter.ServiceViewHolder> {

    List<ServiceCardModel> list;
    Context context;

    public ServiceCardAdapter(List<ServiceCardModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceViewHolder(LayoutInflater.from(context).inflate(R.layout.item_service_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceCardModel item = list.get(position);
        holder.title.setText(item.title);
        holder.description.setText(item.description);
        holder.image.setImageResource(item.imageResId);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, description;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.serviceImage);
            title = itemView.findViewById(R.id.serviceTitle);
            description = itemView.findViewById(R.id.serviceDescription);
        }
    }
}
