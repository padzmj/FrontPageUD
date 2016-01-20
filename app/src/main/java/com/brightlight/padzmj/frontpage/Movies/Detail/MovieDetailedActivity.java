package com.brightlight.padzmj.frontpage.Movies.Detail;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.brightlight.padzmj.frontpage.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class MovieDetailedActivity extends AppCompatActivity {

    String movieID, posterPath, backdropPath, movieTitle, releaseYear, movieSynopsis, userRating, runTime, state;
    boolean favourite;
    RealmList<Movie> FavouriteMoviesavourite;
    RealmList<Movie> realmMovieList = new RealmList<>();
    Context context;
    Realm newRealm, realm;
    Movie movie;

    boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detailed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main_detail);
        setSupportActionBar(toolbar);

        ImageView poster, backdropPoster;

        poster = (ImageView) findViewById(R.id.detail_poster);
        backdropPoster = (ImageView) findViewById(R.id.detail_backdrop_poster);

        Bundle bundle = getIntent().getExtras();

        movie = null;

        context = this;

        if (bundle != null) {
            movieID = bundle.getString("id");
            posterPath = bundle.getString("posterPath");
            backdropPath = bundle.getString("backdropPath");
            movieTitle = bundle.getString("movieTitle");
            releaseYear = bundle.getString("releaseYear");
            this.movieSynopsis = bundle.getString("synopsis");
            userRating = bundle.getString("userRating");
            runTime = bundle.getString("runtime");
            favourite = bundle.getBoolean("favourite");

            Glide.with(this).load(backdropPath).diskCacheStrategy(DiskCacheStrategy.ALL).into(backdropPoster);
            Glide.with(this).load(posterPath).diskCacheStrategy(DiskCacheStrategy.ALL).into(poster);
        }

        if (savedInstanceState == null) {
            newRealm = Realm.getInstance(context);
            newRealm.beginTransaction();

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_to_favourites_fab);

            RealmQuery<Movie> movieRealmQuery = newRealm.where(Movie.class);

            final RealmResults<Movie> results = movieRealmQuery.equalTo("id", movieID).findAll();

            for (Movie results1 : results) {
                movie = results1;
            }

            fab.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {

                                           if (movie != null) {
                                               Snackbar.make(view, movie.getTitle() + " Already in Favourites", Snackbar.LENGTH_LONG).show();
                                           } else {
                                               realm = Realm.getInstance(context);
                                               realm.beginTransaction();
                                               Movie realmMovie = realm.createObject(Movie.class);

                                               realmMovie.setId(movieID);
                                               realmMovie.setBackdropPath(backdropPath);
                                               realmMovie.setPosterPath(posterPath);
                                               realmMovie.setTitle(movieTitle);
                                               realmMovie.setReleaseYear(releaseYear);
                                               realmMovie.setSynopsis(movieSynopsis);
                                               realmMovie.setUserRating(userRating);
                                               realmMovie.setFavouriteMovie(true);

                                               realmMovieList.add(realmMovie);
                                               Snackbar.make(view, realmMovie.getTitle() + " Added to Favourites", Snackbar.LENGTH_LONG).show();
                                               realm.commitTransaction();

                                           }


                                       }
                                   }
            );


            newRealm.commitTransaction();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            MovieDetailedFragment movieDetailedFragment = MovieDetailedFragment.newInstance(this);
            movieDetailedFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_master_detail, movieDetailedFragment);
            fragmentTransaction.commit();
        }

    }
}
//
//static final String REVIEWS_KEY = "reviews_key";
//String id, posterPath, backdropPath,  movieTitle, releaseYear, movieSynopsis, userRating, runTime;
//RecyclerView recyclerView, reviewRecyclerView;
//
//List<Review> reviewList = Collections.emptyList();
//List<Trailer> trailerList = Collections.emptyList();
//
//TextView title, release, synopsis, rating, runtime, reviewsTextViewTitle, trailersTextViewTitle;
//ImageView poster, backdropPoster, ratingIcon, runtimeIcon;
//
//ViewGroup reviewsGroup;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_movie_detailed);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main_detail);
//        setSupportActionBar(toolbar);
//
//        title= (TextView) findViewById(R.id.detail_movie_title);
//        release = (TextView) findViewById(R.id.detail_release_year);
//        synopsis = (TextView) findViewById(R.id.detail_synopsis);
//        rating = (TextView) findViewById(R.id.detail_user_rating);
//        runtime = (TextView) findViewById(R.id.detail_runtime);
//        poster = (ImageView) findViewById(R.id.detail_poster);
//        backdropPoster = (ImageView) findViewById(R.id.detail_backdrop_poster);
//        ratingIcon = (ImageView) findViewById(R.id.detail_user_rating_icon);
//        runtimeIcon = (ImageView) findViewById(R.id.detail_runtime_icon);
//        //reviewsTextViewTitle = (TextView) findViewById(R.id.reviewsTextViewTitle);
//        //trailersTextViewTitle = (TextView) findViewById(R.id.trailersTextViewTitle);
//
//        String reviewsTitle = getResources().getString(R.string.reviewsHeaderTitle);
//        String trailersTitle = getResources().getString(R.string.trailersHeaderTitle);
//        String space = getResources().getString(R.string.space_placeholder);
//
//        Bundle bundle = getIntent().getExtras();
//
//        if(bundle!=null){
//            id = bundle.getString("id");
//            posterPath = bundle.getString("posterPath");
//            backdropPath = bundle.getString("backdropPath");
//            movieTitle = bundle.getString("movieTitle");
//            releaseYear = bundle.getString("releaseYear");
//            this.movieSynopsis = bundle.getString("synopsis");
//            userRating = bundle.getString("userRating");
//            runTime = bundle.getString("runtime");
//            toolbar.setTitle(movieTitle);
//        }
//
////        DownloadExtendedMoviesDB downloadExtendedMoviesDB = new DownloadExtendedMoviesDB(this);
////        downloadExtendedMoviesDB.execute(id);
//
//        if(savedInstanceState == null){
//            DownloadTrailers downloadTrailers = new DownloadTrailers(this);
//            downloadTrailers.execute(id);
//
//            DownloadReviews downloadReviews = new DownloadReviews(this);
//            downloadReviews.execute(id);
//        }
//
//
//        Glide.with(this).load(backdropPath).diskCacheStrategy(DiskCacheStrategy.ALL).into(backdropPoster);
//        Glide.with(this).load(posterPath).diskCacheStrategy(DiskCacheStrategy.ALL).into(poster);
//        ratingIcon.setBackgroundResource(R.drawable.ic_star_black_18dp);
//        //runtimeIcon.setBackgroundResource(R.drawable.ic_access_time_black_18dp);
//        title.setText(movieTitle);
//        release.setText(releaseYear);
//        synopsis.setText(this.movieSynopsis);
//        rating.setText(userRating);
//        //runtime.setText(runTime);
//        //reviewsTextViewTitle.setText(reviewsTitle);
//        //trailersTextViewTitle.setText(trailersTitle);
//
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action " + id, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }
//
//public class DownloadTrailers extends AsyncTask<String, Void, List<Trailer>> {
//
//    private String LOG_TAG = DownloadTrailers.class.getSimpleName();
//    private String API_KEY = getString(R.string.api_key);
//    private String movieJSONStr;
//
//    private Context mContext;
//
//    public DownloadTrailers(Context context){
//        mContext = context;
//    }
//
//
//
//    private List<Trailer> movieData(String parsedJSONStr) throws JSONException {
//
//        final String dbResults = "results";
//        final String id = "id";
//        final String dbKey = "key";
//        final String dbSize = "size";
//        final String dbType = "type";
//
//        final String youtubeURL = "https://www.youtube.com/watch?v=";
//
//        JSONObject jsonObject = new JSONObject(parsedJSONStr);
//        JSONArray jsonArray = jsonObject.getJSONArray(dbResults);
//
//        List<Trailer> trailerList = new ArrayList<>();
//
//        for(int i = 0; i < jsonArray.length(); i++){
//            JSONObject trailerObject = jsonArray.getJSONObject(i);
//
//            String trailerKey = trailerObject.getString(dbKey);
//
//            Trailer trailer = new Trailer();
//
//            trailer.setTrailerURL(youtubeURL + trailerKey);
//            trailer.setTrailerKey(trailerKey);
//
//            trailerList.add(trailer);
//        }
//
//        return trailerList;
//    }
//
//    @Override
//    protected List<Trailer> doInBackground(String... params) {
//
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//
//        try {
//            URL url = new URL("https://api.themoviedb.org/3/movie/"+params[0]+"/videos?api_key=" + API_KEY);
//
//            //URL url = new URL(uri.toString());
//
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//            // Read the input stream into a String
//            InputStream inputStream = urlConnection.getInputStream();
//            StringBuffer buffer = new StringBuffer();
//            if (inputStream == null) {
//                return null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                buffer.append(line + "\n");
//            }
//
//            if (buffer.length() == 0) {
//                return null;
//            }
//            movieJSONStr = buffer.toString();
//
//            Log.v(LOG_TAG, movieJSONStr);
//        }
//        catch (IOException e)
//        {
//            Log.e(LOG_TAG, "Error ", e);
//            return null;
//        }
//
//        finally
//        {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (final IOException e) {
//                    Log.e(LOG_TAG, "Error closing stream", e);
//                }
//            }
//
//            try{
//                return movieData(movieJSONStr);
//            } catch (JSONException e){
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }
//
//    @Override
//    protected void onPostExecute(List<Trailer> trailers) {
//        trailerList = trailers;
//
//        if(trailerList.size() != 0){
//            //trailersTextViewTitle.setVisibility(View.VISIBLE);
//
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//
//
//            TrailersFragment trailersFragment = TrailersFragment.newInstance(getApplication(), trailerList);
//            fragmentTransaction.add(R.id.trailersFrameLayout, trailersFragment);
//            fragmentTransaction.commit();
//        }
//    }
//}
//
//public class DownloadReviews extends AsyncTask<String, Void, List<Review>> {
//
//    private String LOG_TAG = DownloadReviews.class.getSimpleName();
//    private String API_KEY = getString(R.string.api_key);
//    private String movieJSONStr;
//
//    private Context mContext;
//
//    public DownloadReviews(Context context){
//        mContext = context;
//    }
//
//
//    private List<Review> movieData(String parsedJSONStr) throws JSONException {
//
//        final String dbResults = "results";
//        final String id = "id";
//        final String dbPoster = "url";
//        final String dbAuthor = "author";
//        final String dbContent = "content";
//
//        JSONObject jsonObject = new JSONObject(parsedJSONStr);
//        JSONArray jsonArray = jsonObject.getJSONArray(dbResults);
//
//        List<Review> reviewList = new ArrayList<>();
//
//        for(int i = 0; i < jsonArray.length(); i++){
//
//            JSONObject reviewObject = jsonArray.getJSONObject(i);
//
//            String author = reviewObject.getString(dbAuthor);
//            String content = reviewObject.getString(dbContent);
//
//            Review review = new Review();
//
//            review.setAuthor(author);
//            review.setContent(content);
//
//            reviewList.add(review);
//        }
//
//        return reviewList;
//    }
//
//    @Override
//    protected List<Review> doInBackground(String... params) {
//
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//
//        try {
//            URL url = new URL("https://api.themoviedb.org/3/movie/"+params[0]+"/reviews?api_key=" + API_KEY);
//
//            //URL url = new URL(uri.toString());
//
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//            // Read the input stream into a String
//            InputStream inputStream = urlConnection.getInputStream();
//            StringBuffer buffer = new StringBuffer();
//            if (inputStream == null) {
//                return null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                buffer.append(line + "\n");
//            }
//
//            if (buffer.length() == 0) {
//                return null;
//            }
//            movieJSONStr = buffer.toString();
//
//            Log.v(LOG_TAG, movieJSONStr);
//        }
//        catch (IOException e)
//        {
//            Log.e(LOG_TAG, "Error ", e);
//            return null;
//        }
//
//        finally
//        {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (final IOException e) {
//                    Log.e(LOG_TAG, "Error closing stream", e);
//                }
//            }
//
//            try{
//                return movieData(movieJSONStr);
//            } catch (JSONException e){
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }
//
//    @Override
//    protected void onPostExecute(List<Review> reviews) {
//        reviewList = reviews;
//
//        if(reviewList.size() != 0){
//            //reviewsTextViewTitle.setVisibility(View.VISIBLE);
//
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//
//            ReviewsFragment reviewsFragment = ReviewsFragment.newInstance(getApplication(), reviewList);
//            fragmentTransaction.add(R.id.reviewsFrameLayout, reviewsFragment);
//            fragmentTransaction.commit();
//        }
//    }
//}
