package com.abc.codetoartassignment.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abc.codetoartassignment.R;
import com.abc.codetoartassignment.model.Movie;
import com.abc.codetoartassignment.utility.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by abc on 13-Feb-17.
 */

public class MovieAdapter_ListView extends ArrayAdapter{

    Context context;
    ArrayList<Movie> movieList = new ArrayList<>();

    public MovieAdapter_ListView(Context context,ArrayList<Movie> movieList) {
        super(context,0);
        this.movieList = movieList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout layout ;
        TextView textViewTitle ;
        ImageView movieImage ;
        TextView textViewReleaseDate;
        TextView textViewAdult ;

        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflater.inflate(R.layout.movie_list, null);
        }
        else
        {
            layout=(LinearLayout)convertView;
        }


        movieImage = (ImageView) layout.findViewById(R.id.movieImage);
        textViewTitle = (TextView) layout.findViewById(R.id.textViewTitle);
        //ImageView play = (ImageView) layout.findViewById(R.id.play);
        textViewReleaseDate = (TextView) layout.findViewById(R.id.textViewReleaseDate);
        textViewAdult = (TextView) layout.findViewById(R.id.textViewAdult);

        if(movieList.get(position).isAdult())
            textViewAdult.setText("A");
        else
            textViewAdult.setText("U/A");

        textViewReleaseDate.setText(movieList.get(position).getRelease_date());
        textViewTitle.setText(movieList.get(position).getTitle());

        new ImageTask(movieImage).execute(Constants.IMAGE_PATH + movieList.get(position).getPoster_path());
        return layout;
    }

    class ImageTask extends AsyncTask<String, Void, Bitmap>
    {
        ImageView imageView;
        public ImageTask(ImageView imageView){
            this.imageView = imageView;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            String strUrl = params[0];
            try {

                java.net.URL url = new URL(strUrl);
                URLConnection conn = url.openConnection();
                InputStream stream = conn.getInputStream();
                Bitmap mp= BitmapFactory.decodeStream(stream);

                return mp;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap s) {
            super.onPostExecute(s);
            imageView.setImageBitmap(s);
        }
    }

}
