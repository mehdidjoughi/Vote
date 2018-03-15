package com.example.mehdidjo.votes;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mehdidjo.votes.data.VoteContract;
import com.example.mehdidjo.votes.data.VoteDbHelper;

public class AnalyticsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_Vote_LOADER = 0;

    private int nbrloader=0;
    private VoteDbHelper mVoteDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

            getLoaderManager().initLoader(EXISTING_Vote_LOADER, null, this);

       requette();
        Button button = (Button) findViewById(R.id.graph);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnalyticsActivity.this ,GraphActivity.class );
                startActivity(intent);
            }
        });
    }
    private void requette(){

        int nbrv=0;
        int result1=0;
        int result2=0;
        int result3=0;
        int result4=0;
        String [] projection={
                VoteContract.VoteEntry._ID
        };
        String selection = VoteContract.VoteEntry.COLUMN_VOTE_CHOIX+"=?";
        String [] choix0={"0"};
        String [] choix1={"1"};
        String [] choix2={"2"};
        String [] choix3={"3"};
       Cursor cursor = getContentResolver().query(VoteContract.VoteEntry.CONTENT_URI,projection,selection,choix0,null);
        if (cursor.moveToFirst()){
             result1 = cursor.getCount();
            cursor.close();
        }

         cursor = getContentResolver().query(VoteContract.VoteEntry.CONTENT_URI,projection,selection,choix1,null);
        if (cursor.moveToFirst()){
            result2 = cursor.getCount();
            cursor.close();
        }
         cursor = getContentResolver().query(VoteContract.VoteEntry.CONTENT_URI,projection,selection,choix2,null);
        if (cursor.moveToFirst()){
            result3 = cursor.getCount();
            cursor.close();
        }
         cursor = getContentResolver().query(VoteContract.VoteEntry.CONTENT_URI,projection,selection,choix3,null);
        if (cursor.moveToFirst()){
            result4 = cursor.getCount();
            cursor.close();
        }
        cursor = getContentResolver().query(VoteContract.VoteEntry.CONTENT_URI,projection,null,null,null);
        if (cursor.moveToFirst()){
            nbrv = cursor.getCount();
            cursor.close();
        }
        Log.v("Analitics","R1---------->"+result1+"R2---------->"+result2+"R3---------->"+result3+"R4---------->"+result4);

        TextView textViewChoix1 = (TextView) findViewById(R.id.ch1);
        String resultchoix1 = String.valueOf(result1);
        textViewChoix1.setText(resultchoix1);

        TextView textViewChoix2 = (TextView) findViewById(R.id.ch2);
        String resultchoix2 = String.valueOf(result2);
        textViewChoix2.setText(resultchoix2);

        TextView textViewChoix3 = (TextView) findViewById(R.id.ch3);
        String resultchoix3 = String.valueOf(result3);
        textViewChoix3.setText(resultchoix3);

        TextView textViewChoix4 = (TextView) findViewById(R.id.ch4);
        String resultchoix4 = String.valueOf(result4);
        textViewChoix4.setText(resultchoix4);
        if (nbrv !=0) {

            int pr1 = (int) (result1 * 100) / nbrv;
            TextView pri1view1 = (TextView) findViewById(R.id.ch1p);
            pri1view1.setText(String.valueOf(pr1) + "%");

            int pr2 = (int) (result2 * 100) / nbrv;
            TextView pri1view2 = (TextView) findViewById(R.id.ch2p);
            pri1view2.setText(String.valueOf(pr2) + "%");

            int pr3 = (int) (result3 * 100) / nbrv;
            TextView pri1view3 = (TextView) findViewById(R.id.ch3p);
            pri1view3.setText(String.valueOf(pr3) + "%");

            int pr4 = (int) (result4 * 100) / nbrv;
            TextView pri1view4 = (TextView) findViewById(R.id.ch4p);
            pri1view4.setText(String.valueOf(pr4) + "%");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String [] projection1={
                VoteContract.VoteEntry._ID
        };
        return new CursorLoader(this,VoteContract.VoteEntry.CONTENT_URI ,projection1,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        int nbrVote=0;

        if(cursor.moveToFirst()){
            nbrVote = cursor.getCount();
        }
        String numberVote = String.valueOf(nbrVote);
        TextView numberVoteView = (TextView) findViewById(R.id.nbrvote);
        numberVoteView.setText(numberVote);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
