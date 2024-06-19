package com.seohyun.kimseohyun2091052;

public class Diary {
    private String id;
    private String day;
    private String content;

    public Diary() {
        // Empty constructor needed for Firestore
    }

    public Diary(String id, String day, String content) {
        this.id = id;
        this.day = day;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
