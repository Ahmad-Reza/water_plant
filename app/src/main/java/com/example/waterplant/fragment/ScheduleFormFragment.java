package com.example.waterplant.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.example.waterplant.R;
import com.example.waterplant.model.PlantModel;
import com.example.waterplant.utilities.ScheduleNotificationReceiver;
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
    private static final String ITEM_POSITION = "POSITION";
    private static final String SCHEDULE_ITEM = "SCHEDULE_ITEM";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d MMM 'at' hh:mm a");

    private PlantModel plantModel;
    private SchedulePlantModel schedulePlantModel;

    private int position;
    private LocalDateTime scheduleTime;
    private ActionListener<SchedulePlantModel> actionListener;

    public ScheduleFormFragment() { }

    public static ScheduleFormFragment newInstance(int position, Object item) {
        ScheduleFormFragment fragment = new ScheduleFormFragment();
        Bundle args = new Bundle();
        args.putInt(ITEM_POSITION, position);
        args.putParcelable(SCHEDULE_ITEM, (Parcelable) item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ITEM_POSITION);
            Parcelable parcelable = getArguments().getParcelable(SCHEDULE_ITEM);
            if (parcelable instanceof PlantModel) {
                plantModel = (PlantModel) parcelable;
            } else if (parcelable instanceof SchedulePlantModel) {
                schedulePlantModel = (SchedulePlantModel) parcelable;
            }
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

            } else if (itemId == R.id.remove_button) {
                dismiss();
                actionListener.onActionPerformed(ActionListener.Action.REMOVE, position, schedulePlantModel);

                return true;
            }
            return false;
        });

        if (getContext() != null) {
            toolbar.getMenu().findItem(R.id.close_button).setIconTintList(AppCompatResources.getColorStateList(getContext(), R.color.white));
            toolbar.getMenu().findItem(R.id.remove_button).setIconTintList(AppCompatResources.getColorStateList(getContext(), R.color.white));
        }

        TextInputLayout messageLayout = rootView.findViewById(R.id.message_layout);
        TextInputEditText messageInput = rootView.findViewById(R.id.message_input);

        TextInputLayout dateTimeLayout = rootView.findViewById(R.id.schedule_time_layout);
        TextInputEditText scheduleTimeInput = rootView.findViewById(R.id.schedule_time_input);

        LocalTime currentTime = LocalTime.now();

        MaterialTimePicker timePickerDialog = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTitleText(R.string.time_picker_title)
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setHour(currentTime.getHour())
                .setMinute(currentTime.getMinute())
                .build();

        timePickerDialog.addOnPositiveButtonClickListener(v -> {
            int selectedHour = timePickerDialog.getHour();
            int selectedMin = timePickerDialog.getMinute();

            scheduleTime = scheduleTime.withHour(selectedHour).withMinute(selectedMin);
            scheduleTimeInput.setText(formatDateTime(scheduleTime, getResources()));
        });

        MaterialDatePicker<Long> datePickerDialog = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.date_picker_title)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePickerDialog.addOnPositiveButtonClickListener(selection -> {
            if (selection != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(selection);

                scheduleTime = calendar.getTime()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                timePickerDialog.show(ScheduleFormFragment.this.getChildFragmentManager(), "MATERIAL_TIME_PICKER");
            }
        });

        scheduleTimeInput.setOnClickListener(view -> {
            if (!datePickerDialog.isAdded()) {
                datePickerDialog.show(ScheduleFormFragment.this.getChildFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        ExtendedFloatingActionButton scheduleBtn = rootView.findViewById(R.id.schedule_button);
        scheduleBtn.setOnClickListener(view -> {
            String inputMessage = Objects.requireNonNull(messageInput.getText()).toString();
            String inputTime = Objects.requireNonNull(scheduleTimeInput.getText()).toString();

            if (inputMessage.isEmpty()) {
                messageLayout.setError(getString(R.string.require_field_error));

            } else if (inputTime.isEmpty()) {
                dateTimeLayout.setError(getString(R.string.require_field_error));

            } else {
                ActionListener.Action action;
                if (schedulePlantModel != null) {
                    action = ActionListener.Action.UPDATE;
                    plantModel = schedulePlantModel.getPlantModel();
                    scheduleTime = schedulePlantModel.getLocalDateTime();

                } else action = ActionListener.Action.SAVE;

                SchedulePlantModel updatedSchedulePlant = new SchedulePlantModel(plantModel, messageInput.getText().toString(), scheduleTime);

                dismiss();
                actionListener.onActionPerformed(action, position, updatedSchedulePlant);
            }
        });

        if (schedulePlantModel != null) {
            messageInput.setText(schedulePlantModel.getMessage());
            scheduleTimeInput.setText(schedulePlantModel.getLocalDateTime().toString());

        } else {
            toolbar.getMenu().findItem(R.id.remove_button).setVisible(false);
        }

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            actionListener = (ActionListener) parentFragment;
        } else {
            actionListener = (ActionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        actionListener = null;
    }

    @Nullable
    public static String formatDateTime(LocalDateTime scheduleTime, Resources resources) {
        if (scheduleTime != null) {
            StringBuilder selectedTime = new StringBuilder(scheduleTime.format(DATE_TIME_FORMATTER));

            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);
            LocalDate yesterday = today.minusDays(2);
            LocalDate scheduleDate = scheduleTime.toLocalDate();

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