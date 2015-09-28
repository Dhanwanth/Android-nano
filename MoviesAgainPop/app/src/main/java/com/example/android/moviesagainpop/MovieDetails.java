package com.example.android.moviesagainpop;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MovieDetails extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Intent intent=getIntent();
        String message = intent.getStringExtra("MovieDetails");
        TextView txtView=(TextView) findViewById(R.id.movie_name_id);
        String str1=message.split(" && ")[2];
        txtView.setText(str1);
        ImageView ImgView=(ImageView) findViewById(R.id.imgMoviePoster);
        ImgView.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(this).load("http://image.tmdb.org/t/p/w185/"+message.split(" && ")[0]).into(ImgView);
        TextView txtView1=(TextView) findViewById(R.id.txtView_Rele);
        txtView1.setText(message.split(" && ")[4].split("-")[0]+"\n"+message.split(" && ")[4]+"\n"+message.split(" && ")[3]);
        TextView txtView2=(TextView) findViewById(R.id.txtView_Desc);
        txtView2.setText(message.split(" && ")[1]);
        //setContentView(R.layout.activity_movie_details);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
