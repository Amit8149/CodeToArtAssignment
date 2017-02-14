package com.abc.codetoartassignment.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.abc.codetoartassignment.R;
import com.abc.codetoartassignment.adapter.SidlerAdapter;

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

import static com.abc.codetoartassignment.utility.Constants.*;

public class DetailsActivity extends AppCompatActivity {

    TextView titleTextView,overViewTextView;
    RatingBar ratingBar;

    ArrayList<String> imagesArrayList = new ArrayList<>();

    ViewPager imageViewPager;
    SidlerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        titleTextView = (TextView)findViewById(R.id.titleTextView);
        overViewTextView = (TextView)findViewById(R.id.overViewTextView);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ratingBar.setClickable(false);
        getSupportActionBar().setTitle(getIntent().getStringExtra(JSON_OBJECT_ORIGINAL_TITLE));
        imageViewPager = (ViewPager) findViewById(R.id.imageViewPager);

        new ImageTask().execute(DETAILS_IMAGE_PATH + getIntent().getIntExtra(JSON_OBJECT_ID,0) + DETAILS_IMAGES + PARAMETER);
        new MovieDetailsTask().execute(DETAILS_URL + getIntent().getIntExtra(JSON_OBJECT_ID, 0) + PARAMETER);

    }

    class ImageTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String strUrl = urls[0];
            URL url = null;
            URLConnection conn= null;
            InputStream stream = null;
            StringBuilder builder = null;
            InputStreamReader reader = null;
            try {
                url = new URL(strUrl);
                conn = url.openConnection();
                stream = conn.getInputStream();
                reader = new InputStreamReader(stream);
                builder = new StringBuilder();
                int ch = 0;
                while ((ch = reader.read()) != -1) {
                    builder.append((char) ch);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(JSON_OBJECT_DETAILS_POSTERS);

                int index = 0;
                while(index < jsonArray.length() && index <5){
                    imagesArrayList.add(jsonArray.getJSONObject(index).getString(JSON_OBJECT_FILE_PATH));
                    index++;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter = new SidlerAdapter(DetailsActivity.this,imagesArrayList);
            imageViewPager.setAdapter(adapter);
        }

    }

    private class MovieDetailsTask extends AsyncTask<String,Void,String>{

        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog=new ProgressDialog(DetailsActivity.this);
            dialog.setTitle("Loading...");
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            String strUrl = urls[0];

            URL url = null;
            URLConnection conn= null;
            InputStream stream = null;
            StringBuilder builder = null;
            InputStreamReader reader = null;

            try {
                url = new URL(strUrl);
                conn = url.openConnection();
                stream = conn.getInputStream();
                reader = new InputStreamReader(stream);
                builder = new StringBuilder();
                int ch = 0;
                while ((ch = reader.read()) != -1) {
                    builder.append((char) ch);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);
            dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(jsonString);

                titleTextView.setText(jsonObject.getString(JSON_OBJECT_ORIGINAL_TITLE));
                overViewTextView.setText(jsonObject.getString(JSON_OBJECT_OVERVIEW));
                ratingBar.setRating((float)jsonObject.getDouble(JSON_OBJECT_VOTE_AVERAGE));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
