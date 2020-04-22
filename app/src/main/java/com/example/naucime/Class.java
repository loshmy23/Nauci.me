package com.example.naucime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Class extends AppCompatActivity {
    private ListView lekcije;
    private TextView progressTextView;
    private ProgressBar predmetProgres;
    private Button quiz;
    private ImageView background;
    private ImageView transparentBackground;
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
        background = findViewById(R.id.background);
        transparentBackground = findViewById(R.id.transparentBackground);


        dbHelper = new QuizDbHelper(this);

        final Intent intent = getIntent();

        nazivPredmeta.setText(nazivPredmeta.getText() + intent.getStringExtra("predmet"));
        switch (intent.getStringExtra("predmet")){
            case "Fizika":
                class_ID = 1;
                lekcije.setBackground(getResources().getDrawable(R.drawable.fizika_lessons));
                background.setBackground(getResources().getDrawable(R.drawable.fizika));
                transparentBackground.setBackground(getResources().getDrawable(R.color.fizikaBackground));
                predmetProgres.getProgressDrawable().setColorFilter(Color.parseColor("#0088d6"), PorterDuff.Mode.SRC_IN);
                break;

            case "Hemija":
                class_ID = 2;
                lekcije.setBackground(getResources().getDrawable(R.drawable.hemija_lessons));
                background.setBackground(getResources().getDrawable(R.drawable.hemija));
                transparentBackground.setBackground(getResources().getDrawable(R.color.hemijaBackground));
                break;

            case "Istorija":
                class_ID = 3;
                lekcije.setBackground(getResources().getDrawable(R.drawable.istorija_lessons));
                background.setBackground(getResources().getDrawable(R.drawable.image));
                transparentBackground.setBackground(getResources().getDrawable(R.color.istorijaBackground));
                break;

            case "Geografija":
                class_ID = 4;
                lekcije.setBackground(getResources().getDrawable(R.drawable.geografija_lessons));
                background.setBackground(getResources().getDrawable(R.drawable.geografija));
                transparentBackground.setBackground(getResources().getDrawable(R.color.geografijaBackground));
                break;
            default:
                class_ID = 0;
                break;
        }
        updateSeeker();

        fillListView();

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
        List<LessonModel> allLessons = dbHelper.getAllLesson(class_ID);

        for(int i=0; i<allLessons.size(); i++){
            lessons.add(new LessonModel(allLessons.get(i).getId(), allLessons.get(i).getName(), allLessons.get(i).getRead()));
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
                lekcija.putExtra("Id", model.getId());
                startActivity(lekcija);
                dbHelper.setAsRead(model.getId());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
