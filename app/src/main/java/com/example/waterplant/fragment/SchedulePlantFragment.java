package com.example.waterplant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waterplant.R;
import com.example.waterplant.adapter.SchedulePlantAdapter;
import com.example.waterplant.dataBase.PlantDBHandler;
import com.example.waterplant.dataBase.ScheduleDBHandler;
import com.example.waterplant.model.PlantModel;

import java.util.ArrayList;
import java.util.List;

public class SchedulePlantFragment extends Fragment implements ActionListener<SchedulePlantModel> {
    private View loadingLayout;

    private SchedulePlantAdapter schedulePlantAdapter;
    private final List<SchedulePlantModel> schedulePlantModels = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule_plant, container, false);

        loadingLayout = rootView.findViewById(R.id.loading_layout);

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        schedulePlantAdapter = new SchedulePlantAdapter(schedulePlantModels, (position, item) -> {
            ScheduleFormFragment formFragment = ScheduleFormFragment.newInstance(position, item);
            formFragment.show(getChildFragmentManager(), "scheduleFormFragment");
        });

        recyclerView.setAdapter(schedulePlantAdapter);

        fetchSchedulePlants();

        return rootView;
    }

    private void fetchSchedulePlants() {
        loadingLayout.setVisibility(View.VISIBLE);

        PlantDBHandler dbHandler = new PlantDBHandler(getContext());
        List<PlantModel> plantModels = dbHandler.fetchPlanData(isSuccessful -> loadingLayout.setVisibility(View.GONE));

        ScheduleDBHandler scheduleDBHandler = new ScheduleDBHandler(getContext());
        List<SchedulePlantModel> records = scheduleDBHandler.fetchSchedulePlant(plantModels);
        if (records != null && !records.isEmpty()) schedulePlantModels.addAll(records);

        schedulePlantAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActionPerformed(Action actionId, int position, SchedulePlantModel item) {
        item.setId(String.valueOf(position));

        ScheduleDBHandler scheduleDBHandler = new ScheduleDBHandler(getContext());

        if (Action.SAVE.equals(actionId)) {
            schedulePlantModels.add(position, item);
            scheduleDBHandler.updateSchedulePlant(position, actionId, item, isSuccessful -> {
            });

        } else if (Action.UPDATE.equals(actionId)) {
            schedulePlantModels.set(position, item);
            scheduleDBHandler.updateSchedulePlant(position, actionId, item, isSuccessful -> {
            });

        } else if (Action.REMOVE.equals(actionId)) {
            schedulePlantModels.remove(item);
            scheduleDBHandler.deleteSchedulePlant(item);
        }

        if (schedulePlantAdapter != null) schedulePlantAdapter.notifyDataSetChanged();
    }
}