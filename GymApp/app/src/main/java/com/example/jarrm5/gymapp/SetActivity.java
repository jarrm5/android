package com.example.jarrm5.gymapp;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.jarrm5.gymapp.MainActivity.myDb;

public class SetActivity extends AppCompatActivity {

    private int theKey;                  //Stores the key of exercises that was clicked to get to this activity
    private List sets;                   //holds set objects used for populating listview
    private ListView mListViewSets;      //container to hold the sets
    private TextView mEmptySet;          //default message when there are no exercises to display
    private String mThisExercise;       //Store the name of the exercises associated with these sets

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        mListViewSets = (ListView)findViewById(R.id.listViewSets);
        mEmptySet = (TextView)findViewById(R.id.noSet);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            //Retrieve the exercise key from the workout clicked from the previous activity
            theKey = extras.getInt("key");
        }

        //Exercise key recovered, now get the Exercise name associated with the key
        mThisExercise = getExerciseName(theKey).getExerName();
        this.setTitle(mThisExercise + " Sets");

        //Display the listview with exercises
        if(!populateSets(theKey)){
            //Nothing to display, show empty message
            mEmptySet.setText("Click + to add some " + mThisExercise + " sets!");
            mListViewSets.setEmptyView(findViewById(R.id.noSet));
        }

        //long res1 = myDb.createSet(new Set(205, 10, "2017-04-09",3));
        //long res2 = myDb.createSet(new Set(100, 9, getDateTime(),5));
        //long res3 = myDb.createSet(new Set(235, 6, "2017-04-09",3));


    }

    //Get the exercise name associated with the key passed to this activity
    //Used for displaying the exercise name when there are no sets associated with it
    private Exercise getExerciseName(int key){
        Cursor cursor = myDb.getExerciseByKey(key);
        cursor.moveToFirst();
        Exercise e = new Exercise(cursor.getInt(cursor.getColumnIndex(myDb.getPkeyExerciseId())),cursor.getString(cursor.getColumnIndex(myDb.getExerciseName())));
        cursor.close();
        return e;
    }
    private boolean populateSets(int key){
        //Retrieve data from the helper
        Cursor cursor = myDb.getSets(key);

        //No data is returned; return false
        if(cursor.getCount() == 0){
            return false;
        }

        sets = new ArrayList();
        while(cursor.moveToNext()) {
            //Create set objects from each record in the database specified by key, save to the array
            sets.add(new Set(cursor.getInt(cursor.getColumnIndex(myDb.getPkeySetId())),
                             cursor.getInt(cursor.getColumnIndex(myDb.getSetWeight())),
                             cursor.getInt(cursor.getColumnIndex(myDb.getSetReps())),
                             cursor.getString(cursor.getColumnIndex(myDb.getSetDate())),
                             cursor.getInt(cursor.getColumnIndex(myDb.getFkeyExerciseId()))));
        }
        cursor.close();
        //Load the data array to the listview.
        //Set objects are passed to each LV item; we can see the names from the set's toString method
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,sets);
        mListViewSets.setAdapter(adapter);

        //Successfully obtained data; return true
        return true;
    }

    //Fetch the current date in 'yyyy-MM-dd' format'
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
