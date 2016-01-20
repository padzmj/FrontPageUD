package com.brightlight.padzmj.frontpage.Model.MySQL;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by PadzMJ on 29/12/2015.
 */
public class DownloadExtendedMoviesDB extends AsyncTask<String, Void, String> {

    final String MOVIE_PARAM = "movie";
    final String API_KEY_PARAM = "?api_key";
    private String LOG_TAG = DownloadExtendedMoviesDB.class.getSimpleName();
    private String API_KEY = "6f8b815f9761bc4221829080d402892b";
    private String movieJSONStr;
    private Context mContext;

    public DownloadExtendedMoviesDB(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("RUN_TIME", "Pre Execute");
    }

    private String movieData(String parsedJSONStr) throws JSONException {

        final String posterURL = "http://image.tmdb.org/t/p/w500";

        final String dbRuntime = "runtime";

        JSONObject jsonObject = new JSONObject(parsedJSONStr);


        String movieRuntime = jsonObject.getString("runtime");


        Log.i("RUN_TIME", movieRuntime);

        return movieRuntime;
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
    protected void onPostExecute(String movie) {
        Log.i("RUN_TIME", movie);
        Toast.makeText(mContext, movie, Toast.LENGTH_LONG).show();
        Log.i("RUN_TIME", "Post Execute");
    }
}