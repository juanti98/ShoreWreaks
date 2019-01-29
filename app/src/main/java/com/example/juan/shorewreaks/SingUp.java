package com.example.juan.shorewreaks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

    }
}
