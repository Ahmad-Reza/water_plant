package com.example.waterplant.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waterplant.R;
import com.example.waterplant.model.PlantModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GardenAdapter extends RecyclerView.Adapter<GardenAdapter.ItemViewHolder> {
    private final List<PlantModel> planModels;
    private final OnItemClickListener listener;

    public GardenAdapter(List<PlantModel> planModels, OnItemClickListener listener) {
        this.planModels = planModels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_garden, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        PlantModel plantModel = planModels.get(position);
        holder.setItem(plantModel);

        if (plantModel != null) {
            Picasso.get().load(plantModel.getImage()).into(holder.imageView);
            holder.nameView.setText(plantModel.getName());
            holder.categoryView.setText(plantModel.getCategory());
        }
    }

    @Override
    public int getItemCount() {
        return planModels.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private PlantModel plant;

        private final ImageView imageView;
        private final TextView nameView;
        private final TextView categoryView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            nameView = itemView.findViewById(R.id.name_view);
            categoryView = itemView.findViewById(R.id.category_view);

            itemView.setOnClickListener(view -> listener.onItemClick(plant));
        }

        public void setItem(PlantModel plant) {
            this.plant = plant;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(PlantModel plantModel);
    }
}
