package com.example.waterplant.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class PlantModel implements Parcelable {
    private Uri image;
    private String name;
    private String category;

    public PlantModel(Uri image, String title, String description) {
        this.image = image;
        this.name = title;
        this.category = description;
    }

    protected PlantModel(Parcel in) {
        image = in.readParcelable(Uri.class.getClassLoader());
        name = in.readString();
        category = in.readString();
    }

    public static final Creator<PlantModel> CREATOR = new Creator<PlantModel>() {
        @Override
        public PlantModel createFromParcel(Parcel in) {
            return new PlantModel(in);
        }

        @Override
        public PlantModel[] newArray(int size) {
            return new PlantModel[size];
        }
    };

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(image, i);
        parcel.writeString(name);
        parcel.writeString(category);
    }
}
