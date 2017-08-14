package com.example.alexander.popularmoviesapp.moviedata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by awest on 8/13/2017.
 */

public class OnlineMovie extends Movie implements Parcelable {
    private String imageUrl = "http://image.tmdb.org/t/p/w342";
    private String backgroundImageUrl = "http://image.tmdb.org/t/p/w780";

    public OnlineMovie(String id, String title, String imageUrl,
                       String backgroundImageUrl, String synopsis,
                       double userRating, String releaseDate) {

        super(id, title, synopsis, userRating, releaseDate);

        this.imageUrl += imageUrl;
        this.backgroundImageUrl += backgroundImageUrl;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    private OnlineMovie(Parcel in) {
        super(in);
        imageUrl = in.readString();
        backgroundImageUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeString(imageUrl);
        parcel.writeString(backgroundImageUrl);
    }

    public static final Parcelable.Creator<OnlineMovie> CREATOR = new Parcelable.Creator<OnlineMovie>() {
        @Override
        public OnlineMovie createFromParcel(Parcel parcel) {
            return new OnlineMovie(parcel);
        }

        @Override
        public OnlineMovie[] newArray(int size) {
            return new OnlineMovie[size];
        }
    };

}
