package com.example.juan.shorewreaks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

public class SingUp extends AppCompatActivity {
    protected Button mCancelButton;
    protected Button mSingupButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        getSupportActionBar().hide();

//        cancel button
        mCancelButton = (Button)findViewById(R.id.cancelbutton);
        mSingupButton = (Button)findViewById(R.id.signupbutton);
        mCancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });

        if (AccessToken.getCurrentAccessToken() == null) {
            goLoginScreen();


        }




    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, SingUp.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void logout(View view){
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }

}
