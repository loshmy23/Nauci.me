package com.example.naucime;

import android.provider.BaseColumns;

public final class QuizContract {

    public static class ClassTable implements BaseColumns {
        public static final String TABLE_NAME = "class";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_BACKGROUND = "background";
        public static final String COLUMN_COLOR1 = "color1";
        public static final String COLUMN_COLOR2 = "color2";
        public static final String COLUMN_COLOR3 = "color3";
        public static final String COLUMN_COLOR4 = "color4";
    }

    public static class LessonTable implements BaseColumns {
        public static final String TABLE_NAME = "lesson";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LESSON_CODE = "lesson_code";
        public static final String COLUMN_FILENAME = "filename";
        public static final String COLUMN_READ = "read";
        public static final String COLUMN_CLASS_ID = "class_id";
    }

    public static class QuestionTable implements  BaseColumns{
        public static final String TABLE_NAME = "question";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_QUESTION_TYPE = "question_type";
        public static final String COLUMN_OPTION1 = "option1";
        public static final String COLUMN_OPTION2 = "option2";
        public static final String COLUMN_OPTION3 = "option3";
        public static final String COLUMN_ANSWER = "answer";
        public static final String COLUMN_RELATED_LESSON_CODE = "related_lesson_code";
    }

    public static class SettingsTable implements BaseColumns{
        public static final String TABLE_NAME = "settings";
        public static final String COLUMN_STARTING_PHOTO = "starting_photo";
        public static final String COLUMN_PHOTO = "photo";
        public static final String COLUMN_PHOTO_PAGE = "photo_page";
        public static final String COLUMN_POHOTO_DIMENSIONS = "photo_dimensions";
        public static final String COLUMN_LESSON_CODE = "lesson_code";
    }

//    public static class ClassTable implements BaseColumns {
//        public static final String TABLE_NAME = "class";
//        public static final String COLUMN_NAME = "name";
//    }
//
//    public static class LessonTable implements BaseColumns {
//        public static final String TABLE_NAME = "lesson";
//        public static final String COLUMN_NAME = "name";
//        public static final String COLUMN_TEXT = "text";
//        public static final String COLUMN_READ = "read";
//        public static final String COLUMN_CLASS_ID = "class_id";
//    }
//
//    public static class QuizTable implements  BaseColumns{
//        public static final String TABLE_NAME = "quiz";
//        public static final String COLUMN_QUESTION = "question";
//        public static final String COLUMN_OPTION1 = "option1";
//        public static final String COLUMN_OPTION2 = "option2";
//        public static final String COLUMN_OPTION3 = "option3";
//        public static final String COLUMN_ANSWER = "answer";
//        public static final String COLUMN_RELATED_LESSON = "related_lesson";
//    }
}
