package com.uc3m.beltransen.checklist;

import java.util.Calendar;
import java.util.Date;

public class Task {
    String name;
    Date date;
    boolean isDone;

    public Task(String name){
        this.name = name;
        this.isDone = false;
        this.date = Calendar.getInstance().getTime();
    }

    public Task(String name, boolean isDone){
        this.name = name;
        this.isDone = isDone;
        this.date = Calendar.getInstance().getTime();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
