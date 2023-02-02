package com.example.smarthomegesturecontrol;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class GestureDisplay extends MainActivity {
    int gestureType;
    VideoView videoView;
    MediaController mediaController;
    Button captureBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gesture_display);
        videoView = findViewById(R.id.videoView);
        mediaController = new MediaController(this);
        Bundle extras = getIntent().getExtras();
        gestureType = extras.getInt("gestureType");
        setGestureVideo(gestureType);

        captureBtn = (Button) findViewById(R.id.button);
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GestureDisplay.this,GestureCapture.class);
                intent.putExtra("gestureType", gestureType);
                startActivity(intent);
            }
        });

    }

    void setGestureVideo(int gestureSelection) {
        switch (gestureSelection) {
            case 1:
               videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.hlighton);
               playVideo(videoView);
               break;
            case 2:
                videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.hlightoff);
                playVideo(videoView);
                break;
            case 3:
                videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.hfanon);
                playVideo(videoView);
                break;
            case 4:
                videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.hfanoff);
                playVideo(videoView);
                break;
            case 5:
                videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.hincreasefanspeed);
                playVideo(videoView);
                break;
            case 6:
                videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.hdecreasefanspeed);
                playVideo(videoView);
                break;
            case 7:
                    videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.hsetthermo);
                playVideo(videoView);
                break;
            case 8:
                videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h0);
                playVideo(videoView);
                break;
            case 9:
                videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h1);
                playVideo(videoView);
                break;
            case 10:
                videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h2);
                playVideo(videoView);
                break;
            case 11:
                videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h3);
                playVideo(videoView);
                break;
            case 12:
                videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h4);
                playVideo(videoView);
                break;
            case 13:
                videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h5);
                playVideo(videoView);
                break;
            case 14:
                videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h6);
                playVideo(videoView);
                break;
            case 15:
                videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h7);
                playVideo(videoView);
                break;
            case 16:
                videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h8);
                playVideo(videoView);
                break;
            case 17:
                videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.h9);
                playVideo(videoView);
                break;
            default:
                break;
        }
    }

    void playVideo(VideoView videoView) {
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
                mediaController.show(900000000);
            }
        });

    }
}
