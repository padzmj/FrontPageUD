package com.brightlight.padzmj.frontpage.Model.MySQL;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by PadzMJ on 27/12/2015.
 */
public class MovieProvider extends ContentProvider {

    private static final int MOVIE = 100;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int USER = 101;
    private static final SQLiteQueryBuilder sqlLiteQueryBuilderBuilder;
    private static final String movieSettingSelection = MovieContract.MovieEntry.TABLE_NAME +
            "." + MovieContract.MovieEntry.COLUMN_MOVIE_SETTING + "=?";

    static {
        sqlLiteQueryBuilderBuilder = new SQLiteQueryBuilder();
        /*sqlLiteQueryBuilderBuilder.setTables(
                MovieContract.MovieEntry.TABLE_NAME + "INNER JOIN" +
                         MovieContract.UserEntry.TABLE_NAME +
                        "ON" + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID +
                        "=" + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry._ID);*/

        sqlLiteQueryBuilderBuilder.setTables(MovieContract.MovieEntry.TABLE_NAME);
    }

    private MovieDBHelper mOpenHelper;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.MOVIE_PATH, MOVIE);
        //matcher.addURI(authority, MovieContract.USER_PATH, USER);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;

        retCursor = mOpenHelper.getReadableDatabase().query(
                MovieContract.MovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;

        /*
        switch(sUriMatcher.match(uri)){
            case(MOVIE):{


                break;
            }

            case (USER):{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown Uri" +uri);
        }*/
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case USER:
                return MovieContract.UserEntry.CONTENT_TYPE;
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);

        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase database = mOpenHelper.getWritableDatabase();

        //final int match = sUriMatcher.match(uri);

        Uri returnUri;
        long _id = database.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);

        if (_id > 0)
            returnUri = MovieContract.MovieEntry.buildMovie();
        else
            throw new android.database.SQLException("Failed to insert row into " + uri);
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase database = mOpenHelper.getWritableDatabase();

        int rowsDeleted;

        rowsDeleted = database.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase database = mOpenHelper.getWritableDatabase();
        int rowsUpdated;

        rowsUpdated = database.update(MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase database = mOpenHelper.getWritableDatabase();
        database.beginTransaction();

        int returnCount = 0;

        try {
            for (ContentValues value : values) {
                long _id = database.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }
}
