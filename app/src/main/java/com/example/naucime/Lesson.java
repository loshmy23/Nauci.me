package com.example.naucime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class Lesson extends AppCompatActivity implements View.OnClickListener{

    private String name;
    private String text;
    private int read;
    private int class_ID;
    private int id;
    private int currentIndex = 0;
    private Question currentQuestion;
    private List<Question> questions;

    private TextView lessonName;
    private TextView lessonText;
    private TextView question;
    private Button toggleLesson;
    private Button toggleMiniQuiz;
    private Button lessonOption1;
    private Button lessonOption2;
    private Button lessonOption3;
    private Button previousQuestion;
    private Button nextQuestion;
    private LinearLayout lessonLayout;
    private LinearLayout miniQuizLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        lessonName = findViewById(R.id.lessonNameTextView);
        lessonText = findViewById(R.id.lessonTextTextView);
        question = findViewById(R.id.miniQuizQuestionTextView);
        toggleLesson = findViewById(R.id.toggleLessonButton);
        toggleMiniQuiz = findViewById(R.id.toggleMiniQuiz);
        lessonOption1 = findViewById(R.id.lessonOption1);
        lessonOption2 = findViewById(R.id.lessonOption2);
        lessonOption3 = findViewById(R.id.lessonOption3);
        previousQuestion = findViewById(R.id.previousQuestion);
        nextQuestion = findViewById(R.id.nextQuestion);
        lessonLayout = findViewById(R.id.lessonLayout);
        miniQuizLayout = findViewById(R.id.miniQuizLayout);

        Intent intent = getIntent();
        id = intent.getIntExtra("Id", 0);

        final QuizDbHelper dbHelper = new QuizDbHelper(this);
        final Lesson lesson = dbHelper.getLesson(id);

        toggleLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lessonLayout.setVisibility(View.VISIBLE);
                miniQuizLayout.setVisibility(View.GONE);
            }
        });

        toggleMiniQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miniQuizLayout.setVisibility(View.VISIBLE);
                lessonLayout.setVisibility(View.GONE);
                questions = dbHelper.getMiniQuizQuestions(id);
                nextMiniQuizQuestion();
            }
        });
        lessonOption1.setOnClickListener(this);
        lessonOption2.setOnClickListener(this);
        lessonOption3.setOnClickListener(this);

        lessonName.setText(lesson.getName());
        lessonText.setText(lesson.getText());
        previousQuestion.setText("<<");
        nextQuestion.setText(">>");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lessonOption1:
                lessonOption1.setBackgroundResource(R.color.red);
                break;
            case R.id.lessonOption2:
                lessonOption2.setBackgroundResource(R.color.red);
                break;
            case R.id.lessonOption3:
                lessonOption3.setBackgroundResource(R.color.red);
                break;
        }
        checkAnswer();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextMiniQuizQuestion();
            }
        }, 500);

    }

    private void clearAnswer() {
        lessonOption1.setBackgroundColor(Color.parseColor(
                "#00000000"
        ));
        lessonOption2.setBackgroundColor(Color.parseColor(
                "#00000000"
        ));
        lessonOption3.setBackgroundColor(Color.parseColor(
                "#00000000"
        ));
    }

    private void checkAnswer() {
        switch (currentQuestion.getAnswer()){
            case 1:
                lessonOption1.setBackgroundResource(R.color.green);
                break;
            case 2:
                lessonOption2.setBackgroundResource(R.color.green);
                break;
            case 3:
                lessonOption3.setBackgroundResource(R.color.green);
                break;
        }
    }

    private void nextMiniQuizQuestion() {
        clearAnswer();
        currentQuestion = questions.get(currentIndex);
        question.setText(currentQuestion.getQuestion());
        lessonOption1.setText(currentQuestion.getOption1());
        lessonOption2.setText(currentQuestion.getOption2());
        lessonOption3.setText(currentQuestion.getOption3());
        currentIndex++;
    }

    public Lesson() {
    }

    public Lesson(int id, String name, String text, int read, int class_ID) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.read = read;
        this.class_ID = class_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getClass_ID() {
        return class_ID;
    }

    public void setClass_ID(int class_ID) {
        this.class_ID = class_ID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
