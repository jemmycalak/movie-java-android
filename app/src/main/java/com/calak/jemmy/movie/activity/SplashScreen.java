package com.calak.jemmy.movie.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.calak.jemmy.movie.R;

public class SplashScreen extends AppCompatActivity {

    private Handler handler;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageView = (ImageView)findViewById(R.id.imageSplash);
        handler = new Handler();
        Glide.with(this).load(R.drawable.loader).into(imageView);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, MovieActivity.class));
            }
        };
        handler.postDelayed(runnable, 5000);
    }
}
