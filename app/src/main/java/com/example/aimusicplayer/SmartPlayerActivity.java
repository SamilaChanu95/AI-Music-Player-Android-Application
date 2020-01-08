package com.example.aimusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class SmartPlayerActivity extends AppCompatActivity {

    private RelativeLayout parentRelativeLayout;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private String keeper = "";

    private ImageView pausePlayBtn, nextBtn, previousBtn;
    private TextView songNameTxt;

    private ImageView imageView;
    private RelativeLayout lowerRelativeLayout;
    private Button voiceEnabledBtn;
    private String mode = "ON";

    private MediaPlayer myMediaPlayer;
    private int position;
    private ArrayList<File> mySongs; //For get the list of songs from the MainActivity as Files
    private String mSongName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_player);

        checkVoiceCommandPermission();

        pausePlayBtn = findViewById(R.id.play_pause_btn);
        nextBtn = findViewById(R.id.next_btn);
        previousBtn = findViewById(R.id.previous_btn);
        songNameTxt = findViewById(R.id.songName);
        imageView = findViewById(R.id.logo);
        voiceEnabledBtn = findViewById(R.id.voice_enable_btn);
        lowerRelativeLayout = findViewById(R.id.lower);

        parentRelativeLayout = findViewById(R.id.parentRelativeLayout); //Bind the parentRelativeLayout with Relativelayout in to that parentRelativeLayout variable

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(SmartPlayerActivity.this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());



        imageView.setBackground(R.drawable.logo);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {

                ArrayList<String> matchesFound = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (matchesFound != null)
                {
                    keeper = matchesFound.get(0);
                    Toast.makeText(SmartPlayerActivity.this, "Result = " + keeper, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });


        parentRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) // Decide what to do with the touch
            {
                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        speechRecognizer.startListening(speechRecognizerIntent);
                        keeper = "";
                        break;

                    case MotionEvent.ACTION_UP:
                        speechRecognizer.stopListening();
                        break;
                }

                return false;
            }
        });

        //handle the visibility of the voiceEnabled button with click the button
        voiceEnabledBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mode.equals("ON"))
                {
                    mode = "OFF";
                    voiceEnabledBtn.setText("Voice Enabled Mode - OFF");
                    lowerRelativeLayout.setVisibility(view.VISIBLE);
                }
                else
                {
                    mode = "ON";
                    voiceEnabledBtn.setText("Voice Enabled Mode - ON");
                    lowerRelativeLayout.setVisibility(view.GONE);
                }
            }
        });

    }


    /* Wear OS by Google provides two types of voice actions
        : System-provided ->
        These voice actions are task-based and are built into the Wear platform. You filter for them in the activity that you want to start
        when the voice action is spoken. Examples include "Take a note" or "Set an alarm".

        : App-provided ->
        These voice actions are app-based, and you declare them just like a launcher icon. Users say "Start Your App Name" to use these
        voice actions and an activity that you specify starts.
    */


    //method for get permission for the voice commands
    private void checkVoiceCommandPermission()
    {
        //get the audio and then convert in that in to the text
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //Build version need exceed the version 6 Mashmellow
        {
            //if not grant the permission Record Audio for the SmartPlayerActivity
            if (!(ContextCompat.checkSelfPermission(SmartPlayerActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED))
            {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }

    }
}
