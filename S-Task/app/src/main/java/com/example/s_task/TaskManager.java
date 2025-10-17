package com.example.s_task;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private static final String PREF_NAME = "TaskPreferences";
    private static final String TASK_LIST_KEY = "task_list";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public TaskManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveTasks(List<Task> tasks) {
        String json = gson.toJson(tasks);
        sharedPreferences.edit().putString(TASK_LIST_KEY, json).apply();
    }

    public List<Task> loadTasks() {
        String json = sharedPreferences.getString(TASK_LIST_KEY, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
