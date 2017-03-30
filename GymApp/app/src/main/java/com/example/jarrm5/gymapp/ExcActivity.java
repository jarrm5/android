package com.example.jarrm5.gymapp;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import static com.example.jarrm5.gymapp.MainActivity.myDb;

public class ExcActivity extends AppCompatActivity {

    private int theKey;    //Stores the key of workout that was clicked to get to this activity
    private List exercises;
    private ListView mListViewExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exc);

        mListViewExercises = (ListView)findViewById(R.id.listViewExercises);
        //Recover the workout key from the previous activity
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            //Retrieve the workout key from the workout clicked from the previous activity
            theKey = extras.getInt("key");
        }
        //Display the listview with exercises
        if(!populateExercises(theKey)){
            //Nothing to display, show empty message
            mListViewExercises.setEmptyView(findViewById(R.id.noExc));
        }
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

        builder.setTitle("Enter the exercise name").setView(input).setView(input);

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
