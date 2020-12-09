package com.uc3m.beltransen.checklist;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CheckList implements Parcelable {
    private String name;
    private ArrayList<Task> tasks;
    private boolean isOpen;

    public CheckList() {
        this.name = "";
        this.tasks = new ArrayList<Task>();
        isOpen = true;
    }

    public CheckList(String name){
        this.name = name;
        this.tasks = new ArrayList<Task>();
        isOpen = true;
    }

    protected CheckList(Parcel in) {
        name = in.readString();
        tasks = in.createTypedArrayList(Task.CREATOR);
        isOpen = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(tasks);
        dest.writeByte((byte) (isOpen ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CheckList> CREATOR = new Creator<CheckList>() {
        @Override
        public CheckList createFromParcel(Parcel in) {
            return new CheckList(in);
        }

        @Override
        public CheckList[] newArray(int size) {
            return new CheckList[size];
        }
    };

    public void addTask(Task e){
        this.tasks.add(e);
    }

    public void removeTask(int index){
        this.tasks.remove(index);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Task get(int i){
        return tasks.get(i);
    }

    public void clear(){
        tasks.clear();
    }
    public int size(){
        return tasks.size();
    }
}
