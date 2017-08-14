package com.example.alexander.popularmoviesapp.moviedata;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by awest on 8/13/2017.
 */

public class DownloadedMovie extends Movie implements Parcelable {
    public DownloadedMovie(String id, String title, String synopsis,
                           double userRating, String releaseDate) {
        super(id, title, synopsis, userRating, releaseDate);
    }

    public Bitmap getPoster() {
        return null;
    }

    public Bitmap getBackdrop() {
        return null;
    }

    private DownloadedMovie(Parcel in) {
        super(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
    }

    public static final Parcelable.Creator<DownloadedMovie> CREATOR = new Parcelable.Creator<DownloadedMovie>() {
        @Override
        public DownloadedMovie createFromParcel(Parcel parcel) {
            return new DownloadedMovie(parcel);
        }

        @Override
        public DownloadedMovie[] newArray(int size) {
            return new DownloadedMovie[size];
        }
    };
}
