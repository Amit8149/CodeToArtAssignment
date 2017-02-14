package com.abc.codetoartassignment.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.abc.codetoartassignment.MySingleton;
import com.abc.codetoartassignment.R;

import com.abc.codetoartassignment.adapter.MovieAdapter;
import com.abc.codetoartassignment.model.Movie;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import static com.abc.codetoartassignment.utility.Constants.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Movie> movieList = new ArrayList<>();
    RecyclerView recyclerView;
    MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        //listView = (ListView) findViewById(R.id.listView);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(MAIN_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    JSONArray results = response.getJSONArray(JSON_ARRAY_RESULT);

                    int index = 0;
                    while(index < results.length()) {

                        JSONObject movie = results.getJSONObject(index);

                        if (movie != null)
                            movieList.add(new Movie(
                                    movie.getString(JSON_OBJECT_POSTER_PATH),
                                    movie.getBoolean(JSON_OBJECT_ADULT),
                                    movie.getString(JSON_OBJECT_OVERVIEW),
                                    movie.getString(JSON_OBJECT_RELEASE_DATE),
                                    movie.getInt(JSON_OBJECT_ID),
                                    movie.getString(JSON_OBJECT_ORIGINAL_TITLE),
                                    movie.getString(JSON_OBJECT_ORIGINAL_LANGUAGE),
                                    movie.getString(JSON_OBJECT_TITLE),
                                    movie.getString(JSON_OBJECT_BACKDROP_PATH),
                                    movie.getDouble(JSON_OBJECT_POPULARITY),
                                    movie.getInt(JSON_OBJECT_VOTE_COUNT),
                                    movie.getBoolean(JSON_OBJECT_VIDEO),
                                    movie.getDouble(JSON_OBJECT_VOTE_AVERAGE)
                            ));

                        index++;

                        movieAdapter = new MovieAdapter(movieList,MainActivity.this);
                        recyclerView.setAdapter(movieAdapter);
                     }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Somthing went wrong", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.item1){
            Intent intent=new Intent(MainActivity.this,InformationActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
