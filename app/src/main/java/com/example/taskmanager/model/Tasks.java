package com.example.taskmanager.model;

import java.io.Serializable;

public class Tasks implements Serializable {

    int ID;
    String title;
    String description;
    String flag;
    int pushpin;
    String datecreated;
    String dateupdated;

    public Tasks(int ID, String title, String description, String flag, int pushpin, String datecreated, String dateupdated) {
        this.ID = ID;
        this.title = title;
        this.description = description;
        this.flag = flag;
        this.pushpin = pushpin;
        this.datecreated = datecreated;
        this.dateupdated = dateupdated;
    }

    public Tasks(String title, String description, String flag, int pushpin, String datecreated, String dateupdated) {
        this.title = title;
        this.description = description;
        this.flag = flag;
        this.pushpin = pushpin;
        this.datecreated = datecreated;
        this.dateupdated = dateupdated;
    }

    public Tasks(String title, String description, String flag, String datecreated, String dateupdated) {
        this.title = title;
        this.description = description;
        this.flag = flag;
        this.datecreated = datecreated;
        this.dateupdated = dateupdated;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getPushpin() {
        return pushpin;
    }

    public void setPushpin(int pushpin) {
        this.pushpin = pushpin;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    public String getDateupdated() {
        return dateupdated;
    }

    public void setDateupdated(String dateupdated) {
        this.dateupdated = dateupdated;
    }
}
