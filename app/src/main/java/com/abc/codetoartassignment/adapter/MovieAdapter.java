package com.abc.codetoartassignment.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.codetoartassignment.R;
import com.abc.codetoartassignment.activity.DetailsActivity;
import com.abc.codetoartassignment.model.Movie;
import static com.abc.codetoartassignment.utility.Constants.*;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by abc on 13-Feb-17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private ArrayList<Movie> movieList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textViewTitle, textViewReleaseDate,  textViewAdult;
        public ImageView movieImage ;

        ArrayList<Movie> movieArrayList = new ArrayList<>();
        Context context;

        public MyViewHolder(View view,ArrayList<Movie> movieArrayList, Context context) {
            super(view);

            this.movieArrayList = movieArrayList;
            this.context = context;

            view.setOnClickListener(this);

            textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
            movieImage = (ImageView) view.findViewById(R.id.movieImage);
            textViewReleaseDate = (TextView) view.findViewById(R.id.textViewReleaseDate);
            textViewAdult = (TextView) view.findViewById(R.id.textViewAdult);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            //Movie movie = this.movieArrayList.get(position);

            Intent intent = new Intent(this.context, DetailsActivity.class);
            intent.putExtra(JSON_OBJECT_ID,movieArrayList.get(position).getId());
            intent.putExtra(JSON_OBJECT_ORIGINAL_TITLE,movieArrayList.get(position).getOriginal_title());
            this.context.startActivity(intent);

        }
    }
    @Override
    public MovieAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view,movieList,context);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MyViewHolder holder, int position) {

        Movie movie = movieList.get(position);

        if(movieList.get(position).isAdult())
            holder.textViewAdult.setText("A");
        else
            holder.textViewAdult.setText("U/A");

        holder.textViewTitle.setText(movie.getTitle());
        holder.textViewReleaseDate.setText(movie.getRelease_date());

        Picasso.with(context)
                .load(IMAGE_PATH + movieList.get(position).getPoster_path())
                .into(holder.movieImage);
        //new ImageTask(holder.movieImage).execute(IMAGE_PATH + movieList.get(position).getPoster_path());

    }

    /*class ImageTask extends AsyncTask<String, Void, Bitmap>
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
    }*/

    public MovieAdapter(ArrayList<Movie> moviesList, Context context) {
        this.movieList = moviesList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
