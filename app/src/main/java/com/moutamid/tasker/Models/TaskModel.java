package com.moutamid.tasker.Models;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskModel {

    String name, category, description, condition;
    ArrayList<HashMap<String, String>> subTasks;

    public TaskModel(String name, String category, String description, String condition, ArrayList<HashMap<String, String>> subTasks) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.condition = condition;
        this.subTasks = subTasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public ArrayList<HashMap<String, String>> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<HashMap<String, String>> subTasks) {
        this.subTasks = subTasks;
    }

    public TaskModel() {
    }
}
