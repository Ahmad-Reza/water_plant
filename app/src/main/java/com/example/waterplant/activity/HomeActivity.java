package com.example.waterplant.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.waterplant.R;
import com.example.waterplant.fragment.AddPlantBottomSheetFragment;
import com.example.waterplant.fragment.MyGardenFragment;
import com.example.waterplant.fragment.SchedulePlantFragment;
import com.example.waterplant.model.PlantModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {
    private static final int ADD_PLANT_REQUEST = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MaterialToolbar toolbar = findViewById(R.id.material_toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.about_item) {

                return true;

            } else if (itemId == R.id.logout_item) {

                return true;
            }

            return false;
        });

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        AppBarLayout appBarLayout = findViewById(R.id.appbar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }

                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getString(R.string.my_plant_label));

                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

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
        FloatingActionButton addPlantBtn = findViewById(R.id.add_plant_button);
        addPlantBtn.setOnClickListener(view -> {
            BottomSheetDialog sheetDialog = new BottomSheetDialog(this);
            final View sheetLayout = LayoutInflater.from(this).inflate(R.layout.choose_plant_from_sheet, findViewById(R.id.root_layout));
            ImageView camera = sheetLayout.findViewById(R.id.camera_view);
            camera.setOnClickListener(view1 -> {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, ADD_PLANT_REQUEST);

                sheetDialog.dismiss();
            });

            ImageView gallery = sheetLayout.findViewById(R.id.gallery_view);
            gallery.setOnClickListener(view1 -> {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/png");
                startActivityForResult(intent, ADD_PLANT_REQUEST);

                sheetDialog.dismiss();
            });

            sheetDialog.setContentView(sheetLayout);
            sheetDialog.show();
        });
    }
    }

    private void updateToolbarIcon(MaterialToolbar toolbar, boolean isShowing) {
        MenuItem aboutItem = toolbar.getMenu().findItem(R.id.about_item);
        MenuItem logoutItem = toolbar.getMenu().findItem(R.id.logout_item);

        if (!isShowing) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                aboutItem.setIconTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.black));
                logoutItem.setIconTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.black));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                aboutItem.setIconTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.white));
                logoutItem.setIconTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.white));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PLANT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Uri plantUri = data != null ? data.getData() : null;

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                final View dialogView = getLayoutInflater().inflate(R.layout.plant_property_dialog, null);
                dialogBuilder.setTitle("Plant Details");
                dialogBuilder.setView(dialogView);

                ImageView plantView = dialogView.findViewById(R.id.plant_view);
                Picasso.get().load(plantUri).into(plantView);

                EditText nameView = dialogView.findViewById(R.id.name_input);
                EditText categoryView = dialogView.findViewById(R.id.category_input);

                AlertDialog alertDialog = dialogBuilder.create();
                dialogBuilder.setPositiveButton("Save", ((dialogInterface, i) -> {
                    String name = nameView.getText().toString();
                    String category = categoryView.getText().toString();

                    if (plantUri != null) {
                        PlantModel plant = new PlantModel(plantUri, name, category);
                        updatePlant(plant);

                    } else {
                        Toast.makeText(getApplicationContext(), "Please, select Correct Image", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                }));

                dialogBuilder.setNegativeButton("Cancel", ((dialogInterface, i) -> alertDialog.dismiss()));

                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        }
    }

    private void updatePlant(PlantModel plant) {

        // TODO update plant data into database

    }
}