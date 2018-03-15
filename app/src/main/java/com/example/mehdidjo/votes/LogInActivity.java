package com.example.mehdidjo.votes;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mehdidjo.votes.data.VoteContract;
import com.example.mehdidjo.votes.data.VoteDbHelper;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);



        Button button = (Button) findViewById(R.id.btn_signup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                checkmail();

            }
        });


    }

    private void checkmail() {
        EditText emailView = (EditText) findViewById(R.id.input_email);
        String email = emailView.getText().toString().trim();
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            emailView.setError("This field is required");
            focusView = emailView;
            focusView.requestFocus();
        } else if (!isEmailValid(email)) {
            emailView.setError("This email address is invalid");
            focusView = emailView;
            focusView.requestFocus();
        } else if (!emailExist(email)){
            Intent intent = new Intent(LogInActivity.this , FormActivity.class);
            intent.putExtra("email" , email);
            startActivity(intent);
        } else {
            long id = emailExistWithId(email);
            if (id != -1) {
                Intent intent = new Intent(LogInActivity.this, FormActivity.class);
                Uri curentVoteUri = ContentUris.withAppendedId(VoteContract.VoteEntry.CONTENT_URI, id);
                intent.setData(curentVoteUri);
                startActivity(intent);
            }
            Toast.makeText(LogInActivity.this ,"Ereur",Toast.LENGTH_SHORT).show();
        }

    }


    private boolean emailExist( String email){

        VoteDbHelper mVoteDbHelper;
        mVoteDbHelper =new VoteDbHelper(LogInActivity.this);
        SQLiteDatabase database = mVoteDbHelper.getReadableDatabase();

        String [] projection = {
                VoteContract.VoteEntry._ID,

        };
        String selection = VoteContract.VoteEntry.COLUMN_VOTE_EMAIL+" LIKE?";
        String[] selectionArgs = new String[] { email };

        Cursor cursor = database.query(VoteContract.VoteEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
        if(cursor.getCount() !=0)
            return true;

        return false;
    }


    private long emailExistWithId( String email){

        VoteDbHelper mVoteDbHelper;
        mVoteDbHelper =new VoteDbHelper(LogInActivity.this);
        SQLiteDatabase database = mVoteDbHelper.getReadableDatabase();

        String [] projection = {
                VoteContract.VoteEntry._ID,

        };
        String selection = VoteContract.VoteEntry.COLUMN_VOTE_EMAIL+" LIKE?";
        String[] selectionArgs = new String[] { email };

        Cursor cursor = database.query(VoteContract.VoteEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
        if(cursor.moveToFirst()) {
            //return 1;
            int idColumnIndex = cursor.getColumnIndex(VoteContract.VoteEntry._ID);
            String ids = cursor.getString(0);
            long id = Long.parseLong(ids);
            Log.v("Login Activity" ,"emailExistWithId----> "+id);
            return id;
        }

        return -1;
    }
    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".") ;
    }
}
