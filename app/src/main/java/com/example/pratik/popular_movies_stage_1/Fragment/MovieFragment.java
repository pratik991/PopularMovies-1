package com.example.pratik.popular_movies_stage_1.Fragment;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.pratik.popular_movies_stage_1.Activity.DetailActivity;
import com.example.pratik.popular_movies_stage_1.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.pratik.popular_movies_stage_1.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Pratik on 10/23/17.
 */

public class MovieFragment extends Fragment {

    private final String LOG_TAG = MovieFragment.class.getSimpleName();

    private ImageAdapter movieDetailsAdapter;

    @Override
    public void onStart() {
        super.onStart();
        selectList();
    }

    private void selectList()
    {
        FetchMoviesTask movieTask = new FetchMoviesTask();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = sharedPrefs.getString(getString(R.string.pref_list_key),getString(R.string.pref_list_default));
        movieTask.execute(sortOrder);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.movie_main, container, false);

        movieDetailsAdapter = new  ImageAdapter(this.getContext(), new ArrayList<Movie>());

        GridView gView = (GridView)rootView.findViewById(R.id.gridview);
        gView.setAdapter(movieDetailsAdapter);

        gView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(),"Movie Detail",Toast.LENGTH_SHORT).show();
                Movie movie = movieDetailsAdapter.getItem(i);
                Intent movieDetailIntent = new Intent(getContext(),DetailActivity.class);
                movieDetailIntent.putExtra(getString(R.string.movie_details_data), (Parcelable) movie);
                startActivity(movieDetailIntent);
            }
        });
        return rootView;
    }

    public class FetchMoviesTask extends AsyncTask<String, Void,  ArrayList<Movie>>
    {

        @Override
        protected void onPostExecute( ArrayList<Movie> results) {
            if(results != null)
            {
                movieDetailsAdapter.clear();
                for(Movie movie : results)
                {
                    movieDetailsAdapter.add(movie);
                }
            }
        }

        @Override
        protected  ArrayList<Movie> doInBackground(String... parameters) {

            int READ_TIMEOUT = 1000;
            int CONN_TIMEOUT = 1500;

            String BASE_URL = getString(R.string.base_url);
            String VER = getString(R.string.api_version_3);
            String BASE_PATH = getString(R.string.movie);
            String Q_FIELD_API_KEY = getString(R.string.api_key);
            String Q_FIELD_LANG = getString(R.string.lang);
            String Q_PARAM_LANG = getString(R.string.lang_type);
            String Q_FIELD_PAGE = getString(R.string.page);
            String Q_PARAM_PAGE = "1";
            String REQ_METHOD = getString(R.string.request_type_get);

            if (parameters.length == 0)
                return null;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr;

            try{

                Uri movieUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(VER)
                        .appendPath(BASE_PATH)
                        .appendPath(parameters[0])
                        .appendQueryParameter(Q_FIELD_API_KEY, "") // ADD API KEY HERE
                        .appendQueryParameter(Q_FIELD_LANG, Q_PARAM_LANG)
                        .appendQueryParameter(Q_FIELD_PAGE, Q_PARAM_PAGE)
                        .build();

                URL dataUrl = new URL(movieUri.toString());
                urlConnection = (HttpURLConnection) dataUrl.openConnection();
                urlConnection.setReadTimeout(READ_TIMEOUT);
                urlConnection.setConnectTimeout(CONN_TIMEOUT);
                urlConnection.setRequestMethod(REQ_METHOD);
                urlConnection.setDoInput(true);
                urlConnection.connect();

                InputStream inStrm = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inStrm == null)
                    return null;

                reader = new BufferedReader(new InputStreamReader(inStrm));

                String line;
                while ((line = reader.readLine()) != null)
                {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0)
                    return null;

                movieJsonStr = buffer.toString();
                return parseMovieJson(movieJsonStr);

            }
            catch(Exception e){
                Log.e("PopularMovieFragment", "error", e);
                return null;
            }
            finally {
                if (urlConnection != null)
                    urlConnection.disconnect();

                if (reader != null)
                    try {
                        reader.close();
                    }
                    catch (final IOException ioe)
                    {
                        Log.e("MovieFragment", "Reader did not close properly.", ioe);
                    }
            }
        }
    }

    private ArrayList<Movie> parseMovieJson(String jsonStr) throws JSONException
    {
        String RESULTS = getString(R.string.results);
        String ORIG_TITLE = getString(R.string.original_title);
        String OVERVIEW = getString(R.string.overview);
        String POSTER_PATH = getString(R.string.poster_path);
        String RELEASE_DATE = getString(R.string.release_date);
        String VOTE_AVERAGE = getString(R.string.vote_average);

        JSONObject movieArray = new JSONObject(jsonStr);
        JSONArray results = movieArray.getJSONArray(RESULTS);

        ArrayList<Movie> movieDetails = new ArrayList<>();
        for(int movie = 0; movie < results.length(); movie++)
        {
            Movie movieDetail = new Movie();
            JSONObject movieObject = results.getJSONObject(movie);

            movieDetail.setTitle(movieObject.getString(ORIG_TITLE));
            movieDetail.setmOverView(movieObject.getString(OVERVIEW));
            movieDetail.setmPosterPath(movieObject.getString(POSTER_PATH));
            movieDetail.setmReleaseDate(movieObject.getString(RELEASE_DATE));
            movieDetail.setmVoteAverage(movieObject.getDouble(VOTE_AVERAGE));

            movieDetails.add(movieDetail);
        }

        return movieDetails;
    }

    public class ImageAdapter extends ArrayAdapter<Movie>
    {
        private final String LOG_TAG = ImageAdapter.class.getSimpleName();

        public ImageAdapter(Context context, List<Movie> movieDetails)
        {
            super(context, 0, movieDetails);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Movie details = getItem(position);
            ImageView imageView;

            if (convertView == null) {
                imageView = new ImageView(getContext());
                imageView.setLayoutParams(new GridView.LayoutParams(740, 1112));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(10, 10, 10, 10);
            } else {
                imageView = (ImageView) convertView;
            }

            Picasso.with(parent.getContext()).load(details.getmPosterPath()).into(imageView);
            return imageView;
        }
    }
}
