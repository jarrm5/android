package com.example.jarrm5.gymapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * Created by James on 4/10/2017.
 */

public class SetDialog extends DialogFragment {

    public interface MyDialogFragmentListener {     //Interface to help send values back to set activity
        void onReturnValue(int weight, int rep);
    }

    private LayoutInflater inflater; //store a ref to the activity's inflater
    private View v;                  //store the view of the dialog
    private TextView mDialogTitle;

    private NumberPicker mWeightPicker; //number spinner for weight selection
    private NumberPicker mRepPicker;    //number spinner for reps

    private int displayWeight;          //Initialize weight number on the number picker
    private int displayReps;            //Initialize weight number on the number picker
    private String mode;                //String displayed in the title

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.dialog_set, null);
        mDialogTitle = (TextView)v.findViewById(R.id.textView_setDialogTitle);

        mDialogTitle.setText(mode + " Set");
        setPickers(displayWeight,displayReps);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(v).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //implement the interface for sending picker values back to the set activity
                MyDialogFragmentListener activity = (MyDialogFragmentListener) getActivity();
                //send values back to set activity
                activity.onReturnValue(mWeightPicker.getValue(),mRepPicker.getValue());
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();
    }
    //Do some preliminaries for the number pickers
    private void setPickers(int weight, int reps){
        mWeightPicker = (NumberPicker)v.findViewById(R.id.numberPicker_Weight);
        mWeightPicker.setMaxValue(500);
        mWeightPicker.setValue(weight);
        mWeightPicker.setMinValue(0);
        mWeightPicker.setWrapSelectorWheel(false);

        mRepPicker = (NumberPicker)v.findViewById(R.id.numberPicker_Reps);
        mRepPicker.setMaxValue(100);
        mRepPicker.setValue(reps);
        mRepPicker.setMinValue(0);
        mRepPicker.setWrapSelectorWheel(false);
    }
    public void setDefaultWeight(int displayWeight) {
        this.displayWeight = displayWeight;
    }
    public void setDefaultReps(int displayReps) {
        this.displayReps = displayReps;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }
}
