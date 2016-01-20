package com.brightlight.padzmj.frontpage;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.brightlight.padzmj.frontpage.Movies.Detail.Movie;
import com.brightlight.padzmj.frontpage.Movies.Detail.MovieDetailedFragment;
import com.brightlight.padzmj.frontpage.Movies.Detail.MovieFragment;

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

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private static final String POPULAR_PARAM = "popular";
    private static final String TOP_RATED_PARAM = "top_rated";
    private static final String LOG_NAME = MainActivity.class.getSimpleName();

    final ArrayList<String> list = new ArrayList<>();

    Realm realm;
    RealmResults<Movie> realmFavouriteMovieResults;
    RealmResults<Movie> realmMovieDBResults;

    Toolbar toolbar;

    boolean twoPane;
    CoordinatorLayout mainCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_activity_coordinatorLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        realm = Realm.getInstance(this);

        realmFavouriteMovieResults = realm.where(Movie.class).equalTo("favouriteMovie", true).findAll();
        realmMovieDBResults = realm.where(Movie.class).findAll();

        twoPane = ((findViewById(R.id.fragment_master_detail) != null));

        DownloadMoviesDB downloadMoviesDB = new DownloadMoviesDB(this);
        downloadMoviesDB.execute(POPULAR_PARAM);


//        Toast.makeText(this, realmMovieDBResults.size() + "  DB Results", Toast.LENGTH_LONG).show();
//        Toast.makeText(this, realmFavouriteMovieResults.size()+"  DB Favourites", Toast.LENGTH_LONG).show();
//        Uri movieUri = MovieEntry.buildMovie();
//
//        final int delete = getApplicationContext().getContentResolver().delete(movieUri,
//                null, null);
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

        DownloadMoviesDB popular = new DownloadMoviesDB(this);
        DownloadMoviesDB top_rated = new DownloadMoviesDB(this);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popular) {
            toolbar.setTitle("Popular");
            popular.execute(POPULAR_PARAM);
            return true;
        } else if (id == R.id.action_top_rated) {
            toolbar.setTitle("Top Rated");
            top_rated.execute(TOP_RATED_PARAM);
            return true;
        } else if (id == R.id.action_favourites) {

            //Toast.makeText(this, results + " Results", Toast.LENGTH_LONG).show();

            if (realmFavouriteMovieResults.size() != 0) {
                toolbar.setTitle("Favourites");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                MovieFragment movieFragment = MovieFragment.newInstance(getApplication(), realmFavouriteMovieResults, twoPane);

                fragmentTransaction.replace(R.id.fragment_navigation, movieFragment);
                fragmentTransaction.commit();
            } else {
                Snackbar.make(mainCoordinatorLayout, "No Favourite Movies", Snackbar.LENGTH_LONG).show();
            }


        }

        return super.onOptionsItemSelected(item);
    }

    void launchMoviesFragment(List<Movie> movies) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        MovieFragment movieFragment = MovieFragment.newInstance(getApplication(), movies, twoPane);

        fragmentTransaction.replace(R.id.fragment_navigation, movieFragment);
        fragmentTransaction.commit();
    }

    void launchMoviesTwoPaneFragment(List<Movie> movies) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragment_navigation, MovieFragment.newInstance(getApplicationContext(), movies, twoPane));
        fragmentTransaction.replace(R.id.fragment_master_detail, MovieDetailedFragment.newInstance(getApplicationContext()));
        fragmentTransaction.commit();
    }

    void launchMoviesFromDB(RealmResults<Movie> movieDBRealmList) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        MovieFragment movieFragment = MovieFragment.newInstance(getApplication(), movieDBRealmList, twoPane);

        fragmentTransaction.replace(R.id.frameLayout, movieFragment);
        fragmentTransaction.commit();
    }

    private class DownloadMoviesDB extends AsyncTask<String, Void, List<Movie>> {

        final String MOVIE_PARAM = "movie";
        final String API_KEY_PARAM = "?api_key";
        private String LOG_TAG = DownloadMoviesDB.class.getSimpleName();
        private String API_KEY = "6f8b815f9761bc4221829080d402892b";
        private String movieJSONStr;
        private Context context;

        DownloadMoviesDB(Context context) {
            this.context = context;
        }

        private List<Movie> movieData(String parsedJSONStr) throws JSONException {

            final String posterURL = "http://image.tmdb.org/t/p/w500";

            final String dbResults = "results";
            final String id = "id";
            final String dbPosterPath = "poster_path";
            final String dbBackdropPath = "backdrop_path";
            final String dbTitle = "original_title";
            final String dbReleaseDate = "release_date";
            final String dbOverview = "overview";
            final String dbRating = "vote_average";
            final String dbRuntime = "runtime";
            List<Movie> movieList = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(parsedJSONStr);
            JSONArray jsonArray = jsonObject.getJSONArray(dbResults);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            //Vector<ContentValues> cVVector = new Vector<ContentValues>(jsonArray.length());

            for (int i = 0; i < 20; i++) {
                JSONObject jsonMovie = jsonArray.getJSONObject(i);

                final String movieID = jsonMovie.getString(id);
                final String moviePosterPath = jsonMovie.getString(dbPosterPath);
                final String movieBackdrop = jsonMovie.getString(dbBackdropPath);
                final String movieTitle = jsonMovie.getString(dbTitle);
                final String movieReleaseYear = jsonMovie.getString(dbReleaseDate);
                final String movieSynopsis = jsonMovie.getString(dbOverview);
                final String movieRating = jsonMovie.getString(dbRating);
//                String movieRuntime = jsonMovie.getString("runtime");
                Movie movie = new Movie();

                Date date = new Date();

                try {
                    date = format.parse(movieReleaseYear);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String year = (String) android.text.format.DateFormat.format("yyyy", date);
                //Log.i("DATE!!!!", year);

                movie.setId(movieID);
                movie.setPosterPath(posterURL + moviePosterPath);
                movie.setBackdropPath(posterURL + movieBackdrop);
                movie.setTitle(movieTitle);
                movie.setReleaseYear(year);
                movie.setSynopsis(movieSynopsis);
                movie.setUserRating(movieRating);
                movie.setFavouriteMovie(false);
                movieList.add(movie);
            }

//            if ( cVVector.size() > 0 ) {
//                ContentValues[] cvArray = new ContentValues[cVVector.size()];
//                cVVector.toArray(cvArray);
//                getApplicationContext().getContentResolver().bulkInsert(MovieEntry.CONTENT_URI, cvArray);
//            }
//
//            Uri movieUri = MovieEntry.buildMovie();
//
//            Cursor cur = getApplicationContext().getContentResolver().query(movieUri,
//                    null, null, null, null);
//
//
//            if(cur != null){
//                Log.i("Inserted", cur.getCount()+"");
//            }
//
//
//            cVVector = new Vector<ContentValues>(cur.getCount());
//            if ( cur.moveToFirst() ) {
//                do {
//                    ContentValues cv = new ContentValues();
//                    DatabaseUtils.cursorRowToContentValues(cur, cv);
//                    cVVector.add(cv);
//                } while (cur.moveToNext());
//            }

            /*for (int i = 0; i <= cVVector.size(); i++){
                Log.d(LOG_TAG, "FetchWeatherTask Complete. " + cVVector.get(i).getAsString("title") + " Inserted");
            }*/

            return movieList;
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://api.themoviedb.org/3/movie/" + params[0] + "?api_key=" + API_KEY);


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
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
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

                try {
                    return movieData(movieJSONStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (!twoPane) {
                launchMoviesFragment(movies);
            } else {
                launchMoviesTwoPaneFragment(movies);
            }
        }
    }
}