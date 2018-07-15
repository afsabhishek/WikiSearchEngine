package com.android.wikimedia;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    EditText searchText ;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        searchText = findViewById(R.id.searchbar);
        imageView = findViewById(R.id.image);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.blink);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(700);
        imageView.startAnimation(animation);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                searchText.setVisibility(View.VISIBLE);
                searchText.startAnimation(animation);
                searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        searchText.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SplashActivity.this,SearchActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });

            }

        }, 2*1000); // wait for 2 seconds


    }


}
