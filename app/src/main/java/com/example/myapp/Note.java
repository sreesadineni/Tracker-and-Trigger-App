package com.example.myapp;

import java.util.HashMap;
import java.util.Map;

public class Note {
    private String title;
    private String desc;
    private int priority;
    Map<String, Object> note;

    public Note() {
        //empty constructor needed
    }

    public Note(String title, String desc, int priority) {
        this.title = title;
        this.desc = desc;
        this.priority = priority;
        this.note = new HashMap<>();
        this.note.put("title", title);
        this.note.put("desc", desc);
        this.note.put("priority", priority);
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public int getPriority() { return priority; }

    public Map<String, Object> getNote() {
        return note;
    }
}
