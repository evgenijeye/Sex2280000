package com.example.exam.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.exam.R;
import com.example.exam.screens.SignIn.SignInScreen;

public class LaunchScreen extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);

        SharedPreferences sharedPreferences = getSharedPreferences("o", Context.MODE_PRIVATE);

//        SharedPreferences sharedPreferences = getSharedPreferences("firstLoaded",
//                Context.MODE_PRIVATE);


        boolean firstLoaded = sharedPreferences.getBoolean("fl",true);


        //если первая загрузка
        if(firstLoaded)
        {
            //-> первая загрузка - false
            SharedPreferences.Editor e = sharedPreferences.edit();
            e.putBoolean("fl", false);
            e.apply();
            //->SignUpScreen
            intent = new Intent(LaunchScreen.this, SignInScreen.class);
        }
        //->SignInScreen
        else  intent = new Intent(LaunchScreen.this, SignInScreen.class);

        new Handler().postAtTime(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        },2000);
    }
}