package com.example.myapp;

import java.util.HashMap;
import java.util.Map;

public class List {
    private String item;
    boolean status;
    Map<String, Object> list;

    public List() {
        // empty constructor needed
    }

    public List(String item, boolean status) {
        this.item = item;
        this.status = status;
        this.list = new HashMap<>();
        this.list.put("item", item);
        this.list.put("status", status);
    }

    public String getItem() {
        return item;
    }

    public boolean getStatus() {
        return status;
    }

    public Map<String, Object> getList() {
        return list;
    }
}
