package com.example.naucime;

public class LessonModel {

    int isRead;
    String lessonName;
    int id;

    public LessonModel(int isRead, String lessonName, int id) {
        this.isRead = isRead;
        this.lessonName = lessonName;
        this.id = id;
    }

    public int isRead() {
        return isRead;
    }

    public void setRead(int isRead) {
        this.isRead = isRead;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
