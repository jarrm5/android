package com.example.jarrm5.gymapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import static com.example.jarrm5.gymapp.MainActivity.myDb; //import the app's database

public class ExcActivity extends AppCompatActivity {

    private int theKey;                  //Stores the key of workout that was clicked to get to this activity
    private List exercises;              //holds Exercise objects used for populating listview
    private ListView mListViewExercises; //container to hold the exercises
    private TextView mEmptyExc;          //default message when there are no exercises to display
    private String mThisWorkout;         //save the name of the workout associated with these exercises

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exc);

        mListViewExercises = (ListView)findViewById(R.id.listViewExercises);
        mEmptyExc = (TextView)findViewById(R.id.noExc);

        //Recover the workout key from the previous activity
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            //Retrieve the workout key from the workout clicked from the previous activity
            theKey = extras.getInt("key");
        }

        //Workout key recovered, now get the workout name associated with the key
        mThisWorkout = getWorkoutName(theKey).getWktName();
        this.setTitle(mThisWorkout + " Exercises");

        //Display the listview with exercises
        if(!populateExercises(theKey)){
            //Nothing to display, show empty message
            mEmptyExc.setText("Click + to add some " + mThisWorkout + " exercises!");
            mListViewExercises.setEmptyView(findViewById(R.id.noExc));
        }

        //Event for clicking a workout in the listview
        mListViewExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Exercise e = (Exercise)exercises.get(position);
                Intent intent = new Intent(ExcActivity.this,SetActivity.class);
                intent.putExtra("key",e.getExerId());      //Pass the key of the exercise that was clicked to the next activity
                startActivity(intent);
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

        //Creating the dialog box for entering the exercise name
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);

        builder.setTitle("Enter an exercise for " + mThisWorkout + " workout").setView(input);
        builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Exercise exercise = new Exercise(input.getText().toString(),theKey);
                long exercise_key = myDb.createExercise(exercise);
                populateExercises(theKey);
            }
        });
        builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

        return super.onOptionsItemSelected(item);
    }

    //Get the workout name associated with the key passed to this activity
    //Used for displaying the workout name when there are no exercises associated with it
    private Workout getWorkoutName(int key){
        Cursor cursor = myDb.getWorkoutByKey(key);
        cursor.moveToFirst();
        Workout w = new Workout(cursor.getInt(cursor.getColumnIndex(myDb.getPkeyWorkoutId())),cursor.getString(cursor.getColumnIndex(myDb.getWorkoutName())));
        cursor.close();
        return w;
    }

    //Get all exercise names associated with the key passed to this activity
    private boolean populateExercises(int key){
        //Retrieve data from the helper
        Cursor cursor = myDb.getExercises(key);

        //No data is returned; return false
        if(cursor.getCount() == 0){
            return false;
        }

        exercises = new ArrayList();
        while(cursor.moveToNext()) {
            //Create exercise objects from each record in the database specified by key, save to the array
            exercises.add(new Exercise(cursor.getInt(cursor.getColumnIndex(myDb.getPkeyExerciseId())),cursor.getString(cursor.getColumnIndex(myDb.getExerciseName())),cursor.getInt(cursor.getColumnIndex(myDb.getFkeyWorkoutId())) ));
        }
        cursor.close();
        //Load the data array to the listview.
        //Exercise objects are passed to each LV item; we can see the names from the exercise's toString method
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,exercises);
        mListViewExercises.setAdapter(adapter);

        //Succesfully obtained data; return true
        return true;
    }
}
