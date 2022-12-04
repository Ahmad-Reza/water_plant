package com.example.waterplant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waterplant.R;
import com.example.waterplant.adapter.GardenAdapter;
import com.example.waterplant.dataBase.PlantDBHandler;
import com.example.waterplant.model.PlantModel;

import java.util.List;

public class MyGardenFragment extends Fragment implements ActionListener<SchedulePlantModel> {
    private ActionListener<SchedulePlantModel> actionListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_garden, container, false);

        View loadingLayout = rootView.findViewById(R.id.loading_layout);
        loadingLayout.setVisibility(View.VISIBLE);

        PlantDBHandler dbHandler = new PlantDBHandler(getContext());
        List<PlantModel> plantModels = dbHandler.fetchPlanData(isSuccessful -> loadingLayout.setVisibility(View.GONE));

        RecyclerView recycler = rootView.findViewById(R.id.garden_recycler);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        GardenAdapter adapter = new GardenAdapter(plantModels, (position, item) -> {
            ScheduleFormFragment formFragment = ScheduleFormFragment.newInstance(position, item);
            formFragment.show(getChildFragmentManager(), "scheduleFormFragment");
        });

        recycler.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onActionPerformed(Action actionId, int position, SchedulePlantModel item) {
        actionListener.onActionPerformed(actionId, position, item);
    }

    public void addActionListener(ActionListener<SchedulePlantModel> actionListener) {
        this.actionListener = actionListener;
    }
}