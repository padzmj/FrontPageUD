package com.brightlight.padzmj.frontpage.Movies.Detail;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
    String id, posterPath, backdropPath, movieTitle, releaseYear, movieSynopsis, userRating, runTime;
    List<Review> reviewList = new ArrayList<>();
    List<Trailer> trailerList = new ArrayList<>();

    TextView title, release, synopsis, rating, runtime, reviewsTextViewTitle, trailersTextViewTitle;
    ImageView poster, backdropPoster, ratingIcon, runtimeIcon;
    RecyclerView recyclerView;

    View view;

    ArrayList<Object> items = new ArrayList<>();

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

        view = (View) rootView.findViewById(R.id.movie_detailed_coordinatorLayout);
        title = (TextView) rootView.findViewById(R.id.detail_movie_title);
        release = (TextView) rootView.findViewById(R.id.detail_release_year);
        synopsis = (TextView) rootView.findViewById(R.id.detail_synopsis);
        rating = (TextView) rootView.findViewById(R.id.detail_user_rating);
        runtime = (TextView) rootView.findViewById(R.id.detail_runtime);
        poster = (ImageView) rootView.findViewById(R.id.detail_poster);
        backdropPoster = (ImageView) rootView.findViewById(R.id.detail_backdrop_poster);
        ratingIcon = (ImageView) rootView.findViewById(R.id.detail_user_rating_icon);
        runtimeIcon = (ImageView) rootView.findViewById(R.id.detail_runtime_icon);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.contentRecyclerView);
        //reviewsTextViewTitle = (TextView) findViewById(R.id.reviewsTextViewTitle);
        //trailersTextViewTitle = (TextView) findViewById(R.id.trailersTextViewTitle);

        String reviewsTitle = getResources().getString(R.string.reviewsHeaderTitle);
        String trailersTitle = getResources().getString(R.string.trailersHeaderTitle);
        String space = getResources().getString(R.string.space_placeholder);

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

            Realm realm = Realm.getInstance(mContext);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmQuery<Trailer> trailerRealmQuery = realm.where(Trailer.class);
                    RealmQuery<Review> reviewRealmQuery = realm.where(Review.class);

                    RealmResults<Trailer> trailerRealmResults = trailerRealmQuery.equalTo("trailerMovieID", id).findAll();
                    RealmResults<Review> reviewRealmResults = reviewRealmQuery.equalTo("reviewMovieID", id).findAll();

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

//                if((reviewRealmResults.size()==0)&&(trailerRealmResults.size()==0)){
//                    trailersReviews(id);
//                }else{
//
//
////                    for(Review review1:reviewRealmResults){
////                        if(review1.getReviewMovieID().equals(id)){
////                            reviewList.add(review1);
////                        }
////                    }
////                    for(Trailer trailer:trailerRealmResults){
////                        Log.i("HEREWE", trailer.getTrailerURL());
////                        if(trailer.getTrailerMovieID().equals(id)){
////                            trailerList.add(trailer);
////                        }
////                    }
//                }
                }
            });
        }
//        Toast.makeText(mContext, items.size()+" Reviews & Trailers", Toast.LENGTH_LONG).show();

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mContext, items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        if (userRating != null) ratingIcon.setBackgroundResource(R.drawable.ic_star_black_18dp);
        //runtimeIcon.setBackgroundResource(R.drawable.ic_access_time_black_18dp);
        title.setText(movieTitle);
        release.setText(releaseYear);
        synopsis.setText(this.movieSynopsis);
        rating.setText(userRating);
        //runtime.setText(runTime);
        //reviewsTextViewTitle.setText(reviewsTitle);
        //trailersTextViewTitle.setText(trailersTitle);

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
                //trailersTextViewTitle.setVisibility(View.VISIBLE);

//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//
//
//                TrailersFragment trailersFragment = TrailersFragment.newInstance(mContext, trailerList);
//                fragmentTransaction.replace(R.id.trailersFrameLayout, trailersFragment);
//                fragmentTransaction.commit();
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
                //reviewsTextViewTitle.setVisibility(View.VISIBLE);

//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//
//                ReviewsFragment reviewsFragment = ReviewsFragment.newInstance(mContext, reviewList);
//                fragmentTransaction.replace(R.id.reviewsFrameLayout, reviewsFragment);
//                fragmentTransaction.commit();
            }
        }
    }
}