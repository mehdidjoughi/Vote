package com.example.mehdidjo.votes.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


/**
 * Created by Mehdi Djo on 08/11/2017.
 */

public class VoteProvider extends ContentProvider {

    private static final int VOTE = 100;
    private static final int VOTE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(VoteContract.CONTENT_AUTHORITY , VoteContract.PATH_VOTES ,VOTE);
        sUriMatcher.addURI(VoteContract.CONTENT_AUTHORITY , VoteContract.PATH_VOTES+"/#" ,VOTE_ID);
    }

    private VoteDbHelper mVoteDbHelper;

    @Override
    public boolean onCreate() {
        mVoteDbHelper =new VoteDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query( Uri uri,  String[] projection,  String selection,  String[] selectionArgs,  String sortOrder) {

        SQLiteDatabase database = mVoteDbHelper.getReadableDatabase();
        Cursor cursor;
        int match =sUriMatcher.match(uri);
        switch (match){
            case VOTE :

                cursor = database.query(VoteContract.VoteEntry.TABLE_NAME ,projection , selection  ,selectionArgs ,null,null,sortOrder);
                break;
            case VOTE_ID :
                selection = VoteContract.VoteEntry._ID+"=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(VoteContract.VoteEntry.TABLE_NAME , projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver() ,uri);
        return cursor;
    }

    @Override
    public Uri insert( Uri uri,  ContentValues contentValues) {
        int match =sUriMatcher.match(uri);
        switch (match){
            case VOTE :
                return insertVote(uri,contentValues);
            default: throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

    }


    private Uri insertVote(Uri uri,  ContentValues contentValues){

        SQLiteDatabase database = mVoteDbHelper.getWritableDatabase();
        long id = database.insert(VoteContract.VoteEntry.TABLE_NAME , null ,contentValues);

        getContext().getContentResolver().notifyChange(uri ,null);

        return ContentUris.withAppendedId(uri,id);
    }



    @Override
    public int delete( Uri uri,  String selection,  String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase database = mVoteDbHelper.getWritableDatabase();
        int rowdeleted=0;
        switch (match){
            case VOTE:
               rowdeleted= database.delete(VoteContract.VoteEntry.TABLE_NAME,null ,null);
                break;

            case VOTE_ID:
                selection = VoteContract.VoteEntry._ID+"=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri)) };

                rowdeleted = database.delete(VoteContract.VoteEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:throw new IllegalArgumentException("Deletion is not supported for "+uri);
        }
        if (rowdeleted != 0){
           getContext().getContentResolver().notifyChange(uri ,null);
        }
        return rowdeleted;
    }

    @Override
    public int update( Uri uri,  ContentValues contentValues,  String selection,  String[] selectionArgs) {

        int match = sUriMatcher.match(uri);
        SQLiteDatabase database = mVoteDbHelper.getWritableDatabase();
        int rowUpdated=0;
        switch (match){
            case VOTE :

                rowUpdated =   database.update(VoteContract.VoteEntry.TABLE_NAME,contentValues,selection,selectionArgs);

                break;
            case VOTE_ID:
                selection= VoteContract.VoteEntry._ID+"=?";
                selectionArgs   = new String[] {  String.valueOf(ContentUris.parseId(uri)) };
                rowUpdated =   database.update(VoteContract.VoteEntry.TABLE_NAME,contentValues,selection,selectionArgs);

                break;

                default: new IllegalAccessError("Update is not supported for " +uri);
        }
        if (rowUpdated != 0){
            getContext().getContentResolver().notifyChange(uri , null);
        }
        return rowUpdated;
    }

    @Override
    public String getType( Uri uri) {
        return null;
    }
}
