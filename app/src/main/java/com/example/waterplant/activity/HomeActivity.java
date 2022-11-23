package com.example.waterplant.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.waterplant.R;
import com.example.waterplant.adapter.ActionListener;
import com.example.waterplant.adapter.ViewPagerAdapter;
import com.example.waterplant.dataBase.PlantDBHandler;
import com.example.waterplant.fragment.MyGardenFragment;
import com.example.waterplant.fragment.SchedulePlantFragment;
import com.example.waterplant.fragment.SchedulePlantModel;
import com.example.waterplant.model.PlantModel;
import com.example.waterplant.utilities.ImageUtility;
import com.example.waterplant.utilities.ResourceUtility;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements ActionListener<SchedulePlantModel> {

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
                ResourceUtility.clearPreferences(this);
                Intent intent = new Intent(HomeActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();

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

        ViewPager2 viewPager = findViewById(R.id.viewpager_view);
        viewPager.setAdapter(new ViewPagerAdapter(this, getFragments()));

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    gardenTab.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.tab_select));
                    gardenTab.setTextColor(AppCompatResources.getColorStateList(getApplicationContext(), R.color.white));

                    scheduleTab.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.tab_deselected));
                    scheduleTab.setTextColor(AppCompatResources.getColorStateList(getApplicationContext(), R.color.textColor));

                } else if (position == 1){
                    scheduleTab.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.tab_select));
                    scheduleTab.setTextColor(AppCompatResources.getColorStateList(getApplicationContext(), R.color.white));

                    gardenTab.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.tab_deselected));
                    gardenTab.setTextColor(AppCompatResources.getColorStateList(getApplicationContext(), R.color.textColor));
                }
            }
        });

        View.OnClickListener listener = view -> {
            if (view.getId() == R.id.garden_tab_view) {
                gardenTab.setBackground(AppCompatResources.getDrawable(this, R.drawable.tab_select));
                gardenTab.setTextColor(AppCompatResources.getColorStateList(this, R.color.white));

                scheduleTab.setBackground(AppCompatResources.getDrawable(this, R.drawable.round_corners));
                scheduleTab.setTextColor(AppCompatResources.getColorStateList(this, R.color.textColor));

                viewPager.setCurrentItem(0, true);

            } else if (view.getId() == R.id.schedule_tab_view) {
                scheduleTab.setBackground(AppCompatResources.getDrawable(this, R.drawable.tab_select));
                scheduleTab.setTextColor(AppCompatResources.getColorStateList(this, R.color.white));

                gardenTab.setBackground(AppCompatResources.getDrawable(this, R.drawable.round_corners));
                gardenTab.setTextColor(AppCompatResources.getColorStateList(this, R.color.textColor));
                viewPager.setCurrentItem(1, true);
            }
        };

        gardenTab.setOnClickListener(listener);
        scheduleTab.setOnClickListener(listener);

        ActivityResultLauncher<Intent> galleryActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Uri plantUri = result.getData() != null ? result.getData().getData() : null;
            plantModelDialog(viewPager, plantUri);
        });

        ActivityResultLauncher<Intent> cameraActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Bitmap imageBitmap = result.getData() != null ? (Bitmap) result.getData().getExtras().get("data") : null;

            ImageUtility imageUtility = new ImageUtility(getApplicationContext());
            Uri plantUri = imageBitmap != null ? imageUtility.getUriFromBitmap(imageBitmap) : null;

            plantModelDialog(viewPager, plantUri);
        });

        FloatingActionButton addPlantBtn = findViewById(R.id.add_plant_button);
        addPlantBtn.setOnClickListener(view -> {
            BottomSheetDialog sheetDialog = new BottomSheetDialog(this);
            final View sheetLayout = LayoutInflater.from(this).inflate(R.layout.choose_plant_from_sheet, findViewById(R.id.root_layout));
            ImageView camera = sheetLayout.findViewById(R.id.camera_view);
            camera.setOnClickListener(view1 -> {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraActivityLauncher.launch(intent);

                sheetDialog.dismiss();
            });

            ImageView gallery = sheetLayout.findViewById(R.id.gallery_view);
            gallery.setOnClickListener(view1 -> {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/png");
                galleryActivityLauncher.launch(intent);

                sheetDialog.dismiss();
            });

            sheetDialog.setContentView(sheetLayout);
            sheetDialog.show();
        });
    }

    private void plantModelDialog(ViewPager2 viewPager, Uri plantUri) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View dialogView = getLayoutInflater().inflate(R.layout.plant_property_dialog, null);
        dialogBuilder.setTitle("Plant Details");
        dialogBuilder.setView(dialogView);

        ImageView plantView = dialogView.findViewById(R.id.plant_view);
        Picasso.get().load(plantUri).into(plantView);

        EditText nameView = dialogView.findViewById(R.id.name_input);
        EditText categoryView = dialogView.findViewById(R.id.category_input);

        AlertDialog alertDialog = dialogBuilder.create();

        Button saveButton = dialogView.findViewById(R.id.save_button);
        saveButton.setOnClickListener(view -> {
            String name = nameView.getText().toString();
            String category = categoryView.getText().toString();

            if (plantUri != null) {
                updatePlant(new PlantModel(plantUri, name, category));
                viewPager.setAdapter(new ViewPagerAdapter(this, getFragments()));

            } else {
                Toast.makeText(getApplicationContext(), "Please, select Correct Image", Toast.LENGTH_SHORT).show();
            }

            alertDialog.dismiss();
        });

        Button cancelButton = dialogView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(view -> alertDialog.dismiss());

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MyGardenFragment());
        fragments.add(new SchedulePlantFragment());

        return fragments;
    }

    private void updatePlant(PlantModel plant) {
        PlantDBHandler plantDB = new PlantDBHandler(getApplicationContext());
        ImageUtility imageUtility = new ImageUtility(getApplicationContext());

        byte[] byteFromUri = imageUtility.getByteFromUri(plant.getImage());
        if (byteFromUri != null) {
            plantDB.createPlantData(byteFromUri, plant.getName(), plant.getCategory());
        }
    }

    @Override
    public void onActionPerformed(SchedulePlantModel schedulePlantModel) {
        for (Fragment fragment : getFragments()) {
            if (fragment instanceof ActionListener)
                ((ActionListener) fragment).onActionPerformed(schedulePlantModel);
        }

        Toast.makeText(getApplicationContext(), "Plant has been scheduled successfully", Toast.LENGTH_SHORT).show();
    }
}