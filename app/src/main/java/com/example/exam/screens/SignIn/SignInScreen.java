package com.example.exam.screens.SignIn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.exam.R;
import com.example.exam.common.CheckData;
import com.example.exam.databinding.ActivitySignInScreenBinding;
import com.example.exam.screens.SignUp.SignUpScreen;

public class SignInScreen extends AppCompatActivity {
    ActivitySignInScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
    public void SignIn(View view){
    if(!binding.editTextTextEmailAddress.getText().toString().equals("")&&!binding.etPassword.getText().toString().equals("")){
        if(CheckData.checkMail(binding.editTextTextEmailAddress.getText().toString()))
        {
            CheckData.authConfirmed(SignInScreen.this, binding.editTextTextEmailAddress.getText().toString(),binding.etPassword.getText().toString());
        }
        else{CheckData.makeMessage("Некорректный email",this);}
    }
    else{CheckData.makeMessage("Пустые поля",this);}

    }
    public void GoSignUp(View view){
        Intent signUp = new Intent(SignInScreen.this, SignUpScreen.class);
        startActivity(signUp);
        finish();

    }
}