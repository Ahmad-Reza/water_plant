package com.example.waterplant.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.waterplant.R;
import com.example.waterplant.fragment.MyGardenFragment;
import com.example.waterplant.fragment.SchedulePlantFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView gardenTab = findViewById(R.id.garden_tab_view);
        TextView scheduleTab = findViewById(R.id.schedule_tab_view);

        View.OnClickListener listener = view -> {
            if (view.getId() == R.id.garden_tab_view) {
                gardenTab.setBackground(AppCompatResources.getDrawable(this, R.drawable.tab_select));
                gardenTab.setTextColor(AppCompatResources.getColorStateList(this, R.color.white));

                scheduleTab.setBackground(AppCompatResources.getDrawable(this, R.drawable.tab_deselect));
                scheduleTab.setTextColor(AppCompatResources.getColorStateList(this, R.color.textColor));

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_view, new MyGardenFragment()).addToBackStack(null).commit();

            } else if (view.getId() == R.id.schedule_tab_view) {
                scheduleTab.setBackground(AppCompatResources.getDrawable(this, R.drawable.tab_select));
                scheduleTab.setTextColor(AppCompatResources.getColorStateList(this, R.color.white));

                gardenTab.setBackground(AppCompatResources.getDrawable(this, R.drawable.tab_deselect));
                gardenTab.setTextColor(AppCompatResources.getColorStateList(this, R.color.textColor));

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_view, new SchedulePlantFragment()).addToBackStack(null).commit();
            }
        };

        gardenTab.setOnClickListener(listener);
        scheduleTab.setOnClickListener(listener);
    }
}