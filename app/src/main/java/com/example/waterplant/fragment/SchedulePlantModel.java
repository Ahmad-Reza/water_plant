package com.example.waterplant.fragment;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.waterplant.model.PlantModel;

import java.time.LocalDateTime;

public class SchedulePlantModel implements Parcelable {
    private PlantModel plantModel;
    private String message;
    private LocalDateTime localDateTime;

    public SchedulePlantModel (PlantModel plantModel, String message, LocalDateTime localDateTime) {
        this.plantModel = plantModel;
        this.message = message;
        this.localDateTime = localDateTime;
    }

    protected SchedulePlantModel(Parcel in) {
        plantModel = in.readParcelable(PlantModel.class.getClassLoader());
        message = in.readString();
    }

    public static final Creator<SchedulePlantModel> CREATOR = new Creator<SchedulePlantModel>() {
        @Override
        public SchedulePlantModel createFromParcel(Parcel in) {
            return new SchedulePlantModel(in);
        }

        @Override
        public SchedulePlantModel[] newArray(int size) {
            return new SchedulePlantModel[size];
        }
    };

    public PlantModel getPlantModel() {
        return plantModel;
    }

    public void setPlantModel(PlantModel plantModel) {
        this.plantModel = plantModel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(plantModel, i);
        parcel.writeString(message);
    }
}
