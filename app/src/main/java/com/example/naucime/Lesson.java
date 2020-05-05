package com.example.naucime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.AndroidException;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.jar.Manifest;

public class Lesson extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    private int id = 0;
    private int currentIndex = 0;
    private String url = "";
    private Question currentQuestion;
    private List<Question> questions;
    private List<PhotoSettings> settings;

    private TextView lessonName;
    private TextView question;
    private TextView datum;
    private Button toggleLesson;
    private Button toggleMiniQuiz;
    private Button lessonOption1;
    private Button lessonOption2;
    private Button lessonOption3;
    private Button previousQuestion;
    private Button nextQuestion;
    private Button showVideo;
    private Button showZoomOptions;
    private LinearLayout zoomButtons;
    private Button zoomIn;
    private Button zoomOut;
    private Button zoom100;
    private FrameLayout lessonLayout;
    private LinearLayout miniQuizLayout;
    private PDFView pdfView;
    private ImageView formula;
    //private WebView videoView;


    final QuizDbHelper dbHelper = new QuizDbHelper(this);
    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private YouTubePlayer youTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        lessonName = findViewById(R.id.lessonNameTextView);
        question = findViewById(R.id.miniQuizQuestionTextView);
        datum = findViewById(R.id.datum);
        toggleLesson = findViewById(R.id.toggleLessonButton);
        toggleMiniQuiz = findViewById(R.id.toggleMiniQuiz);
        lessonOption1 = findViewById(R.id.lessonOption1);
        lessonOption2 = findViewById(R.id.lessonOption2);
        lessonOption3 = findViewById(R.id.lessonOption3);
        previousQuestion = findViewById(R.id.previousQuestion);
        nextQuestion = findViewById(R.id.nextQuestion);
        lessonLayout = findViewById(R.id.lessonLayout);
        miniQuizLayout = findViewById(R.id.miniQuizLayout);
        pdfView = findViewById(R.id.pdfView);
        //fitToScreen = findViewById(R.id.fitToScreen);
        showVideo = findViewById(R.id.showVideo);
        showZoomOptions = findViewById(R.id.showZoomOptions);
        zoomButtons = findViewById(R.id.zoomButtons);
        zoom100 = findViewById(R.id.zoom100);
        zoomIn = findViewById(R.id.zoomIn);
        zoomOut = findViewById(R.id.zoomOut);
        formula = findViewById(R.id.formula);
        //videoView = findViewById(R.id.videoView);
        youTubeView = findViewById(R.id.youtube);
        youTubeView.initialize(YouTubeConfig.getApiKey(), this);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);


        Intent intent = getIntent();
        id = intent.getIntExtra("Id", 0);
        final LessonModel lesson = dbHelper.getLesson(id);
        settings = dbHelper.getSettings(lesson.getLessonCode());
        pdfView.fromAsset(lesson.getFilename())
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        editBoard(page);
                    }
                })
                .onRender(new OnRenderListener() {
                    @Override
                    public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                        pdfView.fitToWidth();
                    }
                })
                .load();
        pdfView.zoomTo(1.5f);

        zoom100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfView.fitToWidth(pdfView.getCurrentPage());
            }
        });

        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfView.zoomWithAnimation(pdfView.getZoom()+0.5f);
            }
        });
        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfView.zoomWithAnimation(pdfView.getZoom()-0.5f);
            }
        });

        showVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVideo();
            }
        });
        showZoomOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(zoomButtons.getVisibility()==View.GONE){
                    zoomButtons.setVisibility(View.VISIBLE);
                    showZoomOptions.setBackground(getResources().getDrawable(R.drawable.cancel));
                }else{
                    zoomButtons.setVisibility(View.GONE);
                    showZoomOptions.setBackground(getResources().getDrawable(R.drawable.zoom));
                }
            }
        });

//        videoView.setWebViewClient(new WebViewClient());
//        videoView.getSettings().setJavaScriptEnabled(true);
//        videoView.loadUrl("https://www.youtube.com/watch?v=W4hTJybfU7s");

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat sf = new SimpleDateFormat("dd-MMM-yyyy");
        datum.setText(sf.format(c));

        toggleLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lessonLayout.setVisibility(View.VISIBLE);
                miniQuizLayout.setVisibility(View.GONE);
                toggleMiniQuiz.setVisibility(View.VISIBLE);
                toggleLesson.setVisibility(View.GONE);

            }
        });
        toggleLesson.setBackgroundColor(Color.parseColor(intent.getStringExtra("color")));

        toggleMiniQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miniQuizLayout.setVisibility(View.VISIBLE);
                lessonLayout.setVisibility(View.GONE);
                toggleMiniQuiz.setVisibility(View.GONE);
                toggleLesson.setVisibility(View.VISIBLE);
                questions = dbHelper.getMiniQuizQuestions(lesson.getLessonCode());
                nextMiniQuizQuestion();
            }
        });
        toggleMiniQuiz.setBackgroundColor(Color.parseColor(intent.getStringExtra("color")));
        lessonOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickOption(v);
            }
        });
        lessonOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickOption(v);
            }
        });
        lessonOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickOption(v);
            }
        });

        lessonName.setText(lesson.getName());
        lessonName.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        //lessonText.setText(lesson.getText());
        previousQuestion.setText("<<");
        previousQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMiniQuizQuestion();
            }
        });
        nextQuestion.setText(">>");
        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMiniQuizQuestion();
            }
        });
    }

    private void toggleVideo() {
        if(youTubeView.getVisibility()==View.VISIBLE){
            showVideo.setBackground(getResources().getDrawable(R.drawable.youtube));
            youTubeView.setVisibility(View.GONE);
        }else{
            youTubeView.setVisibility(View.VISIBLE);
            showVideo.setBackground(getResources().getDrawable(R.drawable.cancel));
            if(url!=""){
                youTubePlayer.cueVideo(url.substring(1));
            }
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            this.youTubePlayer = youTubePlayer;
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format("Player Error:", youTubeInitializationResult.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YouTubeConfig.getApiKey(), this);
        }
    }

    protected YouTubePlayerView getYouTubePlayerProvider() {
        return youTubeView;
    }

    private void editBoard(int page) {
        for (int i=0; i<settings.size(); i++){
            int photoPage = settings.get(i).getPhotoPage();
            String photo = settings.get(i).getPhoto();
            if(photoPage == page+1){
                if(photo.startsWith("=")){
                    if(youTubeView.getVisibility()!=View.GONE && url != photo){
                        youTubePlayer.cueVideo(photo.substring(1));
                    }
                    url = photo;
                    break;
                }
                formula.setVisibility(View.VISIBLE);
                InputStream is = null;
                try {
                    is = this.getResources().getAssets().open(settings.get(i).getPhoto());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                formula.setImageBitmap(BitmapFactory.decodeStream(is));
                break;
            }
            formula.setVisibility(View.GONE);
        }
//        int i=0;
//        while (i<settings.size()){
//            if(settings.get(i).getPhotoPage() == page){
//                formula.setVisibility(View.VISIBLE);
//
//            }
//        }
//        if(page==2){
//            formula.setVisibility(View.VISIBLE);
//            formula.setBackground(getResources().getDrawable(R.drawable.druginjutnovzakon));
//        }else {
//            formula.setVisibility(View.GONE);
//        }
    }


    public void onClickOption(View v) {
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
        if(currentIndex != questions.size()-1){
            currentIndex++;
        }else {
            currentIndex = 0;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }


}

