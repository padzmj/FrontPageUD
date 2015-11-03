package com.brightlight.padzmj.frontpage;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.brightlight.padzmj.frontpage.Movies.Movie;
import com.brightlight.padzmj.frontpage.Movies.MovieFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final String POPULAR_PARAM = "popular";
    final String TOP_RATED_PARAM = "top_rated";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DownloadMoviesDB downloadMoviesDB = new DownloadMoviesDB();
        downloadMoviesDB.execute(POPULAR_PARAM);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Downloading Movies", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
    }

    private class DownloadMoviesDB extends AsyncTask<String, Void, List<Movie>>{

        private String LOG_TAG = DownloadMoviesDB.class.getSimpleName();
        private String API_KEY = "6f8b815f9761bc4221829080d402892b";
        private String movieJSONStr;


        final String MOVIE_PARAM = "movie";
        final String API_KEY_PARAM = "?api_key";

        private List<Movie> movieData(String parsedJSONStr) throws JSONException{

            final String posterURL = "http://image.tmdb.org/t/p/w500";

            final String dbResults = "results";
            final String id = "id";
            final String dbPosterPath = "poster_path";
            final String dbTitle = "original_title";
            final String dbReleaseDate = "release_date";
            final String dbOverview = "overview";
            final String dbRating = "vote_average";
            List<Movie> movieList = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(parsedJSONStr);
            JSONArray jsonArray = jsonObject.getJSONArray(dbResults);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            for (int i = 0; i < 20; i++) {
                JSONObject jsonMovie = jsonArray.getJSONObject(i);

                String movieID = jsonMovie.getString(id);
                String moviePosterPath = jsonMovie.getString(dbPosterPath);
                String movieTitle = jsonMovie.getString(dbTitle);
                String movieReleaseYear = jsonMovie.getString(dbReleaseDate);
                String movieSynopsis = jsonMovie.getString(dbOverview);
                String movieRating = jsonMovie.getString(dbRating);
                Movie movie = new Movie();

                Date date = new Date();

                try{
                    date = format.parse(movieReleaseYear);
                }catch (Exception e){
                    e.printStackTrace();
                }

                String year = (String) android.text.format.DateFormat.format("yyyy", date);
                //Log.i("DATE!!!!", year);

                movie.setID(movieID);
                movie.setPosterPath(posterURL + moviePosterPath);
                movie.setTitle(movieTitle);
                movie.setReleaseYear(year);
                movie.setSynopsis(movieSynopsis);
                movie.setUserRating(movieRating);

                //Log.i("MOVIE TITLE", movieTitle);
                movieList.add(movie);
            }
            return movieList;
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://api.themoviedb.org/3/movie/"+params[0]+"?api_key=" + API_KEY);


                Uri uri = Uri.parse("https://api.themoviedb.org/3").buildUpon()
                        .appendQueryParameter(MOVIE_PARAM, "movie")
                        .appendQueryParameter(POPULAR_PARAM, "popular")
                        .appendQueryParameter(API_KEY_PARAM, API_KEY)
                        .build();

                //URL url = new URL(uri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                movieJSONStr = buffer.toString();

                Log.v(LOG_TAG, movieJSONStr);
            }
            catch (IOException e)
            {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            }

            finally
            {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }

                try{
                    return movieData(movieJSONStr);
                } catch (JSONException e){
                    e.printStackTrace();
                }
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {

            //Toast.makeText(getApplicationContext(), "Post Execute", Toast.LENGTH_LONG).show();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            //Bundle bundle = new Bundle();
            //bundle.putStringArrayList("movieTitles", movies);

            //Toast.makeText(getApplication(), movies.get(2).getPosterPath(), Toast.LENGTH_LONG).show();

            MovieFragment movieFragment = MovieFragment.newInstance(getApplication(), movies);
            //movieFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.frameLayout, movieFragment);
            fragmentTransaction.commit();


            /*if(strings != null){
                for (String s: strings) {
                    Log.i("JSONMOVIES", s);

                }
            }*/
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        DownloadMoviesDB popular = new DownloadMoviesDB();
        DownloadMoviesDB top_rated = new DownloadMoviesDB();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popular) {
            popular.execute(POPULAR_PARAM);
            return true;
        }else if (id == R.id.action_top_rated) {
            top_rated.execute(TOP_RATED_PARAM);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
