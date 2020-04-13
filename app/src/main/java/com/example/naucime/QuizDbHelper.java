package com.example.naucime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.naucime.QuizContract.*;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "naucime.db";
    private static final int DATABASE_VERSION = 7;

    private SQLiteDatabase db;
    private Context context;



    public QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_CLASS_TABLE = "CREATE TABLE " +
                ClassTable.TABLE_NAME + " ( " +
                ClassTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ClassTable.COLUMN_NAME + " TEXT" + ")";
        final String SQL_CREATE_LESSON_TABLE = "CREATE TABLE " +
                LessonTable.TABLE_NAME + " ( " +
                LessonTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LessonTable.COLUMN_NAME + " TEXT, " +
                LessonTable.COLUMN_TEXT + " TEXT, " +
                LessonTable.COLUMN_READ + " INTEGER, " +
                LessonTable.COLUMN_CLASS_ID + " INTEGER" + ")";
        final String SQL_CREATE_QUIZ_TABLE = "CREATE TABLE " +
                QuizTable.TABLE_NAME + " ( " +
                QuizTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuizTable.COLUMN_QUESTION + " TEXT, " +
                QuizTable.COLUMN_OPTION1 + " TEXT, " +
                QuizTable.COLUMN_OPTION2 + " TEXT, " +
                QuizTable.COLUMN_OPTION3 + " TEXT, " +
                QuizTable.COLUMN_ANSWER + " INTEGER, " +
                QuizTable.COLUMN_RELATED_LESSON + " INTEGER" + ")";

        db.execSQL(SQL_CREATE_CLASS_TABLE);
        db.execSQL(SQL_CREATE_LESSON_TABLE);
        db.execSQL(SQL_CREATE_QUIZ_TABLE);
        readFile("DbData.txt");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ClassTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LessonTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuizTable.TABLE_NAME);
        onCreate(db);
    }



    public void addClass(String name){
        ContentValues cv = new ContentValues();
        cv.put(ClassTable.COLUMN_NAME, name);
        db.insert(name, null, cv);
    }

    public void addLesson(Lesson lession){
        ContentValues cv = new ContentValues();
        cv.put(LessonTable.COLUMN_NAME, lession.getName());
        cv.put(LessonTable.COLUMN_TEXT, lession.getText());
        cv.put(LessonTable.COLUMN_READ, lession.getRead());
        cv.put(LessonTable.COLUMN_CLASS_ID, lession.getClass_ID());
        db.insert(LessonTable.TABLE_NAME, null, cv);
    }

    public void addQuestion(Question question){
        ContentValues cv = new ContentValues();
        cv.put(QuizTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuizTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuizTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuizTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuizTable.COLUMN_ANSWER, question.getAnswer());
        cv.put(QuizTable.COLUMN_RELATED_LESSON, question.getRelatedLesson());
        db.insert(QuizTable.TABLE_NAME, null, cv);
    }

    public List<Question> getAllQuestions(int class_id){
        List<Question> questions = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from quiz where related_lesson in (select _id from Lesson where class_id =" + class_id + ")", null);

        if(c.moveToFirst()){
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex("_id")));
                question.setQuestion(c.getString(c.getColumnIndex("question")));
                question.setOption1(c.getString(c.getColumnIndex("option1")));
                question.setOption2(c.getString(c.getColumnIndex("option2")));
                question.setOption3(c.getString(c.getColumnIndex("option3")));
                question.setAnswer(c.getInt(c.getColumnIndex("answer")));
                question.setRelatedLesson(c.getInt(c.getColumnIndex("related_lesson")));
                questions.add(question);
            }while (c.moveToNext());
        }
        c.close();
        return questions;
    }

    public List<Question> getMiniQuizQuestions(int relatedLesson){
        List<Question> questions = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from quiz where related_lesson=" + relatedLesson, null);
        if(c.moveToFirst()){
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex("_id")));
                question.setQuestion(c.getString(c.getColumnIndex("question")));
                question.setOption1(c.getString(c.getColumnIndex("option1")));
                question.setOption2(c.getString(c.getColumnIndex("option2")));
                question.setOption3(c.getString(c.getColumnIndex("option3")));
                question.setAnswer(c.getInt(c.getColumnIndex("answer")));
                question.setRelatedLesson(c.getInt(c.getColumnIndex("related_lesson")));
                questions.add(question);
            }while (c.moveToNext());
        }
        c.close();
        return questions;
    }

    public List<Lesson> getAllLesson(int class_ID){
        List<Lesson> lessons = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM lesson where class_id=" + class_ID, null);

        if(c.moveToFirst()){
            do {
                Lesson lesson = new Lesson();
                lesson.setId(c.getInt(c.getColumnIndex(LessonTable._ID)));
                lesson.setName(c.getString(c.getColumnIndex(LessonTable.COLUMN_NAME)));
                lesson.setText(c.getString(c.getColumnIndex(LessonTable.COLUMN_TEXT)));
                lesson.setRead(c.getInt(c.getColumnIndex(LessonTable.COLUMN_READ)));
                lesson.setClass_ID(c.getInt(c.getColumnIndex(LessonTable.COLUMN_CLASS_ID)));
                lessons.add(lesson);
            }while (c.moveToNext());
        }
        c.close();
        return lessons;
    }

    public int updateSeeker(int class_ID){
        double allLessons = 0;
        double readLessons = 0;
        db = getReadableDatabase();
        Cursor c = db.rawQuery("select count(*) as \'all\', sum(read) as \'read\' from Lesson where class_id=" + class_ID, null);

        if(c.moveToFirst()){
            do{
                allLessons = c.getInt(c.getColumnIndex("all"));
                readLessons = c.getInt(c.getColumnIndex("read"));
                break;
            }while (c.moveToNext());
        }
        return (int)Math.round((readLessons/allLessons)*100);
    }

    public Lesson getLesson(int id){
        Lesson lesson = new Lesson();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from lesson where _ID=" + id, null);

        if (c.moveToFirst()){
            do{
                lesson.setId(c.getInt(c.getColumnIndex(LessonTable._ID)));
                lesson.setName(c.getString(c.getColumnIndex(LessonTable.COLUMN_NAME)));
                lesson.setText(c.getString(c.getColumnIndex(LessonTable.COLUMN_TEXT)));
                lesson.setRead(c.getInt(c.getColumnIndex(LessonTable.COLUMN_READ)));
                lesson.setClass_ID(c.getInt(c.getColumnIndex(LessonTable.COLUMN_CLASS_ID)));
                break;
            }while (c.moveToNext());
        }
        c.close();
        return lesson;
    }

    public void setAsRead(int lessonId){
        String sql = "update lesson set read = 1 where _ID = " + lessonId;
        db.execSQL(sql);
    }

    public void setAsUnead(int lessonId){
        String sql = "update lesson set read = 0 where _ID = " + lessonId;
        db.execSQL(sql);
    }

//    public void fillDb(){
//        String dbData = readFile("DbData");
//        while (dbData != "end;"){
//            int nameIndex = dbData.indexOf("name:");
//
//        }
//
//        addClass("Fizika");
//        addClass("Hemija");
//        addClass("Istorija");
//        addClass("Geografija");
//        Lesson lesson = new Lesson();
//        lesson.setName("Prvi Njutnov zakon");
//        lesson.setText("Svako telo koje miruje teži da ostane u stanju mirovanja i svako telo koje se kreće teži da nastavi da se kreće istom brzinom i u istom smeru ukoliko na njega ne deluje neka sila koja ga prinudi da promeni stanje mirovanja tj. jednolikog pravolinijskog kretanja.");
//        lesson.setRead(0);
//        lesson.setClass_ID(1);
//        addLesson(lesson);
//        //addLesson(new Lesson());
//    }

    public String readFile(String filename){
        String data = "";
        BufferedReader br = null;
        try{
            br = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));
            String line = "";
            while ((line = br.readLine()) != null) {
                db.execSQL(line);
            }
        }catch (Exception ex) {
            Toast toast = Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
        return data;
    }




    public String unreadLesson(int relatedLesson) {
        setAsUnead(relatedLesson);

        db = getReadableDatabase();
        Cursor c = db.rawQuery("select name from lesson where _ID=" + relatedLesson, null);
        String lesson = "";
        if (c.moveToFirst()){
            do{
                lesson = c.getString(c.getColumnIndex(LessonTable.COLUMN_NAME));
            }while (c.moveToNext());
        }
        c.close();
        return lesson;
    }
}
