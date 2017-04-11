package com.example.jarrm5.gymapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.jarrm5.gymapp.MainActivity.myDb;

//Implement MyDialogFragmentListener to get values back from the custom dialog box
public class SetActivity extends AppCompatActivity implements SetDialog.MyDialogFragmentListener {

    private int theKey;                  //Stores the key of exercises that was clicked to get to this activity
    private List sets;                   //holds set objects used for populating listview
    private ListView mListViewSets;      //container to hold the sets
    private TextView mEmptySet;          //default message when there are no exercises to display
    private SetDialog mSetDialog;        //My custom dialog box for creating/updating sets
    private String mThisExercise;       //Store the name of the exercises associated with these sets

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        mListViewSets = (ListView)findViewById(R.id.listViewSets);
        mEmptySet = (TextView)findViewById(R.id.noSet);
        mSetDialog = new SetDialog();

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

        mListViewSets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get information about the set that was clicked on
                Set s = (Set) sets.get(position);
                //Persist the weight and reps in the number pickers for updating sets
                mSetDialog.setDefaultWeight(s.getWeight());
                mSetDialog.setDefaultReps(s.getReps());
                mSetDialog.setMode("Edit");
                //show the dialog for updating
                mSetDialog.show(getFragmentManager(),"setdialog");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    //Clicking add exercise button in the action bar
    public boolean onOptionsItemSelected(MenuItem item) {
        //Set the number pickers to 0 when creating new sets
        mSetDialog.setDefaultWeight(0);
        mSetDialog.setDefaultReps(0);
        mSetDialog.setMode("Create");
        mSetDialog.show(getFragmentManager(),"setdialog");
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onReturnValue(int weight, int reps){
        Set set = new Set(weight,reps,getDateTime(),theKey);
        long set_key = myDb.createSet(set);
        populateSets(theKey);
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
