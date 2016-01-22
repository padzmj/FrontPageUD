package com.brightlight.padzmj.frontpage.Movies.Detail;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brightlight.padzmj.frontpage.Movies.Reviews.Review;
import com.brightlight.padzmj.frontpage.Movies.Trailers.Trailer;
import com.brightlight.padzmj.frontpage.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by PadzMJ on 11/01/2016.
 */
public class MovieDetailedFragment extends Fragment {

    static Context mContext;

    //UI
    protected TextView titleTV, releaseTV, synopsisTV, ratingTV, runtimeTV, trailersAndReviewsTextViewTitle;
    protected ImageView posterIV, backdropPosterIV, ratingIcon, runtimeIcon;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    //UI Variables
    private String id, posterPath, backdropPath, movieTitle, releaseYear, movieSynopsis, userRating, runTime;


    //Review and Trailer Objects
    protected ArrayList<Object> items = new ArrayList<>();
    private List<Review> reviewList = new ArrayList<>();
    private List<Trailer> trailerList = new ArrayList<>();

    //Realm
    private Realm thisRealm, realm;
    private RealmList<Movie> realmMovieList = new RealmList<>();

    private View view;
    protected Movie movie;


    public static MovieDetailedFragment newInstance(Context context) {
        MovieDetailedFragment.mContext = context;
        return new MovieDetailedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View rootView = inflater.inflate(R.layout.movie_detail_fragment_layout, container, false);

        //UI
        view =  rootView.findViewById(R.id.movie_detailed_coordinatorLayout);
        titleTV = (TextView) rootView.findViewById(R.id.detail_movie_title);
        releaseTV = (TextView) rootView.findViewById(R.id.detail_release_year);
        synopsisTV = (TextView) rootView.findViewById(R.id.detail_synopsis);
        ratingTV = (TextView) rootView.findViewById(R.id.detail_user_rating);
        runtimeTV = (TextView) rootView.findViewById(R.id.detail_runtime);
        posterIV = (ImageView) rootView.findViewById(R.id.detail_poster);
        backdropPosterIV = (ImageView) rootView.findViewById(R.id.detail_backdrop_poster);
        ratingIcon = (ImageView) rootView.findViewById(R.id.detail_user_rating_icon);
        runtimeIcon = (ImageView) rootView.findViewById(R.id.detail_runtime_icon);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.contentRecyclerView);
        trailersAndReviewsTextViewTitle = (TextView) rootView.findViewById(R.id.trailersAndReviewsTextViewTitle);
        fab = (FloatingActionButton) rootView.findViewById(R.id.add_to_favourites_fab);


        String trailersAndReviewsTitle = getResources().getString(R.string.trailersAndReviewsHeaderTitle);

        //Realm instance and movie to load realm into
        realm = Realm.getInstance(mContext);
        movie = null;

        //Bundle from Adapter
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            id = bundle.getString("id");
            posterPath = bundle.getString("posterPath");
            backdropPath = bundle.getString("backdropPath");
            movieTitle = bundle.getString("movieTitle");
            releaseYear = bundle.getString("releaseYear");
            this.movieSynopsis = bundle.getString("synopsis");
            userRating = bundle.getString("userRating");
            runTime = bundle.getString("runtime");


            Glide.with(this).load(backdropPath).diskCacheStrategy(DiskCacheStrategy.ALL).into(backdropPosterIV);
            Glide.with(this).load(posterPath).diskCacheStrategy(DiskCacheStrategy.ALL).into(posterIV);

            DownloadMovieInfo downloadMovieInfo = new DownloadMovieInfo(mContext);
            downloadMovieInfo.execute(id);

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmQuery<Trailer> trailerRealmQuery = realm.where(Trailer.class);
                    RealmQuery<Review> reviewRealmQuery = realm.where(Review.class);

                    RealmResults<Trailer> trailerRealmResults = trailerRealmQuery.equalTo("trailerMovieID", id).findAll();
                    RealmResults<Review> reviewRealmResults = reviewRealmQuery.equalTo("reviewMovieID", id).findAll();

                    //If trailers and/or reviews add into items or download them!
                    if (trailerRealmResults.size() != 0) {
                        for (Trailer trailer : trailerRealmResults) {
                            items.add(trailer);
                        }
                    } else trailersReviews(id);
                    if (reviewRealmResults.size() != 0) {
                        for (Review review : reviewRealmResults) {
                            items.add(review);
                        }
                    } else trailersReviews(id);
                }
            });

            if (savedInstanceState == null) {

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        //Find selected movie by ID and make it the local Movie
                        RealmQuery<Movie> movieRealmQuery = realm.where(Movie.class);
                        final RealmResults<Movie> results = movieRealmQuery.equalTo("id", id).findAll();

                        for (Movie results1 : results) {
                            movie = results1;
                        }

                        fab.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(final View view) {

                               //If local movie is populated then Movie is in Favourites,
                               //If not then add to favourites
                               if (movie != null) {
                                   Snackbar.make(view, movie.getTitle() + " Already in Favourites", Snackbar.LENGTH_LONG).show();
                               } else {
                                   thisRealm = Realm.getInstance(mContext);
                                   thisRealm.executeTransaction(new Realm.Transaction() {
                                       @Override
                                       public void execute(Realm realm) {
                                           Movie realmMovie = realm.createObject(Movie.class);

                                           realmMovie.setId(id);
                                           realmMovie.setBackdropPath(backdropPath);
                                           realmMovie.setPosterPath(posterPath);
                                           realmMovie.setTitle(movieTitle);
                                           realmMovie.setReleaseYear(releaseYear);
                                           realmMovie.setSynopsis(movieSynopsis);
                                           realmMovie.setUserRating(userRating);
                                           realmMovie.setFavouriteMovie(true);

                                           realmMovieList.add(realmMovie);
                                           Snackbar.make(view, realmMovie.getTitle() + " Added to Favourites", Snackbar.LENGTH_LONG).show();
                                       }
                                   });
                               }
                           }
                       });
                    }
                });
            }
        }

        //UI Stuff
        if (userRating != null) ratingIcon.setBackgroundResource(R.drawable.ic_star_black_18dp);
        runtimeIcon.setBackgroundResource(R.drawable.ic_access_time_black_18dp);
        titleTV.setText(movieTitle);
        releaseTV.setText(releaseYear);
        synopsisTV.setText(this.movieSynopsis);
        ratingTV.setText(userRating);
        trailersAndReviewsTextViewTitle.setText(trailersAndReviewsTitle);
//        runtime.setText(runTime);

        //RecyclerView
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mContext, items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));


        return rootView;
    }

    void trailersReviews(String id) {
        DownloadReviews downloadReviews = new DownloadReviews(mContext);
        downloadReviews.execute(id);
        DownloadTrailers downloadTrailers = new DownloadTrailers(mContext);
        downloadTrailers.execute(id);
    }

    public class DownloadTrailers extends AsyncTask<String, Void, List<Trailer>> {

        private String LOG_TAG = DownloadTrailers.class.getSimpleName();
        private String API_KEY = getString(R.string.api_key);
        private String movieJSONStr;

        private Context mContext;

        public DownloadTrailers(Context context) {
            mContext = context;
        }


        private List<Trailer> movieData(String parsedJSONStr) throws JSONException {

            final String dbResults = "results";
            final String id = "id";
            final String dbKey = "key";
            final String dbSize = "size";
            final String dbType = "type";

            final String youtubeURL = "https://www.youtube.com/watch?v=";

            List<Trailer> trailerList = new ArrayList<>();
            final RealmList<Trailer> trailerRealmList = new RealmList<>();

            JSONObject jsonObject = new JSONObject(parsedJSONStr);
            JSONArray jsonArray = jsonObject.getJSONArray(dbResults);

            final String trailerMovieID = jsonObject.getString(id);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject trailerObject = jsonArray.getJSONObject(i);

                final String trailerKey = trailerObject.getString(dbKey);
                final String trailerID = trailerObject.getString(id);

                final Trailer trailer = new Trailer();

                trailer.setTrailerID(trailerID);
                trailer.setTrailerURL(youtubeURL + trailerKey);
                trailer.setTrailerKey(trailerKey);

                Realm realm = Realm.getInstance(mContext);
                realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            RealmQuery<Trailer> trailerRealmQuery = realm.where(Trailer.class);
                            RealmResults<Trailer> realmResults = trailerRealmQuery.equalTo("trailerID", trailerID).findAll();

                            if(realmResults.size() ==0){
                                addRealmTrailers(realm);
                            }else{
                                for(Trailer review1:realmResults){
                                    Log.i("REALM ID", review1.getTrailerID());
                                    if(!(review1.getTrailerID().equals(trailerID))){
                                        addRealmTrailers(realm);
                                    }
                                }
                            }
                        }

                    void addRealmTrailers(Realm realm1){
                        Trailer realmTrailer = realm1.createObject(Trailer.class);
                        realmTrailer.setTrailerID(trailerID);
                        realmTrailer.setTrailerMovieID(trailerMovieID);
                        realmTrailer.setTrailerURL(youtubeURL + trailerKey);
                        realmTrailer.setTrailerKey(trailerKey);
                        trailerRealmList.add(realmTrailer);
                    }
                });

                trailerList.add(trailer);
            }

            return trailerList;
        }

        @Override
        protected List<Trailer> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://api.themoviedb.org/3/movie/" + params[0] + "/videos?api_key=" + API_KEY);

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
        protected void onPostExecute(List<Trailer> trailers) {
            trailerList = trailers;

            if (trailerList.size() == 0) {

                Toast.makeText(mContext, "No Trailers", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class DownloadReviews extends AsyncTask<String, Void, List<Review>> {

        private String LOG_TAG = DownloadReviews.class.getSimpleName();
        private String API_KEY = getString(R.string.api_key);
        private String movieJSONStr;

        private Context mContext;

        public DownloadReviews(Context context) {
            mContext = context;
        }


        private List<Review> movieData(String parsedJSONStr) throws JSONException {

            final String dbResults = "results";
            final String id = "id";
            final String dbPoster = "url";
            final String dbAuthor = "author";
            final String dbContent = "content";

            List<Review> reviewList = new ArrayList<>();
            final RealmList<Review> reviewRealmList = new RealmList<>();

            JSONObject jsonObject = new JSONObject(parsedJSONStr);
            JSONArray jsonArray = jsonObject.getJSONArray(dbResults);

            final String movieID = jsonObject.getString(id);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject reviewObject = jsonArray.getJSONObject(i);

                final String reviewID = reviewObject.getString(id);
                final String author = reviewObject.getString(dbAuthor);
                final String content = reviewObject.getString(dbContent);

                Review review = new Review();

                review.setReviewMovieID(movieID);
                review.setReviewID(reviewID);
                review.setAuthor(author);
                review.setContent(content);

                Realm realm = Realm.getInstance(mContext);
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmQuery<Review> reviewRealmQuery = realm.where(Review.class);
                        RealmResults<Review> realmResults = reviewRealmQuery.equalTo("reviewID", reviewID).findAll();

                        if(realmResults.size() ==0){
                            addRealmReviews(realm);
                        }else{
                            for(Review review1:realmResults){
                                Log.i("REALM ID", review1.getReviewID());
                                if(!(review1.getReviewID().equals(reviewID))){
                                    addRealmReviews(realm);
                                }
                            }
                        }
                    }

                    void addRealmReviews(Realm realm1){
                        Review realmReview = realm1.createObject(Review.class);
                        realmReview.setReviewMovieID(movieID);
                        realmReview.setReviewID(reviewID);
                        realmReview.setAuthor(author);
                        realmReview.setContent(content);
                        reviewRealmList.add(realmReview);
                    }
                });

                reviewList.add(review);
            }

            return reviewList;
        }

        @Override
        protected List<Review> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://api.themoviedb.org/3/movie/" + params[0] + "/reviews?api_key=" + API_KEY);

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
        protected void onPostExecute(List<Review> reviews) {
            reviewList = reviews;

            if (reviewList.size() == 0) {
                Toast.makeText(mContext, "No Reviews", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class DownloadMovieInfo extends AsyncTask<String, Void, String> {

        private String LOG_TAG = DownloadMovieInfo.class.getSimpleName();
        private String API_KEY = getString(R.string.api_key);
        private String movieJSONStr;

        private Context mContext;

        public DownloadMovieInfo(Context context) {
            mContext = context;
        }


        private String movieData(String parsedJSONStr) throws JSONException {

            final String id = "id";
            final String dbRuntime = "runtime";


            JSONObject jsonObject = new JSONObject(parsedJSONStr);

//            Get a Whole load of stuff in the future
//            Only compromise is the use of the radio to get one String Object
//            Will look for alternative

            return jsonObject.getString(dbRuntime);
        }

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://api.themoviedb.org/3/movie/" + params[0] + "?api_key=" + API_KEY);

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
        protected void onPostExecute(String runtime) {

            //runtime to hours and mins
            int mins = Integer.parseInt(runtime);
            int hours = mins/60;
            int minutes = mins % 60;

            String time = hours +"."+minutes+"hrs";

            runtimeTV.setText(time);
        }
    }
}
