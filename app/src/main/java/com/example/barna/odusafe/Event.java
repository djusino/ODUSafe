package com.example.barna.odusafe;

import java.util.Random;

/**
 * Created by Michael Blackwell on 9/5/2017.
 */

public class Event {
    private int id;
    private String description;
    private String crimeType;
    private String reporter;
    private String location;
    private String severity;
    private String date;

    public Event(){

        this.setid(0);
        this.setDescription(null);
        this.setCrimeType(null);
        this.setReporter(null);
        this.setLocation(null);
        this.setSeverity(null);
        this.setDate(null);
    }


    public Event(String description, String crimeType, String reporter, String location, String severity , String date){
        Random r = new Random();
        int x = r.nextInt(10000000);

        this.setid(x);
        this.setDescription(description);
        this.setCrimeType(crimeType);
        this.setReporter(reporter);
        this.setLocation(location);
        this.setSeverity(severity);
        this.setDate(date);


    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }


    public String getCrimeType() {
        return crimeType;
    }

    public void setCrimeType(String crimeType) {
        this.crimeType = crimeType;
    }
}
