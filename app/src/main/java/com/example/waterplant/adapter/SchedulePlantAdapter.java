package com.example.waterplant.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waterplant.R;

import com.squareup.picasso.Picasso;
import com.example.waterplant.fragment.SchedulePlantModel;

import java.util.List;

public class SchedulePlantAdapter extends RecyclerView.Adapter<SchedulePlantAdapter.SchedulePlantViewHolder> {
    private final List<SchedulePlantModel> schedulePlantModels;
    private final ItemClickListener<SchedulePlantModel> actionListener;

    public SchedulePlantAdapter(List<SchedulePlantModel> schedulePlantModels, ItemClickListener<SchedulePlantModel> actionListener) {
        this.schedulePlantModels = schedulePlantModels;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public SchedulePlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_plant, parent, false);

        return new SchedulePlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SchedulePlantViewHolder holder, int position) {
        SchedulePlantModel schedulePlantModel = schedulePlantModels.get(position);
        holder.setItem(schedulePlantModel);

        if (schedulePlantModel != null) {
            holder.plantName.setText(schedulePlantModel.getPlantModel().getName());
            holder.plantMessage.setText(schedulePlantModel.getMessage());
            Picasso.get().load(schedulePlantModel.getPlantModel().getImage()).into(holder.plantImage);
            holder.scheduleTime.setText(schedulePlantModel.getLocalDateTime().toString());
        }
    }

    @Override
    public int getItemCount() {
        return schedulePlantModels.size();
    }

    public class SchedulePlantViewHolder extends RecyclerView.ViewHolder {
        private SchedulePlantModel schedulePlantModel;

        private final TextView plantName;
        private final TextView plantMessage;
        private final ImageView plantImage;
        private final TextView scheduleTime;

        public SchedulePlantViewHolder(@NonNull View itemView) {
            super(itemView);

            plantName = itemView.findViewById(R.id.plant_name_view);
            plantMessage = itemView.findViewById(R.id.plant_message_view);
            plantImage = itemView.findViewById(R.id.plant_image_view);
            scheduleTime = itemView.findViewById(R.id.schedule_time_view);

            itemView.setOnClickListener(view -> actionListener.onItemClick(getLayoutPosition(), schedulePlantModel));
        }

        public void setItem(SchedulePlantModel schedulePlantModel) {
            this.schedulePlantModel = schedulePlantModel;
        }
    }
}
