package com.example.pratik.popular_movies_stage_1.Fragment;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.pratik.popular_movies_stage_1.Model.Movie;
import com.example.pratik.popular_movies_stage_1.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Pratik on 10/27/17.
 */

public class MovieDetailFragment extends Fragment {
    private final String LOG_TAG_FRAG = MovieDetailFragment.class.getSimpleName();
    private Movie mMovie;

    public MovieDetailFragment(){
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        String movieDetailData = getString(R.string.movie_details_data);
        View rootView = inflater.inflate(R.layout.movie_details,container,false);


        if(intent != null && intent.hasExtra(movieDetailData))
        {
            mMovie = intent.getParcelableExtra(movieDetailData);

            ((TextView)rootView.findViewById(R.id.movie_text_view)).setText(mMovie.getTitle());

            ((RatingBar)rootView.findViewById(R.id.vote_average_bar)).setNumStars(5);
            ((RatingBar)rootView.findViewById(R.id.vote_average_bar)).setRating(mMovie.getmVoteAverage().floatValue());

            ImageView imageView = (ImageView)rootView.findViewById(R.id.movie_image_view);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(10, 10, 10, 10);
            Picasso.with(getContext()).load(mMovie.getmPosterPath()).into(imageView);

            ((TextView)rootView.findViewById(R.id.date_text_view)).setText(mMovie.getmReleaseDate());

            ((TextView)rootView.findViewById(R.id.overview_text_view)).setText(mMovie.getmOverView());
            ((TextView)rootView.findViewById(R.id.overview_text_view))
                    .setMovementMethod(new ScrollingMovementMethod());
        }
        else
            Log.e(LOG_TAG_FRAG, "Intent was null or data was not sent.");
        return rootView;
    }

}
