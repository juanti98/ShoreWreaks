package com.example.juan.shorewreaks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    protected TextView mSignUpTextview;
    protected Button mLoginButton;

    protected ImageView mImageView;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        TextInputLayout password = (TextInputLayout)findViewById(R.id.passwordTIL);
        password.requestFocus();

        TextInputLayout username = (TextInputLayout)findViewById(R.id.usernameTIL);
        username.requestFocus();

        mSignUpTextview = findViewById(R.id.signup_text);

        mSignUpTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SingUp.class);
                startActivity(intent);
            }
        });

        mLoginButton = (Button)findViewById(R.id.loginbutton);
        mLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this, SplashScreen.class);
                startActivity(intent);


            }
        });

    }
}