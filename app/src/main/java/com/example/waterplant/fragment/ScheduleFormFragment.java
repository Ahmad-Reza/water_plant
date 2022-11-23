package com.example.waterplant.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.example.waterplant.R;
import com.example.waterplant.adapter.ActionListener;
import com.example.waterplant.model.PlantModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFormFragment extends BottomSheetDialogFragment {
    private static final String PLANT_MODEL = "PLANT_MODEL";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d MMM 'at' hh:mm a");

    private PlantModel plantModel;
    private LocalDateTime selectedDateTime;
    private ActionListener<SchedulePlantModel> listener;

    public ScheduleFormFragment() { }

    public static ScheduleFormFragment newInstance(PlantModel plantModel) {
        ScheduleFormFragment fragment = new ScheduleFormFragment();
        Bundle args = new Bundle();
        args.putParcelable(PLANT_MODEL, plantModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            plantModel = getArguments().getParcelable(PLANT_MODEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule_form, container, false);

        MaterialToolbar toolbar = rootView.findViewById(R.id.material_toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.close_button) {
                dismiss();
                return true;
            }
            return false;
        });

        if (getContext() != null) {
            toolbar.getMenu().findItem(R.id.close_button).setIconTintList(AppCompatResources.getColorStateList(getContext(), R.color.white));
        }

        TextInputLayout messageLayout = rootView.findViewById(R.id.message_layout);
        TextInputEditText messageInput = rootView.findViewById(R.id.message_input);

        TextInputLayout dateTimeLayout = rootView.findViewById(R.id.date_time_layout);
        TextInputEditText dateTimeInput = rootView.findViewById(R.id.date_time_input);

        LocalTime currentTime = LocalTime.now();

        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTitleText(R.string.time_picker_title)
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setHour(currentTime.getHour())
                .setMinute(currentTime.getMinute())
                .build();

        materialTimePicker.addOnPositiveButtonClickListener(v -> {
            int selectedHour = materialTimePicker.getHour();
            int selectedMin = materialTimePicker.getMinute();

            selectedDateTime = selectedDateTime
                    .withHour(selectedHour)
                    .withMinute(selectedMin);

            dateTimeInput.setText(formatDateTime(selectedDateTime, getResources()));
        });

        MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.date_picker_title)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            if (selection != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(selection);

                selectedDateTime = calendar.getTime()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                materialTimePicker.show(ScheduleFormFragment.this.getChildFragmentManager(), "MATERIAL_TIME_PICKER");
            }
        });

        dateTimeInput.setOnClickListener(view -> {
            if (!materialDatePicker.isAdded()) {
                materialDatePicker.show(ScheduleFormFragment.this.getChildFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        ExtendedFloatingActionButton scheduleBtn = rootView.findViewById(R.id.schedule_button);
        scheduleBtn.setOnClickListener(view -> {
            String message = Objects.requireNonNull(messageInput.getText()).toString();
            String dateTime = Objects.requireNonNull(dateTimeInput.getText()).toString();

            if (message.isEmpty()) {
                messageLayout.setError(getString(R.string.require_field_error));

            } else if (dateTime.isEmpty()) {
                dateTimeLayout.setError(getString(R.string.require_field_error));

            } else {
                SchedulePlantModel schedulePlantModel = new SchedulePlantModel(plantModel, messageInput.getText().toString(), selectedDateTime);
                listener.onActionPerformed(schedulePlantModel);
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        listener = (ActionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Nullable
    public static String formatDateTime(LocalDateTime dateTime, Resources resources) {
        if (dateTime != null) {
            StringBuilder selectedTime = new StringBuilder(dateTime.format(DATE_TIME_FORMATTER));

            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);
            LocalDate yesterday = today.minusDays(2);
            LocalDate scheduleDate = dateTime.toLocalDate();

            if (yesterday.equals(scheduleDate)) {
                selectedTime = selectedTime.replace(0, selectedTime.indexOf(" at"), resources.getString(R.string.yesterday_label));

            } else if (today.equals(scheduleDate)) {
                selectedTime = selectedTime.replace(0, selectedTime.indexOf(" at"), resources.getString(R.string.today_label));

            } else if (tomorrow.equals(scheduleDate)) {
                selectedTime = selectedTime.replace(0, selectedTime.indexOf(" at"), resources.getString(R.string.tomorrow_label));
            }

            return String.valueOf(selectedTime);
        } else return null;
    }
}