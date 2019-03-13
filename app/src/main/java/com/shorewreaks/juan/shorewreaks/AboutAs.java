package com.shorewreaks.juan.shorewreaks;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import androidx.annotation.RequiresApi;

public class AboutAs extends AppCompatActivity implements AIListener {


    private AIService mAIService;
    private TextToSpeech mTextToSpeech;
    private Context context;
    private TextView tv_desarrollador1, tv_desarrollador2, tv_desarrollador3, tv_desarrollador4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_as);
        context = this;

        final AIConfiguration config = new AIConfiguration("499ca68207fa404a94eed99ecdd26d17",
                AIConfiguration.SupportedLanguages.Spanish,
                AIConfiguration.RecognitionEngine.System);

        tv_desarrollador1 = findViewById(R.id.tv_desarrollador1);
        tv_desarrollador2 = findViewById(R.id.tv_desarrollador2);
        tv_desarrollador3 = findViewById(R.id.tv_desarrollador3);
        tv_desarrollador4 = findViewById(R.id.tv_desarrollador4);

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



    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
}
