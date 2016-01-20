package com.brightlight.padzmj.frontpage.Model.MySQL;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by PadzMJ on 23/12/2015.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.brightlight.padzmj.frontpage";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String USER_PATH = "users";
    public static final String MOVIE_PATH = "movies";

    public static final int DATABASE_VERSION = 2;

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";

        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_USERNAME = "username";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(USER_PATH).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + USER_PATH;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + USER_PATH;
    }

    public static class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_BACKDROP = "backdrop";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_RELEASE_YEAR = "year";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RUNTIME = "runtime";


        public static final String COLUMN_MOVIE_SETTING = "movie_setting";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(MOVIE_PATH).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIE_PATH;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIE_PATH;

        public static Uri buildMovie() {
            return CONTENT_URI.buildUpon().build();
        }
    }
}
