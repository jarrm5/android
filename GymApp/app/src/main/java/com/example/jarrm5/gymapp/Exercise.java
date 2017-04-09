package com.example.jarrm5.gymapp;

/**
 * Created by James on 3/17/2017.
 */
public class Exercise {

    private int exerId;
    private String exerName;
    private int wktId;

    public Exercise() {}

    public Exercise(String exerName, int wktId) {
        this.exerName = exerName;
        this.wktId = wktId;
    }

    public Exercise(int exerId, String exerName) {
        this.exerId = exerId;
        this.exerName = exerName;
    }


    public Exercise(int exerId, String exerName, int wktId) {
        this.exerId = exerId;
        this.exerName = exerName;
        this.wktId = wktId;
    }

    public int getExerId() {
        return exerId;
    }

    public void setExerId(int exerId) {
        this.exerId = exerId;
    }

    public String getExerName() {
        return exerName;
    }

    public void setExerName(String exerName) {
        this.exerName = exerName;
    }

    public int getWktId() {
        return wktId;
    }

    public void setWktId(int wktId) {
        this.wktId = wktId;
    }

    @Override
    public String toString() {
        return getExerName();
    }
}
