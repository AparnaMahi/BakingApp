package com.example.aparn.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeSteps implements Parcelable {

    private int id;
    private String shortDescription;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;

    public static final Parcelable.Creator<RecipeSteps> CREATOR
            = new Parcelable.Creator<RecipeSteps>() {
        @Override
        public RecipeSteps createFromParcel(Parcel source) {
            return new RecipeSteps(source);
        }

        @Override
        public RecipeSteps[] newArray(int size) {
            return new RecipeSteps[size];
        }
    };

    public RecipeSteps(int id_val, String short_Description,
                       String description_values, String video_Url, String thumbnail_Url) {
        id= id_val;
        shortDescription = short_Description;
        description = description_values;
        videoUrl = video_Url;
        thumbnailUrl = thumbnail_Url;
    }

    private RecipeSteps(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoUrl = in.readString();
        thumbnailUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoUrl);
        dest.writeString(thumbnailUrl);
    }

    public int getId() {
        return id;
    }

    public void setId(int id ){
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}

