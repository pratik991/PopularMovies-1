package com.example.pratik.popular_movies_stage_1.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pratik on 10/19/17.
 */

// Class to store Movie Details
public class Movie implements Parcelable {

    private final String LOG_TAG = Movie.class.getSimpleName();
    private String mOriginalTitle;
    private String mPosterPath;
    private String mOverView;
    private String mReleaseDate;
    private Double mVoteAverage;

    public Movie()
    {
    }

    public Movie(Parcel in)
    {
        mOriginalTitle = in.readString();
        mPosterPath = in.readString();
        mOverView = in.readString();
        mReleaseDate = in.readString();
        mVoteAverage = in.readDouble();
    }

    public void setTitle(String originalTitle)
    {
        this.mOriginalTitle = originalTitle;
    }

    public String getTitle()
    {
        return this.mOriginalTitle;
    }

    public void setmPosterPath(String mPosterPath)
    {
        this.mPosterPath = mPosterPath;
    }

    public String getmPosterPath()
    {
        StringBuilder sb = new StringBuilder("http://image.tmdb.org/t/p/")
                .append("w185")
                .append(this.mPosterPath);

        return sb.toString();
    }

    public void setmOverView(String mOverView)
    {
        this.mOverView = mOverView;
    }

    public String getmOverView()
    {
        return this.mOverView;
    }

    public void setmVoteAverage(Double mVoteAverage)
    {
        this.mVoteAverage = mVoteAverage /2;
    }

    public Double getmVoteAverage()
    {
        return this.mVoteAverage;
    }

    public void setmReleaseDate(String mReleaseDate)
    {
        this.mReleaseDate = mReleaseDate;
    }

    public String getmReleaseDate()
    {
        return  "Release Date: " + this.mReleaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mOriginalTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mOverView);
        dest.writeString(mReleaseDate);
        dest.writeDouble(mVoteAverage);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {

            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {

            return new Movie[i];
        }

    };
}
