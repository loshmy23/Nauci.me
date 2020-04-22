package com.example.naucime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.reserve);


        Button fizika = findViewById(R.id.fizikaButton);
        Button hemija = findViewById(R.id.hemijaButton);
        Button istorija = findViewById(R.id.istorijaButton);
        Button geografija = findViewById(R.id.geografijaButton);

        fizika.setOnClickListener(this);
        hemija.setOnClickListener(this);
        istorija.setOnClickListener(this);
        geografija.setOnClickListener(this);



//        Covjek ujna = new Covjek("Dubravka", 55, "Pljevlja");
//        Covjek sasa = new Covjek("Sasa", 44, "Pljevlja");
//
//        ujna.reci("Danice idi spavaj");
//        sasa.reci("Jano idi spavaj");
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.fizikaButton:
                Intent fizika = new Intent(MainActivity.this, Class.class);
                fizika.putExtra("predmet", "Fizika");
                startActivity(fizika);
                break;
            case R.id.hemijaButton:
                Intent hemija = new Intent(MainActivity.this, Class.class);
                hemija.putExtra("predmet", "Hemija");
                startActivity(hemija);
                break;
            case R.id.istorijaButton:
                Intent istorija = new Intent(MainActivity.this, Class.class);
                istorija.putExtra("predmet", "Istorija");
                startActivity(istorija);
                break;
            case R.id.geografijaButton:
                Intent geografija = new Intent(MainActivity.this, Class.class);
                geografija.putExtra("predmet", "Geografija");
                startActivity(geografija);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    //    public class Covjek {
//        String ime;
//        int godina;
//        String grad;
//
//        public Covjek(){
//
//        }
//
//        public Covjek(String ulazniArg1, int ulazniArg2, String ulazniArg3){
//            this.ime = ulazniArg1;
//            this.godina = ulazniArg2;
//            this.grad = ulazniArg3;
//        }
//
//        public String getIme() {
//            return ime;
//        }
//
//        public void setIme(String ime) {
//            this.ime = ime;
//        }

//        public int getGodina() {
//            return godina;
//        }
//
//        public void setGodina(int godina) {
//            this.godina = godina;
//        }
//
//        public String getGrad() {
//            return grad;
//        }
//
//        public void setGrad(String grad) {
//            this.grad = grad;
//        }
//
//        public void reci(String a){
//            Toast toast = Toast.makeText(getApplicationContext(), "Zdravo, moje ime je " + this.ime + ", imam " + this.godina
//                    + " godina i zivim u gradu " + this.grad + " a moj ulazni argument je: " + a, Toast.LENGTH_LONG);
//            toast.show();
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
    }
}
