package com.example.waterplant.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.waterplant.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

public class AddPlantBottomSheetFragment extends BottomSheetDialogFragment {
    private static final int ADD_PLANT_REQUEST = 1000;

    private SheetItemClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_plant_bottom_sheet, container, false);

        ImageView camera = view.findViewById(R.id.camera_view);
        camera.setOnClickListener(view1 -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, ADD_PLANT_REQUEST);
        });


        ImageView gallery = view.findViewById(R.id.gallery_view);
        gallery.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/png");
            startActivityForResult(intent, ADD_PLANT_REQUEST);
        });

        return view;
    }

    /*@Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            listener = (SheetItemClickListener) parentFragment;
        } else {
            listener = (SheetItemClickListener) context;
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PLANT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Uri plantUri = data.getData();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                final View dialogView = getLayoutInflater().inflate(R.layout.plant_property_dialog, null);
                alertDialog.setView(dialogView);

                ImageView plantView = dialogView.findViewById(R.id.plant_view);
                Picasso.get().load(plantUri).into(plantView);

                alertDialog.setPositiveButton("Save", ((dialogInterface, i) -> {

                }));

                alertDialog.setNegativeButton("Cancel", ((dialogInterface, i) -> {

                }));

                AlertDialog alert = alertDialog.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();
            }
        }
    }

    public interface SheetItemClickListener {
        void onSheetItemClick();
    }
}
