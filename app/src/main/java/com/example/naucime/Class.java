package com.example.naucime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Class extends AppCompatActivity {
    private ListView lekcije;
    private TextView progressTextView;
    private ProgressBar predmetProgres;
    private Button quiz;
    private CoordinatorLayout classLayout;
    QuizDbHelper dbHelper = null;
    int class_ID;
    int preSelected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        TextView nazivPredmeta = findViewById(R.id.nazivPredmeta);
        lekcije = findViewById(R.id.lekcije);
        predmetProgres = findViewById(R.id.predmetProgres);
        progressTextView = findViewById(R.id.progressTextView);
        quiz = findViewById(R.id.quiz);
        classLayout = findViewById(R.id.classLayout);


        dbHelper = new QuizDbHelper(this);

        final Intent intent = getIntent();

        nazivPredmeta.setText(nazivPredmeta.getText() + intent.getStringExtra("predmet"));
        switch (intent.getStringExtra("predmet")){
            case "Fizika":
                class_ID = 1;
                lekcije.setBackgroundColor(getResources().getColor(R.color.fizika));
                classLayout.setBackground(getResources().getDrawable(R.drawable.fizika_background));
                break;

            case "Hemija":
                class_ID = 2;
                classLayout.setBackgroundColor(getResources().getColor(R.color.hemija));
                break;

            case "Istorija":
                class_ID = 3;
                classLayout.setBackgroundColor(getResources().getColor(R.color.istorija));
                break;

            case "Geografija":
                class_ID = 4;
                classLayout.setBackgroundColor(getResources().getColor(R.color.geografija));
                break;
            default:
                class_ID = 0;
                break;
        }
        updateSeeker();
//        predmetProgres.setProgress(dbHelper.updateSeeker(class_ID));
//        progressTextView.setText(progressTextView.getText() + (predmetProgres.getProgress() + "%"));
//        if(predmetProgres.getProgress()==100){
//            quiz.setVisibility(View.VISIBLE);
//        }

        fillListView();


//        final List<LessonModel> lessons = new ArrayList<>();
//        List<Lesson> allLessons = dbHelper.getAllLesson(class_ID);
//
//        for(int i=0; i<allLessons.size(); i++){
//            lessons.add(new LessonModel(allLessons.get(i).getRead(), allLessons.get(i).getName(), allLessons.get(i).getId()));
//        }
//        final CustomAdapter adapter = new CustomAdapter(this, lessons);
//
//        lekcije.setAdapter(adapter);
//
//        lekcije.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
//                LessonModel model = lessons.get(i);
//
//                lessons.set(i, model);
//
//                if(preSelected > -1){
//                    LessonModel preRecord = lessons.get(preSelected);
//
//                    lessons.set(preSelected, preRecord);
//                }
//
//                preSelected = i;
//
//                adapter.updateRecords(lessons);
//                view.findViewById(R.id.read).setBackgroundResource(R.drawable.read);
//                Intent lekcija = new Intent(Class.this, Lesson.class);
//                lekcija.putExtra("Id", model.id);
//                startActivity(lekcija);
//                dbHelper.setAsRead(model.id);
//                updateSeeker();
////                predmetProgres.setProgress(dbHelper.updateSeeker(class_ID));
////                progressTextView.setText((predmetProgres.getProgress() + "%"));
//            }
//        });

        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quiz = new Intent(Class.this, Quiz.class);
                quiz.putExtra("classID", class_ID);
                quiz.putExtra("predmet", intent.getStringExtra("predmet"));
                //startActivity(quiz);
                startActivityForResult(quiz, 1);
            }
        });



    }

    public void fillListView(){
        final List<LessonModel> lessons = new ArrayList<>();
        List<Lesson> allLessons = dbHelper.getAllLesson(class_ID);

        for(int i=0; i<allLessons.size(); i++){
            lessons.add(new LessonModel(allLessons.get(i).getRead(), allLessons.get(i).getName(), allLessons.get(i).getId()));
        }
        final CustomAdapter adapter = new CustomAdapter(this, lessons);

        lekcije.setAdapter(adapter);

        lekcije.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                LessonModel model = lessons.get(i);

                lessons.set(i, model);

                if(preSelected > -1){
                    LessonModel preRecord = lessons.get(preSelected);

                    lessons.set(preSelected, preRecord);
                }

                preSelected = i;

                adapter.updateRecords(lessons);
                view.findViewById(R.id.read).setBackgroundResource(R.drawable.read);
                Intent lekcija = new Intent(Class.this, Lesson.class);
                lekcija.putExtra("Id", model.id);
                startActivity(lekcija);
                dbHelper.setAsRead(model.id);
                updateSeeker();
//                predmetProgres.setProgress(dbHelper.updateSeeker(class_ID));
//                progressTextView.setText((predmetProgres.getProgress() + "%"));
            }
        });
        updateSeeker();
    }

    public void updateSeeker(){
        predmetProgres.setProgress(dbHelper.updateSeeker(class_ID));
        progressTextView.setText("Pročitano materijala: " + (predmetProgres.getProgress() + "%"));
        if(predmetProgres.getProgress()==100){
            quiz.setVisibility(View.VISIBLE);
        }else {
            quiz.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillListView();
    }
}
