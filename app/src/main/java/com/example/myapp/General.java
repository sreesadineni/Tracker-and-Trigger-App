package com.example.myapp;

public class General {
    private String title;
    private  String desc;
    private String extra;
    private String titleLower;

    public General() {
    }

    public General(String title, String desc, String extra) {
        this.title = title;
        this.desc = desc;
        this.extra = extra;
        this.titleLower=title.toLowerCase();
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getExtra() {
        return extra;
    }
}
