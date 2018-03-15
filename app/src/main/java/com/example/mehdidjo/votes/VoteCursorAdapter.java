package com.example.mehdidjo.votes;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.mehdidjo.votes.data.VoteContract;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mehdi Djo on 08/11/2017.
 */

public class VoteCursorAdapter extends CursorAdapter {


    public VoteCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.vote_list_item ,viewGroup , false) ;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameView = (TextView) view.findViewById(R.id.name);
        TextView lastNameView = (TextView) view.findViewById(R.id.lastName);
        TextView choixView = (TextView) view.findViewById(R.id.vote);
       // TextView emailView = (TextView) view.findViewById(R.id.email);
        TextView dateView = (TextView) view.findViewById(R.id.date);
        TextView timeView = (TextView) view.findViewById(R.id.time);

        int nameColumnIndex = cursor.getColumnIndex(VoteContract.VoteEntry.COLUMN_VOTE_NAME);
        int lastNameColumnIndex = cursor.getColumnIndex(VoteContract.VoteEntry.COLUMN_VOTE_LASTNAME);
        int choixColumnIndex = cursor.getColumnIndex(VoteContract.VoteEntry.COLUMN_VOTE_CHOIX);
        int emailColumnIndex = cursor.getColumnIndex(VoteContract.VoteEntry.COLUMN_VOTE_EMAIL);
        int dateColumnIndex = cursor.getColumnIndex(VoteContract.VoteEntry.COLUMN_VOTE_DATE);

        String name = cursor.getString(nameColumnIndex);
        String lastName = cursor.getString(lastNameColumnIndex);
        String choix = cursor.getString(choixColumnIndex);
        String email = cursor.getString(emailColumnIndex);
//        String unixTime = cursor.getString(dateColumnIndex);

      ///  Log.v("Vote cursor" ," time ----------->"+unixTime);

        long Time = System.currentTimeMillis() ;
        Date dateobject = new Date(Time);
        String formattedDate = formatDate(dateobject);
        String formatedTime = formatTime(dateobject);


        nameView.setText(name);
        lastNameView.setText(lastName);
        choixView.setText("Vote "+choix);
        //emailView.setText(email);
        dateView.setText(formattedDate);
        timeView.setText(formatedTime);

    }

    private String formatTime(Date dateobject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateobject);
    }

    private String formatDate(Date dateobject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd LLL yyyy");
        return dateFormat.format(dateobject);
    }
}
