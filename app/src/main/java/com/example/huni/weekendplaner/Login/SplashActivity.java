package com.example.huni.weekendplaner.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.huni.weekendplaner.Login.LoginActivity;
import com.example.huni.weekendplaner.Main.MainActivity;
import com.example.huni.weekendplaner.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        ImageView splashimageview = findViewById(R.id.splash_imageView);
        ProgressBar spashprogresbar = findViewById(R.id.splash_progressBar);


        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        final String s = settings.getString("username","Dummy");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(s.equals("Dummy")){
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }

            }
        },4*1000); //4 Second Delay
    }
}
