package com.uc3m.beltransen.checklist;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    String name;
    boolean isDone;

    public Task(String name){
        this.name = name;
        this.isDone = false;
    }

    public Task(String name, boolean isDone){
        this.name = name;
        this.isDone = isDone;
    }


    protected Task(Parcel in) {
        name = in.readString();
        isDone = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeByte((byte) (isDone ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

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
}
