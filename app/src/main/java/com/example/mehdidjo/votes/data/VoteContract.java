package com.example.mehdidjo.votes.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mehdi Djo on 08/11/2017.
 */

public class VoteContract {

    private VoteContract(){}

   public static final String CONTENT_AUTHORITY = "com.example.android.votes";
    public static  final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static  final   String PATH_VOTES = "votes";

    public static final class VoteEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI , PATH_VOTES );

      public static final String  TABLE_NAME = "vote";
      public static final String  _ID = BaseColumns._ID;
      public static final String  COLUMN_VOTE_NAME = "name";
        public static final String  COLUMN_VOTE_LASTNAME = "LastName";
        public static final String  COLUMN_VOTE_EMAIL = "email";
        public static final String  COLUMN_VOTE_CHOIX = "choix";
        public static final String  COLUMN_VOTE_DATE = "date";


    }

}
