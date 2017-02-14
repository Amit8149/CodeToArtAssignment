package com.abc.codetoartassignment.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.abc.codetoartassignment.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static com.abc.codetoartassignment.utility.Constants.IMAGE_PATH;

/**
 * Created by abc on 14-Feb-17.
 */

public class SidlerAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> imageList;

    public SidlerAdapter(Context context,ArrayList<String>imageList){
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.swipe_layout, container, false);
        ImageView sliderImageView = (ImageView) layout.findViewById(R.id.sliderImageView);
        new FetchImage(sliderImageView).execute(IMAGE_PATH + imageList.get(position));
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        try {
            //  container.removeView((LinearLayout) object);
            super.destroyItem(container, position, object);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private class FetchImage extends AsyncTask<String, Void, Bitmap>{

        ImageView imageView;

        public FetchImage(ImageView sliderImageView) {
            this.imageView = sliderImageView;
        }
        @Override
        protected Bitmap doInBackground(String... params) {

            String strUrl = params[0];

            try
            {
                URL url = new URL(strUrl);
                URLConnection urlConnection = url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            imageView.setImageBitmap(bitmap);

        }
    }
}
