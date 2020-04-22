package com.example.naucime;

public class LessonModel {

    private int id;
    private String name;
    private String lessonCode;
    private String filename;
    private int read;
    private int classId;

    public LessonModel(){

    }

    public LessonModel(int id, String name, int read) {
        this.id = id;
        this.name = name;
        this.read = read;
    }

    public LessonModel(String name, String lessonCode, String filename, int read, int classId) {
        this.name = name;
        this.lessonCode = lessonCode;
        this.filename = filename;
        this.read = read;
        this.classId = classId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLessonCode() {
        return lessonCode;
    }

    public void setLeesonCode(String leesonCode) {
        this.lessonCode = leesonCode;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }
}
