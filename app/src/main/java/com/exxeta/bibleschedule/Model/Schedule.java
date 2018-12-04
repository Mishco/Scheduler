package com.exxeta.bibleschedule.Model;

import org.joda.time.LocalDate;

/**
 * Bible Schedule
 */

public class Schedule {

    private String id;
    private LocalDate date;
    private String coordinate;
    private String wasRead;


    public Schedule(String id, LocalDate date, String coordinate, String wasRead) {
        this.id = id;
        this.date = date;
        this.coordinate = coordinate;
        this.wasRead = wasRead;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getWasRead() {
        return wasRead;
    }

    public void setWasRead(String wasRead) {
        this.wasRead = wasRead;
    }
}
