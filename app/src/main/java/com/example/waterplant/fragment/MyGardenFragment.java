package com.example.waterplant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waterplant.R;
import com.example.waterplant.adapter.ActionListener;
import com.example.waterplant.adapter.GardenAdapter;
import com.example.waterplant.dataBase.PlantDBHandler;
import com.example.waterplant.model.PlantModel;

import java.util.List;

public class MyGardenFragment extends Fragment implements ActionListener<Object> {
    private static final String SCHEDULE_FORM_FRAGMENT = "SCHEDULE_FORM_FRAGMENT";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_garden, container, false);

        View loadingLayout = rootView.findViewById(R.id.loading_layout);
        loadingLayout.setVisibility(View.VISIBLE);

        PlantDBHandler dbHandler = new PlantDBHandler(getContext());
        List<PlantModel> plantModels = dbHandler.fetchPlanData(isSuccessful -> loadingLayout.setVisibility(View.GONE));

        RecyclerView recycler = rootView.findViewById(R.id.garden_recycler);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        GardenAdapter adapter = new GardenAdapter(plantModels, this);
        recycler.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onActionPerformed(Object item) {
        if (item instanceof PlantModel) {
            PlantModel plantModel = (PlantModel) item;
            ScheduleFormFragment formFragment = ScheduleFormFragment.newInstance(plantModel);
            formFragment.show(getChildFragmentManager(), SCHEDULE_FORM_FRAGMENT);

        } else if (item instanceof SchedulePlantModel) {
            SchedulePlantModel schedulePlantModel = (SchedulePlantModel) item;
        }
    }
}