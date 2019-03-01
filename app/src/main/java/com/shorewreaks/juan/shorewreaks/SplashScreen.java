package com.shorewreaks.juan.shorewreaks;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    AnimationDrawable anim;

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
        LinearLayout container = (LinearLayout) findViewById(R.id.container);

        anim = (AnimationDrawable)container.getBackground();
        anim.setEnterFadeDuration(1000);
        anim.setExitFadeDuration(1000);

        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.anim_top_in);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in);

        myImage.startAnimation(bounce);
        myTitle.startAnimation(fadeIn);
        mySubtitle.startAnimation(fadeIn);
        openApp(true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (anim != null && !anim.isRunning())
            anim.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (anim != null && anim.isRunning())
            anim.stop();
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
