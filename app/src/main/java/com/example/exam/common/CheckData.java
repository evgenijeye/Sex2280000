package com.example.exam.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.exam.common.entity.User;
import com.example.exam.screens.MainScreen.MainScreen;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckData {
    public static boolean checkMail(String mail){
        return mail.matches("[a-z0-9]+@[a-z0-9]+.[a-z]{1,3}");
    }
    public static void makeMessage(String message, Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Ошибка");
        builder.setMessage(message);
        builder.show();
//        new AlertDialog.Builder(context)
//                .setTitle("Ошибка")
//                .setMessage(message)
//                .show();
    }
    public static void authConfirmed(final Activity activity, String login, String pass) {


        final JSONObject user = new JSONObject();
        try {
            user.put(User.EMAIL, login);
            user.put(User.PASSWORD, pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest logonRequest = new JsonObjectRequest(Request.Method.POST,
                URLs.LOGON, user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    AppData.token = response.getLong("token");
                    User.getCurrentUser().setToken(response.getLong("token"));
                    Intent intent = new Intent(activity, MainScreen.class);
                    activity.startActivity(intent);
                    activity.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                makeMessage(error.getMessage(), activity);
            }
        });

        AppData.getInstance(activity).queue.add(logonRequest);
    }

    public  static  void openMovie(Activity activity, String movieId)
    {
        Intent movie = new Intent(activity, MovieScreen.class);
        movie.putExtra(MovieItem.MOVIE_ID,movieId);
        activity.startActivity(movie);
    }
}
