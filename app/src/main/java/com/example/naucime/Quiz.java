package com.example.naucime;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public class Quiz extends Activity {
    private TextView className;
    private TextView questionCount;
    private TextView countdown;
    private TextView question;
    private TextView currentScore;
    private Button previousQuestion;
    private Button nextQuestion;
    private RadioGroup options;
    private RadioButton option1;
    private RadioButton option2;
    private RadioButton option3;
    private ProgressBar countdownProgress;
    private Dialog dialog;
    private ImageView pictureQuestion;

    private List<Question> questions;
    private int classID;
    private int questionCounter = 0;
    private int questionCountTotal = 0;
    private static final long countdownCount = 10000;
    private Question correctQuestion;
    private QuizDbHelper dbHelper = null;
    private CountDownTimer countDownTimer;
    private long timeLeft;
    private String lessonsToRead ="";

    private int score;
    private String relatedLesson;
    private boolean answered = true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Spremni za kviz?")
                .setCancelable(true)
                .setPositiveButton("Spremni!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setContentView(R.layout.e);
                        manualOnCreate();
                    }
                })
                .setNegativeButton("Još samo malo!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish();
                    dialog.dismiss();
                }
                return true;
            }
        });

    }

    private void hideSystemUI() {
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
    private void showSystemUI() {
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public void manualOnCreate(){
        hideSystemUI();
        dbHelper = new QuizDbHelper(this);
        Intent intent = getIntent();
        classID = intent.getIntExtra("classID", -1);
        className = findViewById(R.id.className);
        className.setText(intent.getStringExtra("predmet"));
        question = findViewById(R.id.question);
        currentScore = findViewById(R.id.currentScore);
        questionCount = findViewById(R.id.questionCount);
        countdown = findViewById(R.id.countdown);
        //previousQuestion = findViewById(R.id.previousQuestion);
        //nextQuestion = findViewById(R.id.nextQuestion);
        options = findViewById(R.id.options);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        countdownProgress = findViewById(R.id.countdownProgress);
        pictureQuestion = findViewById(R.id.pictureQuestion);

        options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(options.getCheckedRadioButtonId()!=-1 && answered == true){
                    final RadioButton selected = findViewById(options.getCheckedRadioButtonId());
                    if (selected != null){
                        selected.setBackground(getResources().getDrawable(R.drawable.selected_option));
                        checkAnswer(1);
                    }
                }
            }
        });

        questions = dbHelper.getAllQuestions(classID);
        questionCountTotal = questions.size();
        Collections.shuffle(questions);
        showNextQuestion();

    }
    @Nullable
    public void checkAnswer(final int a){
        countDownTimer.cancel();
        final RadioButton selected = findViewById(options.getCheckedRadioButtonId());
        final int answer = options.indexOfChild(selected) + 1;

        if(answer == correctQuestion.getAnswer() && a == 1){
            score++;
            currentScore.setText(score + "");
        }else {
            if(!lessonsToRead.contains(dbHelper.unreadLesson(relatedLesson))){
                lessonsToRead += dbHelper.unreadLesson(relatedLesson) + "\n";
            }
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(a == 1) {
                    selected.setBackground(getResources().getDrawable(R.drawable.underline));
                    showNextQuestion();
                }
            }
        }, 500);
    }



    private void showNextQuestion() {
        answered = false;
        options.clearCheck();
        answered = true;
        if(questionCounter < questionCountTotal){
            correctQuestion = questions.get(questionCounter);
            if(correctQuestion.getQuestionType().equals("T")){
                question.setVisibility(View.VISIBLE);
                pictureQuestion.setVisibility(View.GONE);
                question.setText(correctQuestion.getQuestion());
            }else {
                question.setVisibility(View.GONE);
                pictureQuestion.setVisibility(View.VISIBLE);
                InputStream is = null;
                try {
                    is = this.getResources().getAssets().open(correctQuestion.getQuestion());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pictureQuestion.setImageBitmap(BitmapFactory.decodeStream(is));
                //700168
            }

            option1.setText(correctQuestion.getOption1());
            option2.setText(correctQuestion.getOption2());
            option3.setText(correctQuestion.getOption3());
            relatedLesson = correctQuestion.getRelatedLessonCode();
            questionCounter++;
            questionCount.setText(questionCounter + "/" + questionCountTotal);
            timeLeft = countdownCount;
            countdownProgress.setProgress(100);
            countdown();
        }else{
//            Toast toast = Toast.makeText(getApplicationContext(), "Vas rezultat je: " + score, Toast.LENGTH_LONG);
//            toast.show();
            finishQuiz();
        }
    }

    private void countdown() {
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                System.out.println(timeLeft);
                updateCountdownText();
                if(timeLeft < 2000){
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            countDownTimer.onFinish();
                        }
                    }, timeLeft%1000);
                    countdownProgress.setProgress(countdownProgress.getProgress()-10);
                }else {
                    countdownProgress.setProgress(countdownProgress.getProgress()-10);
                }

            }

            @Override
            public void onFinish() {
                timeLeft = 0;
                updateCountdownText();
                countdownProgress.setProgress(0);
                checkAnswer(0);
                answered = true;
                showNextQuestion();
            }
        }.start();
    }

    private void updateCountdownText() {
        int seconds = (int) (timeLeft/1000);
        countdown.setText(seconds + "");
    }

    private void finishQuiz() {
        dialog = new Dialog(this);
        showDialog();
        //finish();
    }

    private void showDialog(){
        TextView lessonList;
        Button close;
        if(lessonsToRead != ""){
            dialog.setContentView(R.layout.quiz_summary);
            lessonList = dialog.findViewById(R.id.lessonsToReadList);
            lessonList.setText(lessonsToRead);
        }else{
            dialog.setContentView(R.layout.dialog_well_done);
        }
        close = dialog.findViewById(R.id.closeDialog);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Ne zelite vise da se igrate?")
                .setCancelable(true)
                .setPositiveButton("Zelim!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton("Ne zelim!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
        if(dbHelper!=null){
            dbHelper.close();
        }
    }

    @Override
    public void finish() {
        super.finish();
    }


}
