package com.example.mehdidjo.votes;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mehdidjo.votes.data.VoteContract;
import com.example.mehdidjo.votes.data.VoteDbHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText nameView;
    private EditText lastNameView;
    private EditText emailView;
    private Spinner spinnerView;
    private int vote=0;
    private  Uri mCurrentVoteUri;
    private boolean voteHasChange=false;

    private static final int EXISTING_Vote_LOADER = 0;

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            voteHasChange=true;
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        nameView = (EditText) findViewById(R.id.nameView);
        lastNameView = (EditText) findViewById(R.id.lastNameView);
        emailView = (EditText) findViewById(R.id.emailView);
        spinnerView = (Spinner) findViewById(R.id.spinnerView);

        nameView.setOnTouchListener(touchListener);
        lastNameView.setOnTouchListener(touchListener);
        emailView.setOnTouchListener(touchListener);
        spinnerView.setOnTouchListener(touchListener);

        setupSpinner();

        Intent intent = getIntent();
        mCurrentVoteUri=intent.getData();

        if (mCurrentVoteUri == null){
            invalidateOptionsMenu();

            String email ="";
            Intent intentEmail = getIntent();
            email= intentEmail.getExtras().getString("email");
            emailView.setText(email);


        }else {
            setTitle("Update vote");
            getLoaderManager().initLoader(EXISTING_Vote_LOADER, null, this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.form_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {

            case R.id.save:
                save();

                return true;

            case R.id.delete:
                delateVote();

                return true;

            case R.id.home:
                if (!voteHasChange){
                    NavUtils.navigateUpFromSameTask(FormActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener exitButtonclickListner = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //confirme la sortie
                        NavUtils.navigateUpFromSameTask(FormActivity.this);
                    }
                };

                showUnsavedChangesDialog(exitButtonclickListner);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener exitButtonclickListner) {

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit");
        builder.setPositiveButton("Exit",exitButtonclickListner);
        builder.setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface!=null){
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void delateVote() {
        int rowDeleted = getContentResolver().delete(mCurrentVoteUri, null, null);
        if (rowDeleted == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, "delete vote failed",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(this, "delete vote successful",
                    Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void save() {

        emailView.setError(null);
        nameView.setError(null);
        lastNameView.setError(null);

        boolean cancel=false;
        View focusView = null;

        String email = emailView.getText().toString().trim();
        String name = nameView.getText().toString().trim();
        String lastName = lastNameView.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailView.setError("This field is required");
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError("This email address is invalid");
            focusView = emailView;
            cancel = true;
        }else if (emailExist(email) && mCurrentVoteUri==null){
            emailView.setError("This email exist");
            focusView = emailView;
            cancel = true;
        }
        if (TextUtils.isEmpty(lastName)) {
            lastNameView.setError("This field is required");
            focusView = lastNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(name)) {
            nameView.setError("This field is required");
            focusView = nameView;
            cancel = true;
        }

        if(cancel){
             focusView.requestFocus();
        }else {
            saveVote();
            Intent intent2 = new Intent(FormActivity.this , CatalaogActivity.class);
            startActivity(intent2);
        }
    }


    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".") ;
    }

    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_vote_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        spinnerView.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        spinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.vote1))) {
                        vote =1;
                    } else if (selection.equals(getString(R.string.vote2))) {
                        vote =2;
                    } else if (selection.equals(getString(R.string.vote3))) {
                        vote =3;
                    }else {
                        vote =0; // UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vote =0; // PetEntry.GENDER_UNKNOWN;
            }
        });
    }
    private void saveVote(){

        String emails = emailView.getText().toString().trim();
        String names = nameView.getText().toString().trim();
        String lastNames = lastNameView.getText().toString().trim();

        long unixTime = System.currentTimeMillis() ;
        Date dateobject = new Date(unixTime);
        String formattedDate = formatDate(dateobject);


        ContentValues values = new ContentValues();
        values.put(VoteContract.VoteEntry.COLUMN_VOTE_NAME , names);
        values.put(VoteContract.VoteEntry.COLUMN_VOTE_LASTNAME , lastNames);
        values.put(VoteContract.VoteEntry.COLUMN_VOTE_EMAIL , emails);
        values.put(VoteContract.VoteEntry.COLUMN_VOTE_DATE , formattedDate);
        values.put(VoteContract.VoteEntry.COLUMN_VOTE_CHOIX , vote);

        if(mCurrentVoteUri == null) {

            Uri newUri = getContentResolver().insert(VoteContract.VoteEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, "Error with saving Vote", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Vote saved", Toast.LENGTH_SHORT).show();
            }
        }else{
            int newUri = getContentResolver().update(mCurrentVoteUri,values,null,null);

            if (newUri == 0) {
                Toast.makeText(this, "Error with updating Vote", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Vote updated", Toast.LENGTH_SHORT).show();
            }

        }

    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentVoteUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String [] projection = {
                VoteContract.VoteEntry._ID,
                VoteContract.VoteEntry.COLUMN_VOTE_EMAIL,
                VoteContract.VoteEntry.COLUMN_VOTE_NAME,
                VoteContract.VoteEntry.COLUMN_VOTE_LASTNAME,
                VoteContract.VoteEntry.COLUMN_VOTE_CHOIX,
        };
        return new CursorLoader(this , mCurrentVoteUri ,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor.moveToFirst()){
            int nameColumnIndex = cursor.getColumnIndex(VoteContract.VoteEntry.COLUMN_VOTE_NAME);
            int lastNameColumnIndex = cursor.getColumnIndex(VoteContract.VoteEntry.COLUMN_VOTE_LASTNAME);
            int choixColumnIndex = cursor.getColumnIndex(VoteContract.VoteEntry.COLUMN_VOTE_CHOIX);
            int emailColumnIndex = cursor.getColumnIndex(VoteContract.VoteEntry.COLUMN_VOTE_EMAIL);
            //int dateColumnIndex = cursor.getColumnIndex(VoteContract.VoteEntry.COLUMN_VOTE_DATE);

            String name = cursor.getString(nameColumnIndex);
            String lastName = cursor.getString(lastNameColumnIndex);
            String choix = cursor.getString(choixColumnIndex);
            String email = cursor.getString(emailColumnIndex);
           // String date = cursor.getString(dateColumnIndex);

            int choixi = Integer.parseInt(choix);
            nameView.setText(name);
            lastNameView.setText(lastName);
            emailView.setText(email);
            switch (choixi){
                case 1 : spinnerView.setSelection(1); break;
                case 2 : spinnerView.setSelection(2); break;
                case 3 : spinnerView.setSelection(3); break;
                default: spinnerView.setSelection(0); break;
            }

        }

    }

    @Override
    public void onBackPressed() {
        if (!voteHasChange) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener exitButtonclickListner = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //confirme la sortie
                NavUtils.navigateUpFromSameTask(FormActivity.this);
            }
        };

        showUnsavedChangesDialog(exitButtonclickListner);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        nameView.setText("");
        lastNameView.setText("");
        emailView.setText("");
        spinnerView.setSelection(0);
    }

    private String formatDate(Date dateobject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd LLL yyyy");
        return dateFormat.format(dateobject);
    }

    private boolean emailExist( String email){

        VoteDbHelper mVoteDbHelper;
        mVoteDbHelper =new VoteDbHelper(FormActivity.this);
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
}
