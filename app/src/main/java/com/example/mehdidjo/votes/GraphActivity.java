package com.example.mehdidjo.votes;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mehdidjo.votes.data.VoteContract;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {
    int nbrv=0;
    int result1=0;
    int result2=0;
    int result3=0;
    int result4=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        requette();

        int tabChart [] = {result1,result2,result3,result4};
        String choixChart [] = {"Vote 0","Vote 1","Vote 2","Vote 3" };

        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i=0 ; i<tabChart.length;i++){
            pieEntries.add(new PieEntry(tabChart[i] , choixChart[i]));
        }
        PieDataSet dataSet = new PieDataSet(pieEntries , "Graph des Votes");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);


        PieData data = new PieData(dataSet);

        PieChart chart = (PieChart) findViewById(R.id.chart);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();

       //
    }

    private void requette() {

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
    }


}
