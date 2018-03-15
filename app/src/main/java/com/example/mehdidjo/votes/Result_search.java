package com.example.mehdidjo.votes;

import android.app.LoaderManager;
import android.content.ContentUris;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mehdidjo.votes.data.VoteContract;

public class Result_search extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    VoteCursorAdapter mCursorAdapter;
    private static final int PET_LOADER=0;
    private String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalaog);

        Intent intent = getIntent();
        search= intent.getExtras().getString("recherch");
        search= search.trim();
        setTitle(search);



       FloatingActionButton fab= (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        ListView listView = (ListView) findViewById(R.id.list);
        mCursorAdapter = new VoteCursorAdapter(this , null);
        listView.setAdapter(mCursorAdapter);

        View empyView = findViewById(R.id.empty);
        listView.setEmptyView(empyView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i,final long id) {
                Intent intent = new Intent(Result_search.this , FormActivity.class);
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
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {


        String selection = VoteContract.VoteEntry.COLUMN_VOTE_NAME+" LIKE?";
        String [] choix0={search+"%"};

        String [] projection = {
                VoteContract.VoteEntry._ID,
                VoteContract.VoteEntry.COLUMN_VOTE_NAME,
                VoteContract.VoteEntry.COLUMN_VOTE_LASTNAME,
                VoteContract.VoteEntry.COLUMN_VOTE_CHOIX,
                VoteContract.VoteEntry.COLUMN_VOTE_EMAIL
        };
        return new CursorLoader(this , VoteContract.VoteEntry.CONTENT_URI ,projection ,selection ,choix0,null);
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
}
