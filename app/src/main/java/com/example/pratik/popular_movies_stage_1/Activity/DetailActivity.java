package com.example.pratik.popular_movies_stage_1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.example.pratik.popular_movies_stage_1.R;
import com.example.pratik.popular_movies_stage_1.Fragment.MovieDetailFragment;

/**
 * Created by Pratik on 10/23/17.
 */

public class DetailActivity extends AppCompatActivity {

    private final String LOG_TAG_ACT = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.movie_details, new MovieDetailFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
