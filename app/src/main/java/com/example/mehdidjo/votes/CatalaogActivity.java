package com.example.mehdidjo.votes;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mehdidjo.votes.data.VoteContract;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CatalaogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {


    private FloatingActionButton fab;
    private static final int PET_LOADER=0;

    VoteCursorAdapter mCursorAdapter;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalaog);

        ListView listView = (ListView) findViewById(R.id.list);
        mCursorAdapter = new VoteCursorAdapter(this , null);
        listView.setAdapter(mCursorAdapter);

        View empyView = findViewById(R.id.empty);
        listView.setEmptyView(empyView);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalaogActivity.this , LogInActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i,final long id) {
                Intent intent = new Intent(CatalaogActivity.this , FormActivity.class);
                Uri curentVoteUri = ContentUris.withAppendedId(VoteContract.VoteEntry.CONTENT_URI ,id);
                intent.setData(curentVoteUri);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id) {
                showDeleteConfirmationDialog(id);
                return true;
            }
        });


        getLoaderManager().initLoader(PET_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu , menu);

        MenuItem item = menu.findItem(R.id.SearchMenu);
//
        searchView = (SearchView) item.getActionView();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(CatalaogActivity.this , Result_search.class);
                intent.putExtra("recherch" , query);
                startActivity(intent);

                Log.v("catalaog" , "=============onQueryTextSubmit-->");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.v("catalaog" , "=============onQueryTextChange");
                mCursorAdapter.getFilter().filter(newText);

                //mCursorAdapter.getFilterQueryProvider().runQuery(newText);
                return false;
            }
        });

//        MenuItem item2 = menu.findItem(R.id.Settings);
//        Intent settingsintent = new Intent(this , SettingsActivity.class);
//        startActivity(settingsintent);


        return  super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {

//            case R.id.SearchMenu:
//                return true;
            case R.id.Add :
                //insertVote();
                Intent intentsearch = new Intent(CatalaogActivity.this , LogInActivity.class);
                startActivity(intentsearch);

                return true;

            case R.id.analytics:

                Intent intent = new Intent(CatalaogActivity.this , AnalyticsActivity.class);
                startActivity(intent);

                return true;
            case R.id.deleteMenu:
                delateAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void delateAll() {

        int rowdeleted = getContentResolver().delete(VoteContract.VoteEntry.CONTENT_URI , null ,null);
    }

    private void search(Menu menu){
        MenuItem item = menu.findItem(R.id.SearchMenu);
        SearchView  searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.v("CatalogeActivity","onQueryTextSubmit : "+query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.v("CatalogeActivity","onQueryTextChange : "+newText);
                return false;
            }
        });
    }

    private void insertVote(){

        long unixTime = System.currentTimeMillis();
        Date dateobject = new Date(unixTime);
        String formattedDate = formatDate(dateobject);
        String formattedTime = formatTime(dateobject);

        ContentValues values = new ContentValues();
        values.put(VoteContract.VoteEntry.COLUMN_VOTE_NAME , "Mehdi");
        values.put(VoteContract.VoteEntry.COLUMN_VOTE_LASTNAME , "Djoughi");
        values.put(VoteContract.VoteEntry.COLUMN_VOTE_CHOIX , 0);
        values.put(VoteContract.VoteEntry.COLUMN_VOTE_DATE , formattedDate+"|"+formattedTime);
        values.put(VoteContract.VoteEntry.COLUMN_VOTE_EMAIL , "Mehdi@djoughi.com");

        Uri newUri = getContentResolver().insert(VoteContract.VoteEntry.CONTENT_URI ,values);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String [] projection = {
                VoteContract.VoteEntry._ID,
                VoteContract.VoteEntry.COLUMN_VOTE_NAME,
                VoteContract.VoteEntry.COLUMN_VOTE_LASTNAME,
                VoteContract.VoteEntry.COLUMN_VOTE_CHOIX,
                VoteContract.VoteEntry.COLUMN_VOTE_EMAIL
        };
        return new CursorLoader(this , VoteContract.VoteEntry.CONTENT_URI ,projection ,null ,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    private void showDeleteConfirmationDialog(final long idVote) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete this vote");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                delateVoteDialog(idVote);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void delateVoteDialog( long id){

        Uri curentVoteUri = ContentUris.withAppendedId(VoteContract.VoteEntry.CONTENT_URI ,id);
        int rowDeleted = getContentResolver().delete(curentVoteUri, null, null);
        if (rowDeleted == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, "delete vote failed",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(this, "delete vote successful",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private String formatDate(Date dateobject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd LLL yyyy");
        return dateFormat.format(dateobject);
    }
    private String formatTime(Date dateobject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateobject);
    }

}
