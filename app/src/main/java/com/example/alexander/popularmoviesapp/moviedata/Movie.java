package com.example.alexander.popularmoviesapp.moviedata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alexander on 6/16/2016.
 */
public class Movie implements Parcelable, JsonDataType {
    private String id;
    private String title;

    private String synopsis;
    private double userRating;
    private String releaseDate;

    public Movie(String id, String title, String synopsis, double userRating, String releaseDate) {
        this.id = id;
        this.title = title;
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    //region Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public double getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
    //endregion

    // Still not entirely sure what this does, but my theory
    // is that it reads in the object information stored in the
    // parcel when it needs to be accessed?
    private Movie(Parcel in) {
        id = in.readString();
        title = in.readString();
        synopsis = in.readString();
        userRating = in.readDouble();
        releaseDate = in.readString();
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", userRating=" + userRating +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(synopsis);
        parcel.writeDouble(userRating);
        parcel.writeString(releaseDate);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public String getJsonDataType() {
        return "Movie";
    }
}
