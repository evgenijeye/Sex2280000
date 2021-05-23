package com.example.exam.screens.MainScreen.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.request.RequestListener;
import com.example.exam.R;
import com.example.exam.common.AppData;
import com.example.exam.common.CheckData;
import com.example.exam.common.URLs;
import com.example.exam.common.entity.MovieItem;
import com.example.exam.common.entity.User;
import com.example.exam.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
//import com.example.exam.screens.MainScreen.R;

public class HomeFragment extends Fragment {

    FragmentHomeBinding fragmentHomeBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater,container,false);
        return fragmentHomeBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadCoverInfo();
    }

    String coverId = null;
    private void loadCoverInfo() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URLs.COVER, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String backgroundImage = response.getString("backgroundImage");
                    String foregroundImage = response.getString("foregroundImage");
                    coverId = response.getString("movieId");
                    appData.loadImage(backgroundImage,fragmentHomeBinding.coverBackImage);
                    appData.loadImage(foregroundImage,fragmentHomeBinding.coverFrontImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        appData.queue.add(jsonObjectRequest);
        fragmentHomeBinding.wtchCover.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                CheckData.openMovie(getActivity(),coverId);
            }
        });
    }

    AppData appData;
    private void loadLastInfo(){
        JsonArrayRequest lastReq = new JsonArrayRequest(Request.Method.GET, URLs.MOVIE_LAST, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                            initLastMovie(response);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String > head = new HashMap<>();
                head.put("Authorization","Bearer"+ User.getCurrentUser().getToken());
                return head;
            }
        };

        appData.queue.add(lastReq);
    }
    private void initLastMovie(JSONArray response){
        try {
            JSONObject lastMovie = (JSONObject) response.get(0);
            final MovieItem last = new MovieItem(lastMovie);
            appData.loadImage(last.getPoster(),fragmentHomeBinding.lastBackImage);
            fragmentHomeBinding.lastMovieName.setText(last.getName());
            fragmentHomeBinding.wtchLast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckData.openMovie(getActivity(),last.getMovieId());
                }
            });
        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }
    private void initTabsMovie(){
        fragmentHomeBinding.moviePager.setUserInputEnabled(false);
        fragmentHomeBinding.moviePager.setAdapter(new FragmentMovieAdapter(this));
        new TabLayoutMediator(fragmentHomeBinding.movieTabs, fragmentHomeBinding.moviePager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                String flag;
                switch (position){
                    case 0:
                        flag="Новое";
                        break;
                    case 1:
                        flag="В тренде";
                        break;
                    default:
                        flag="Для вас";
                                break;
                }
                tab.setText(flag);


            }
        }).attach();
    }

    private class FragmentMovieAdapter extends FragmentStateAdapter {

        public FragmentMovieAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            String filter;
            switch(position){
                case 0:
                    filter="new";
                    break;
                case 1:
                    filter="inTrend";
                    break;
                default:
                    filter="forMe";
                    break;
            }
            return new MovieWithFilterFragment(filter);
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}