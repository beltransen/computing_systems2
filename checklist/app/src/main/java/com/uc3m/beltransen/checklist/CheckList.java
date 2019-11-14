package com.uc3m.beltransen.checklist;

import java.util.ArrayList;

public class CheckList {
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
