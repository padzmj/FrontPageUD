package com.brightlight.padzmj.frontpage.Movies.Trailers;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by PadzMJ on 10/01/2016.
 */
public class Trailer extends RealmObject {

    @PrimaryKey
    private String trailerID;

    private String trailerMovieID, trailerURL, trailerKey;

    public String getTrailerURL() {
        return trailerURL;
    }

    public void setTrailerURL(String trailerURL) {
        this.trailerURL = trailerURL;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }

    public String getTrailerID() {
        return trailerID;
    }

    public void setTrailerID(String trailerID) {
        this.trailerID = trailerID;
    }

    public String getTrailerMovieID() {
        return trailerMovieID;
    }

    public void setTrailerMovieID(String trailerMovieID) {
        this.trailerMovieID = trailerMovieID;
    }
}
