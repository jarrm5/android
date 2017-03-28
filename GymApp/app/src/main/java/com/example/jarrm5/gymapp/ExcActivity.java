package com.example.jarrm5.gymapp;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import static com.example.jarrm5.gymapp.MainActivity.myDb;

public class ExcActivity extends AppCompatActivity {

    //DatabaseHelper myDb;
    private int the_key;
    private List exercises;
    private ListView mListViewExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exc);

        mListViewExercises = (ListView)findViewById(R.id.listViewExercises);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            //Retrieve the workout key from the workout clicked from the previous activity
            the_key = extras.getInt("key");
        }
        getExercises(the_key);
    }

    private void getExercises(int key){
        Cursor cursor = myDb.getExercises(key);
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
    }
}
