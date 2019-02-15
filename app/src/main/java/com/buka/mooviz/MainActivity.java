package com.buka.mooviz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.buka.mooviz.api.TmdbApi;
import com.buka.mooviz.models.Movie;
import com.buka.mooviz.models.Page;
import com.buka.mooviz.utils.MoviesUtility;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TmdbApi api;
    private TextView moviesTextView;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesTextView = findViewById(R.id.textview_movies);
        loadingProgressBar = findViewById(R.id.progressbar_loading);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(TmdbApi.class);

        PopularMoviesRequestTask moviesRequestTask = new PopularMoviesRequestTask();
        moviesRequestTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            LiveData<ArrayList<Movie>> moviesLiveData =
            PopularMoviesRequestTask moviesRequestTask = new PopularMoviesRequestTask();
            moviesRequestTask.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class PopularMoviesRequestTask extends AsyncTask<Void, Void, ArrayList<Movie>> {
        @Override
        protected void onPreExecute() {
            moviesTextView.setText("");
            loadingProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            try {
                Response<Page> response = api.getPopularMovies().execute();

                if (response.isSuccessful()) {
                    Page page = response.body();
                    return page.getResults();
                } else {
                    return null;
                }

            } catch (IOException exception) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            loadingProgressBar.setVisibility(View.INVISIBLE);

            if (movies != null) {
                String moviesText = MoviesUtility.createStringWithMovies(movies);
                moviesTextView.setText(moviesText);
            }
        }
    }
}
