package com.buka.mooviz;

import android.os.AsyncTask;
import android.view.View;

import com.buka.mooviz.api.TmdbApi;
import com.buka.mooviz.models.Movie;
import com.buka.mooviz.models.Page;
import com.buka.mooviz.utils.MoviesUtility;

import java.io.IOException;
import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Movie>> popularMoviesLiveData;
    private TmdbApi tmdbApi;

    public MainViewModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tmdbApi = retrofit.create(TmdbApi.class);

    }

    public LiveData<ArrayList<Movie>> getPopularMovies(){


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
                Response<Page> response = tmdbApi.getPopularMovies().execute();

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
            super.onPostExecute(movies);
            popularMoviesLiveData.setValue(movies);
        }
    }
}
