package com.exxeta.bibleschedule.model;

import java.util.Date;

import io.realm.RealmObject;


public class Schedule extends RealmObject {

    private Date date;
    private String coordinates;
    private boolean wasRead;
    private Date whenWasRead;

    public Schedule() {

    }

    public Schedule(Date date, String coordinates, Boolean wasRead) {
        this.date = date;
        this.coordinates = coordinates;
        this.wasRead = wasRead;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public Boolean getWasRead() {
        return wasRead;
    }

    public void setWasRead(Boolean wasRead) {
        this.wasRead = wasRead;
    }

    public Date getWhenWasRead() {
        return whenWasRead;
    }

    public void setWhenWasRead(Date whenWasRead) {
        this.whenWasRead = whenWasRead;
    }
}
