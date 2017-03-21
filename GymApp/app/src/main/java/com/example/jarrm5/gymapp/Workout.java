package com.example.jarrm5.gymapp;

/**
 * Created by James on 3/17/2017.
 */
public class Workout {

    private int wktId;
    private String wktName;

    public Workout() {}

    public Workout(String wktName) {
        this.wktName = wktName;
    }

    public Workout(int wktId, String wktName) {
        this.wktId = wktId;
        this.wktName = wktName;
    }

    public int getWktId() {
        return wktId;
    }

    public void setWktId(int wktId) {
        this.wktId = wktId;
    }

    public String getWktName() {

        return wktName;
    }

    public void setWktName(String wktName) {
        this.wktName = wktName;
    }
}
