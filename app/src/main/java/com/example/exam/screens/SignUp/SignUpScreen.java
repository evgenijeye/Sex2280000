package com.example.exam.screens.SignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.request.RequestListener;
import com.example.exam.R;
import com.example.exam.common.AppData;
import com.example.exam.common.CheckData;
import com.example.exam.common.URLs;
import com.example.exam.databinding.ActivitySignUpScreenBinding;
import com.example.exam.screens.SignIn.SignInScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class SignUpScreen extends AppCompatActivity {
    ActivitySignUpScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
    public void SignUp(View view){
        if(!binding.editTextTextEmailAddress.getText().toString().equals("")&&
                !binding.etsurname.getText().toString().equals("")&&
                !binding.etRepeatPassword.getText().toString().equals("")&&
                !binding.etPassword.getText().toString().equals("")&&
                !binding.etName.getText().toString().equals(""))
        {
         if(CheckData.checkMail(binding.editTextTextEmailAddress.getText().toString()))
         {
            if(binding.etPassword.getText().toString().equals(binding.etRepeatPassword.getText().toString()))
            {
                JSONObject user = new JSONObject();
                try{
                    user.put("email",binding.editTextTextEmailAddress.getText().toString());
                            user.put("password",binding.etPassword.getText().toString());
                            user.put("firstName",binding.etsurname.getText().toString());
                            user.put("lastName",binding.etName.getText().toString());
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://cinema.areas.su/auth/register", user,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.getMessage()!=null){
                            if(error.getMessage().contains("Успешная")){
                                CheckData.authConfirmed(SignUpScreen.this,binding.editTextTextEmailAddress.getText().toString(),binding.etPassword.getText().toString());

                            }
                            else{
                                CheckData.makeMessage("Проблема с регистрацией",SignUpScreen.this);
                            }
                        }
                        else{
                            CheckData.makeMessage("Проблема с регистрацией",SignUpScreen.this);
                        }
                    }
                });

                JsonObjectRequest signUpRequest = new JsonObjectRequest(Request.Method.POST, URLs.REGISTER, user,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                CheckData.authConfirmed(SignUpScreen.this, binding.editTextTextEmailAddress.getText().toString(), binding.etPassword.getText().toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(Objects.requireNonNull(error.getMessage()).contains("Успешная"))
                        {

                            //будет вызывать авторизацию
                            CheckData.authConfirmed(SignUpScreen.this,
                                    binding.editTextTextEmailAddress.getText().toString(),
                                    binding.etPassword.getText().toString());
                        }
                        else
                            CheckData.makeMessage("Проблема с регистрацией ошибка", SignUpScreen.this);
                    }
                });
                AppData.getInstance(this).queue.add(signUpRequest);

            }
            else{
                CheckData.makeMessage("Пароли не совпадают",SignUpScreen.this);
            }
         }
         else{
             CheckData.makeMessage("Некорректный почта",SignUpScreen.this);
         }
        }
        else{
            CheckData.makeMessage("Есть пустые поля", SignUpScreen.this);
        }
    }
    public void CanSign(View view){
        Intent intent=new Intent(SignUpScreen.this, SignInScreen.class);
        startActivity(intent);
        finish();
    }
}