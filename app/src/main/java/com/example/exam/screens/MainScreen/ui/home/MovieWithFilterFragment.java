package com.example.exam.screens.MainScreen.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.exam.R;
import com.example.exam.common.AppData;
import com.example.exam.common.CheckData;
import com.example.exam.common.URLs;
import com.example.exam.common.entity.MovieItem;
import com.example.exam.databinding.FragmentMovieWithFilterBinding;
import com.example.exam.databinding.ItemMoviePosterBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class MovieWithFilterFragment extends Fragment {
    AppData appData;
    private String filter;
    FragmentMovieWithFilterBinding binding;

    public MovieWithFilterFragment(String filter) {
        // Required empty public constructor
        this.filter = filter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMovieWithFilterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appData = AppData.getInstance(getContext());
        initPosters();
    }

    private void initPosters() {
        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                URLs.MOVIE_WITH_FILTER + filter,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        initMovies(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );

        AppData.getInstance(getContext()).queue.add(arrayRequest);
    }

    List<MovieItem> movieItems = new ArrayList<>();

    private void initMovies(JSONArray response) {
        for (int i = 0; i < response.length(); i++) {
            try {
                movieItems.add(new MovieItem((JSONObject) response.get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        binding.moviesView.setAdapter(new PosterAdapter());
    }

    private class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemMoviePosterBinding itemMoviePosterBinding = ItemMoviePosterBinding.inflate(getLayoutInflater(),parent,false);
            return new ViewHolder(itemMoviePosterBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final MovieItem item = movieItems.get(position);
            appData.loadImage(item.getPoster(),holder.itemMoviePosterBinding.posterView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckData.openMovie(getActivity(),item.getMovieId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return movieItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemMoviePosterBinding itemMoviePosterBinding;
            public ViewHolder(ItemMoviePosterBinding itemMoviePosterBinding) {
                super(itemMoviePosterBinding.getRoot());
                this.itemMoviePosterBinding = itemMoviePosterBinding;
            }
        }
    }
}