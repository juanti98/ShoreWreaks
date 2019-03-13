package com.shorewreaks.juan.shorewreaks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;


public class AboutAs extends AppCompatActivity implements AIListener, NavigationView.OnNavigationItemSelectedListener {


    private AIService mAIService;
    private TextToSpeech mTextToSpeech;
    private Context context;
    private TextView tv_desarrollador1, tv_desarrollador2, tv_desarrollador3, tv_desarrollador4;
    private TextView tvNombreUser, tvEmail;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_as);
        context = this;

        cambioVistaUser();

        tv_desarrollador1 = findViewById(R.id.tv_desarrollador1);
        tv_desarrollador2 = findViewById(R.id.tv_desarrollador2);
        tv_desarrollador3 = findViewById(R.id.tv_desarrollador3);
        tv_desarrollador4 = findViewById(R.id.tv_desarrollador4);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //DialogFlow

        final AIConfiguration config = new AIConfiguration("499ca68207fa404a94eed99ecdd26d17",
                AIConfiguration.SupportedLanguages.Spanish,
                AIConfiguration.RecognitionEngine.System);

        mAIService = AIService.getService(this, config);
        mAIService.setListener(this);
        mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });


        findViewById(R.id.imgb_lechu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAIService.startListening();
            }
        });

       /* mAIService = AIService.getService(this, config);
        mAIService.setListener(this);
        mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });

        findViewById(R.id.imgb_lechu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAIService.startListening();
            }
        });
*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_about);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void cambioVistaUser() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        tvNombreUser = (TextView) headerView.findViewById(R.id.tv_nombreUser);
        tvEmail = (TextView) headerView.findViewById(R.id.tv_email_header);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_about);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }



    @SuppressLint("NewApi")
    @Override
    public void onResult(AIResponse result) {

        Result response = result.getResult();

        mTextToSpeech.speak(response.getFulfillment().getSpeech(), TextToSpeech.QUEUE_FLUSH, null, null);

    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(context, MainScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_perfil) {
            Intent intent = new Intent(context, PerfilUser.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_playas) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.logOut) {
            mAuth.signOut();
            Intent intent = new Intent(AboutAs.this, LoginScreen.class);
            startActivity(intent);
        }

        //  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //  drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
