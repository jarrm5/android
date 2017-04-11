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


public class MainActivity extends AppCompatActivity {

    private ListView mListViewWorkouts;
    private List workouts;
    //Make public so every activity can see the db helper
    //Make static so the object is created only one time
    public static DatabaseHelper myDb;
    private TextView mEmptyWkt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
        mListViewWorkouts = (ListView)findViewById(R.id.listViewWorkouts);
        mEmptyWkt = (TextView)findViewById(R.id.noWkt);

        //Display the listview with workouts
        if(!populateWorkouts()){
            //Nothing to display, show empty message
            mEmptyWkt.setText("Click + to add some workouts!");
            mListViewWorkouts.setEmptyView(findViewById(R.id.noWkt));
        }

        //Event for clicking a workout in the listview
        mListViewWorkouts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Workout w = (Workout)workouts.get(position);
                Intent intent = new Intent(MainActivity.this,ExcActivity.class);
                intent.putExtra("key",w.getWktId());      //Pass the key of the workout that was clicked to the next activity
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
    //Clicking add workout button in the action bar
    public boolean onOptionsItemSelected(MenuItem item) {

        //Creating the dialog box for entering the workout name
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);

        builder.setTitle("Enter the workout name").setView(input).setView(input);

        builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Workout workout = new Workout(input.getText().toString());
                long workout_key = myDb.createWorkout(workout);
                populateWorkouts();
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
    //Show the user all of the workouts
    private boolean populateWorkouts(){
        Cursor cursor = myDb.getWorkouts();

        if(cursor.getCount() == 0){
            return false;
        }

        workouts = new ArrayList();

        while(cursor.moveToNext()) {
            //Create workout objects from each record in the database, save to the array
            workouts.add(new Workout(cursor.getInt(cursor.getColumnIndex(myDb.getPkeyWorkoutId())),cursor.getString(cursor.getColumnIndex(myDb.getWorkoutName()))));
        }
        cursor.close();
        //Load the data array to the listview.
        //Workout objects are passed to each LV item; we can see the names from the Workout's toString method
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,workouts);
        mListViewWorkouts.setAdapter(adapter);

        return true;
    }
}
