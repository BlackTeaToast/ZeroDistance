package com.yoyoyee.zerodistance.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yoyoyee.zerodistance.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by PatrickC on 2016/4/13.
 */
public class ImageViewAdapter extends RecyclerView.Adapter <ImageViewAdapter.ViewHolder>{
    private ArrayList<String> imageURL;

    public ImageViewAdapter(ArrayList<String> imageURL){
        this.imageURL = imageURL;
    }
    //確定每個ViewHolder中 有哪些元素 並且findViewById
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView urlImage;



        public ViewHolder(View itemView){
            super(itemView);

            urlImage = (ImageView) itemView.findViewById(R.id.imageViewForAdapter);

        }

    }

    public int getItemCount(){
        return imageURL.size();
    }

    @Override
    //把每一個進來的資料依XML版面產生一個ViewHolder
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_imageview, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    //將產生的ViewHolder ImageView 放入圖片
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.urlImage.setVisibility(View.VISIBLE);
        //取自http://dean-android.blogspot.tw/2013/06/androidimageviewconverting-image-url-to.html
        //建立一個AsyncTask執行緒進行圖片讀取動作，並帶入圖片連結網址路徑
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                String url = params[0];
                return getBitmapFromURL(url);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                holder.urlImage.setImageBitmap(result);
                super.onPostExecute(result);
            }
        }.execute(imageURL.get(position));
    }

    //讀取網路圖片，型態為Bitmap
    //取自http://dean-android.blogspot.tw/2013/06/androidimageviewconverting-image-url-to.html
    private Bitmap getBitmapFromURL(String imageUrl)
    {
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
