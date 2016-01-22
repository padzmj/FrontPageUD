package com.brightlight.padzmj.frontpage.Movies.Detail;

import com.brightlight.padzmj.frontpage.Movies.Reviews.Review;
import com.brightlight.padzmj.frontpage.Movies.Trailers.Trailer;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by PadzMJ on 22/10/2015.
 */
public class Movie extends RealmObject{

    @PrimaryKey
    private String id;

    private String posterPath;
    private String backdropPath;
    private String title, releaseYear, synopsis, userRating, runtime;

    private boolean favouriteMovie;
    private RealmList<Review> movieReviews;
    private RealmList<Trailer> trailers;


    public Movie(){
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String poster_path) {
        this.posterPath = poster_path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public RealmList<Review> getMovieReviews() {
        return movieReviews;
    }

    public void setMovieReviews(RealmList<Review> movieReviews) {
        this.movieReviews = movieReviews;
    }

    public RealmList<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(RealmList<Trailer> trailers) {
        this.trailers = trailers;
    }

    public boolean isFavouriteMovie() {
        return favouriteMovie;
    }

    public void setFavouriteMovie(boolean favouriteMovie) {
        this.favouriteMovie = favouriteMovie;
    }

}
