package com.example.juan.shorewreaks;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        TextView myTitle = (TextView)findViewById(R.id.textView);
        TextView mySubtitle = (TextView)findViewById(R.id.textView2);
        ImageView myImage = (ImageView)findViewById(R.id.imageView);

//        // sets a Pretty Custom Font
//        Typeface myFont = Typeface.createFromAsset(getAssets(), "bromello.otf");
//        myTitle.setTypeface(myFont);

        Typeface typeface = getResources().getFont(R.font.merriweather);
        myTitle.setTypeface(typeface);
        mySubtitle.setTypeface(typeface);

        //implements and starts animation
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.anim_top_in);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in);

        myImage.startAnimation(bounce);
        myTitle.startAnimation(fadeIn);
        mySubtitle.startAnimation(fadeIn);
        openApp(true);
    }


    private void openApp(boolean locationPermission) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen
                        .this, LoginScreen.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }
}
