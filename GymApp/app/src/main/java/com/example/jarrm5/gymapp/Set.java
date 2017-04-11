package com.example.jarrm5.gymapp;

import java.sql.Date;

/**
 * Created by James on 3/17/2017.
 */
public class Set {

    private int setId;
    private int weight;
    private int reps;
    private String progDate; //Idk if 'Date' is the right datatype, sql may not take it
    private int exerId;

    public Set(){}

    public Set(int weight, int reps, String progDate, int exerId) {
        this.weight = weight;
        this.reps = reps;
        this.progDate = progDate;
        this.exerId = exerId;
    }

    public Set(int setId, int weight, int reps, String progDate, int exerId) {
        this.setId = setId;
        this.weight = weight;
        this.reps = reps;
        this.progDate = progDate;
        this.exerId = exerId;
    }

    public int getSetId() {
        return setId;
    }

    public void setSetId(int setId) {
        this.setId = setId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public String getProgDate() {
        return progDate;
    }

    public void setProgDate(String progDate) {
        this.progDate = progDate;
    }

    public int getExerId() {
        return exerId;
    }

    public void setExerId(int exerId) {
        this.exerId = exerId;
    }

    @Override
    public String toString() {
        return getWeight() + " lbs\r\n" + getReps() + " reps\r\n" + getProgDate();
    }
}
