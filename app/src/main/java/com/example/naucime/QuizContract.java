package com.example.naucime;

import android.provider.BaseColumns;

public final class QuizContract {

    public static class ClassTable implements BaseColumns {
        public static final String TABLE_NAME = "class";
        public static final String COLUMN_NAME = "name";
    }

    public static class LessonTable implements BaseColumns {
        public static final String TABLE_NAME = "lesson";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_READ = "read";
        public static final String COLUMN_CLASS_ID = "class_id";
    }

    public static class QuizTable implements  BaseColumns{
        public static final String TABLE_NAME = "quiz";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_OPTION1 = "option1";
        public static final String COLUMN_OPTION2 = "option2";
        public static final String COLUMN_OPTION3 = "option3";
        public static final String COLUMN_ANSWER = "answer";
        public static final String COLUMN_RELATED_LESSON = "related_lesson";
    }
}
