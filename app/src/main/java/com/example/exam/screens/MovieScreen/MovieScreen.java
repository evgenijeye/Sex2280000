package com.example.exam.screens.MovieScreen;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.exam.R;
import com.example.exam.common.AppData;
import com.example.exam.common.CheckData;
import com.example.exam.common.URLs;
import com.example.exam.common.entity.Episode;
import com.example.exam.common.entity.MovieItem;
import com.example.exam.databinding.ActivityMovieScreenBinding;
import com.example.exam.databinding.ItemEpisodeBinding;
import com.example.exam.databinding.ItemTagBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieScreen extends AppCompatActivity {
    ActivityMovieScreenBinding binding;
    int movieItemId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        movieItemId = getIntent().getIntExtra(MovieItem.MOVIE_ID,0);
        loadedMovieInfo();
    }

    private void loadedMovieInfo() {
        JsonObjectRequest logOnRequest = new JsonObjectRequest(Request.Method.POST, URLs.MOVIE_ID(movieItemId), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    initMovieInfo(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckData.makeMessage(error.getMessage(),MovieScreen.this);
            }
        });
        AppData.getInstance(this).queue.add(logOnRequest);
    }
    MovieItem movieItem;
    private void initMovieInfo(JSONObject response) throws JSONException {
        movieItem = new MovieItem(response);
        AppData.getInstance(this).loadImage(movieItem.getPoster(),binding.mainImage);
        binding.titleTV.setText(movieItem.getName());
        int age = response.getInt("age");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initAge(age);
        }
        initTags(response.getJSONArray("tags"));
        String desc = response.getString("description");
        initImages(response.getJSONArray("images"));
        binding.descriptionView.setText(desc);
    }

    List<String> imageItems = new ArrayList<>();
    private void initImages(JSONArray images) {
        for (int i = 0; i < images.length(); i++) {
            try{
                imageItems.add((String) images.get(i));
            }
            catch(JSONException e){
                e.printStackTrace();
            }

        }
        binding.imagesView.setAdapter(new ImageAdapter());
    }

    List<Episode> episodeItems = new ArrayList<>();

    private void loadedEpisodesInfo(){
        JsonArrayRequest logOnRequest = new JsonArrayRequest(Request.Method.GET, URLs.MOVIE_ID_EPISODES(movieItemId), null, new
                Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            initEpisodesInfo(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckData.makeMessage(error.getMessage(),MovieScreen.this);
            }
        });
    }

    private void initEpisodesInfo(JSONArray response) throws JSONException {
        for (int i = 0; i < response.length(); i++) {
            episodeItems.add(new Episode(response.getJSONObject(i)));
        }
        binding.episodesView.setAdapter(new EpisodeAdapter());
    }

    private void initTags(JSONArray tags) {
        for (int i = 0; i < tags.length(); i++) {
            try{
                JSONObject tag = (JSONObject) tags.get(i);
                addTag(tag.getString("tagName"));
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private void addTag(String tagName) {
        ItemTagBinding tag = ItemTagBinding.inflate(getLayoutInflater(),binding.tagContainer,false);
        tag.getRoot().setText(tagName);
        binding.tagContainer.addView(tag.getRoot());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initAge(int age) {
        binding.ageTV.setText(age+"+");
        int colorId = 0;
        switch(age){
            case 6:
                colorId = R.color.age_6;
                break;
                case 12:
                colorId = R.color.age_12;
                break;case 16:
                colorId = R.color.age_16;
                break;case 18:
                colorId = R.color.age_18;
                break; default:
                colorId = R.color.age_0;
                break;

        }
        binding.ageTV.setTextColor(getResources().getColor(colorId,getTheme()));
    }

    private class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(getLayoutInflater().inflate(R.layout.image_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            AppData.getInstance(getApplicationContext()).loadImage(imageItems.get(position),(ImageView) holder.itemView);
        }

        @Override
        public int getItemCount() {
            return imageItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

    private class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(ItemEpisodeBinding.inflate(getLayoutInflater(),parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Episode item = episodeItems.get(position);
            holder.itemEpisodeBinding.titleView.setText(item.getName());
            holder.itemEpisodeBinding.descView.setText(item.getDescription());
            holder.itemEpisodeBinding.yearView.setText(item.getYear());
        }

        @Override
        public int getItemCount() {
            return episodeItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemEpisodeBinding itemEpisodeBinding;

            public ViewHolder(@NonNull ItemEpisodeBinding binding) {
                super(binding.getRoot());
                this.itemEpisodeBinding = binding;
            }
        }
    }
}