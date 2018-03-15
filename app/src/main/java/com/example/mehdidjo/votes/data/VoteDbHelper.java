package com.example.mehdidjo.votes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mehdi Djo on 08/11/2017.
 */

public class VoteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "votedb.db";
    private static final int DATABASE_VERSION = 2;

    public VoteDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_VOTE_TABLE = "CREATE TABLE "+ VoteContract.VoteEntry.TABLE_NAME +" ("
                + VoteContract.VoteEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + VoteContract.VoteEntry.COLUMN_VOTE_NAME + " TEXT NOT NULL,"
                + VoteContract.VoteEntry.COLUMN_VOTE_LASTNAME + " TEXT NOT NULL,"
                + VoteContract.VoteEntry.COLUMN_VOTE_EMAIL + " TEXT NOT NULL,"
                + VoteContract.VoteEntry.COLUMN_VOTE_DATE + " TEXT NOT NULL,"
                + VoteContract.VoteEntry.COLUMN_VOTE_CHOIX + " INTEGER NOT NULL );";

        db.execSQL(SQL_CREATE_VOTE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
