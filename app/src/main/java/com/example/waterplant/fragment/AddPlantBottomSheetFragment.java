package com.example.waterplant.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.waterplant.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddPlantBottomSheetFragment extends BottomSheetDialogFragment {
    public static final String SELECTED_CAMERA = "SELECTED_CAMERA";
    public static final String SELECTED_GALLERY = "SELECTED_GALLERY";

    private SheetItemClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_plant_bottom_sheet, container, false);

        ImageView camera = view.findViewById(R.id.camera_view);
        camera.setOnClickListener(view1 -> {
            listener.onSheetItemClick(SELECTED_CAMERA);
            dismiss();
        });

        ImageView gallery = view.findViewById(R.id.gallery_view);
        gallery.setOnClickListener(view1 -> {
            listener.onSheetItemClick(SELECTED_CAMERA);
            dismiss();
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            listener = (SheetItemClickListener) parentFragment;
        } else {
            listener = (SheetItemClickListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
    }

    public interface SheetItemClickListener {
        void onSheetItemClick(String selectedItem);
    }
}
