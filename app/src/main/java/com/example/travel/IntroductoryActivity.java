package com.example.travel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class IntroductoryActivity extends AppCompatActivity {

    int SPLASH_TIME_OUT =5100;
    ImageView logo,splashImg;
    TextView tenapp;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(IntroductoryActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            },SPLASH_TIME_OUT);

            logo = findViewById(R.id.logo);
            splashImg = findViewById(R.id.img);
            lottieAnimationView = findViewById(R.id.lottiename);
            tenapp = findViewById(R.id.tenapp);

            splashImg.animate().translationY(-2000).setDuration(1000).setStartDelay(4000);
            logo.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
            tenapp.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
            lottieAnimationView.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
        }

}