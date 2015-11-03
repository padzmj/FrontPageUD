package com.brightlight.padzmj.frontpage.Movies;

/**
 * Created by PadzMJ on 22/10/2015.
 */
public class Movie {
    String id, poster_path, title, releaseYear, synopsis, userRating;

    public Movie(){

    }

    public String getID() {
        return id;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setID(String id) {
        this.id = id;
    }

    public void setPosterPath(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }
}
