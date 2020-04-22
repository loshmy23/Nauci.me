package com.example.naucime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.naucime.QuizContract.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.core.view.accessibility.AccessibilityViewCommand;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "naucime.db";
    private static final int DATABASE_VERSION = 1;

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
                ClassTable.COLUMN_NAME + " TEXT, " +
                ClassTable.COLUMN_COLOR1 + " TEXT, " +
                ClassTable.COLUMN_COLOR2 + " TEXT, " +
                ClassTable.COLUMN_COLOR3 + " TEXT, " +
                ClassTable.COLUMN_COLOR4 + " TEXT)";
        final String SQL_CREATE_LESSON_TABLE = "CREATE TABLE " +
                LessonTable.TABLE_NAME + " ( " +
                LessonTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LessonTable.COLUMN_NAME + " TEXT, " +
                LessonTable.COLUMN_LESSON_CODE + " TEXT UNIQUE, " +
                LessonTable.COLUMN_FILENAME + " TEXT, " +
                LessonTable.COLUMN_READ + " INTEGER, " +
                LessonTable.COLUMN_CLASS_ID + " INTEGER)";

        final String SQL_CREATE_QUESTION_TABLE = "CREATE TABLE " +
                QuestionTable.TABLE_NAME + " ( " +
                QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionTable.COLUMN_QUESTION + " TEXT, " +
                QuestionTable.COLUMN_QUESTION_TYPE + " TEXT, " +
                QuestionTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionTable.COLUMN_ANSWER + " INTEGER, " +
                QuestionTable.COLUMN_RELATED_LESSON_CODE + " TEXT)";

        final String SQL_CREATE_SETTINGS_TABLE = "CREATE TABLE " +
                SettingsTable.TABLE_NAME + " ( " +
                SettingsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SettingsTable.COLUMN_PHOTO + " TEXT, " +
                SettingsTable.COLUMN_PHOTO_PAGE + " TEXT, " +
                SettingsTable.COLUMN_POHOTO_DIMENSIONS + " TEXT, " +
                SettingsTable.COLUMN_STARTING_PHOTO + " TEXT, " +
                SettingsTable.COLUMN_LESSON_CODE + " TEXT)";


        db.execSQL(SQL_CREATE_CLASS_TABLE);
        db.execSQL(SQL_CREATE_LESSON_TABLE);
        db.execSQL(SQL_CREATE_QUESTION_TABLE);
        db.execSQL(SQL_CREATE_SETTINGS_TABLE);
        readFile("DbData.txt");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ClassTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LessonTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SettingsTable.TABLE_NAME);
        onCreate(db);
    }

    public List<Question> getAllQuestions(int class_id){
        List<Question> questions = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + QuestionTable.TABLE_NAME +
                " where " + QuestionTable.COLUMN_RELATED_LESSON_CODE +
                " in (select " + LessonTable.COLUMN_LESSON_CODE + " from " + LessonTable.TABLE_NAME +
                " where " + LessonTable.COLUMN_CLASS_ID + " = " + class_id + ")", null);

        if(c.moveToFirst()){
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setQuestionType(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION_TYPE)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setAnswer(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER)));
                question.setRelatedLessonCode(c.getString(c.getColumnIndex(QuestionTable.COLUMN_RELATED_LESSON_CODE)));
                questions.add(question);
            }while (c.moveToNext());
        }
        c.close();
        return questions;
    }


    public List<Question> getMiniQuizQuestions(String relatedLessonCode){
        List<Question> questions = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + QuestionTable.TABLE_NAME +
                " where " + QuestionTable.COLUMN_RELATED_LESSON_CODE + "=\"" + relatedLessonCode + "\"", null);
        if(c.moveToFirst()){
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setQuestionType(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION_TYPE)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setAnswer(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER)));
                question.setRelatedLessonCode(c.getString(c.getColumnIndex(QuestionTable.COLUMN_RELATED_LESSON_CODE)));
                questions.add(question);
            }while (c.moveToNext());
        }
        c.close();
        return questions;
    }

    public List<LessonModel> getAllLesson(int class_ID){
        List<LessonModel> lessons = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + LessonTable.TABLE_NAME +
                " where " + LessonTable.COLUMN_CLASS_ID + "=" + class_ID, null);

        if(c.moveToFirst()){
            do {
                LessonModel lesson = new LessonModel();
                lesson.setId(c.getInt(c.getColumnIndex(LessonTable._ID)));
                lesson.setName(c.getString(c.getColumnIndex(LessonTable.COLUMN_NAME)));
                lesson.setLeesonCode(c.getString(c.getColumnIndex(LessonTable.COLUMN_LESSON_CODE)));
                lesson.setFilename(c.getString(c.getColumnIndex(LessonTable.COLUMN_FILENAME)));
                lesson.setRead(c.getInt(c.getColumnIndex(LessonTable.COLUMN_READ)));
                lesson.setClassId(c.getInt(c.getColumnIndex(LessonTable.COLUMN_CLASS_ID)));
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
        Cursor c = db.rawQuery("select count(*) as \'all\', " +
                "sum(" + LessonTable.COLUMN_READ + ") as \'read\' " +
                "from " + LessonTable.TABLE_NAME +
                " where " + LessonTable.COLUMN_CLASS_ID + "=" + class_ID, null);

        if(c.moveToFirst()){
            do{
                allLessons = c.getInt(c.getColumnIndex("all"));
                readLessons = c.getInt(c.getColumnIndex("read"));
                break;
            }while (c.moveToNext());
        }
        c.close();
        return (int)Math.round((readLessons/allLessons)*100);
    }

    public LessonModel getLesson(int id){
        LessonModel lesson = new LessonModel();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + LessonTable.TABLE_NAME +
                " where _ID=" + id, null);

        if (c.moveToFirst()){
            do{
                lesson.setId(c.getInt(c.getColumnIndex(LessonTable._ID)));
                lesson.setName(c.getString(c.getColumnIndex(LessonTable.COLUMN_NAME)));
                lesson.setLeesonCode(c.getString(c.getColumnIndex(LessonTable.COLUMN_LESSON_CODE)));
                lesson.setFilename(c.getString(c.getColumnIndex(LessonTable.COLUMN_FILENAME)));
                lesson.setRead(c.getInt(c.getColumnIndex(LessonTable.COLUMN_READ)));
                lesson.setClassId(c.getInt(c.getColumnIndex(LessonTable.COLUMN_CLASS_ID)));
                break;
            }while (c.moveToNext());
        }
        c.close();
        return lesson;
    }

    public void setAsRead(int lessonId){
        String sql = "update " + LessonTable.TABLE_NAME + " set " + LessonTable.COLUMN_READ + " = 1 where " + LessonTable._ID + " = " + lessonId;
        db.execSQL(sql);
    }

    public void setAsUnead(int lessonId){
        String sql = "update " + LessonTable.TABLE_NAME + " set " + LessonTable.COLUMN_READ + " = 0 where " + LessonTable._ID + " = " + lessonId;
        db.execSQL(sql);
    }

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

    public String unreadLesson(String relatedLesson) {
        int unread = 0;
        db = getReadableDatabase();
        String sql = "select " + LessonTable.COLUMN_NAME +
                ", " + LessonTable._ID + " from " + LessonTable.TABLE_NAME +
                " where " + LessonTable.COLUMN_LESSON_CODE + "=\"" + relatedLesson + "\"";
        Cursor c = db.rawQuery(sql, null);
        String lesson = "";
        if (c.moveToFirst()){
            do{
                lesson = c.getString(c.getColumnIndex(LessonTable.COLUMN_NAME));
                unread = c.getInt(c.getColumnIndex(LessonTable._ID));
            }while (c.moveToNext());
        }
        c.close();
        setAsUnead(unread);
        return lesson;
    }

    public List<PhotoSettings> getSettings(String lessonCode){
        List<PhotoSettings> settings = new ArrayList<>();

        db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + SettingsTable.TABLE_NAME +
                " where " + SettingsTable.COLUMN_LESSON_CODE + "=\"" + lessonCode + "\"" , null);

        if(c.moveToFirst()){
            do {
                PhotoSettings photoSettings = new PhotoSettings();
                photoSettings.setLessonCode(lessonCode);
                photoSettings.setPhoto(c.getString(c.getColumnIndex(SettingsTable.COLUMN_PHOTO)));
                photoSettings.setPhotoPage(c.getInt(c.getColumnIndex(SettingsTable.COLUMN_PHOTO_PAGE)));
                photoSettings.setStartingPhoto(c.getString(c.getColumnIndex(SettingsTable.COLUMN_STARTING_PHOTO)));
                photoSettings.setXY(c.getString(c.getColumnIndex(SettingsTable.COLUMN_POHOTO_DIMENSIONS)));
                settings.add(photoSettings);
            }while (c.moveToNext());
        }
        c.close();
        return settings;
    }
}
