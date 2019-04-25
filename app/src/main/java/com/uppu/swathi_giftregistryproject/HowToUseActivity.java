package com.uppu.swathi_giftregistryproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class HowToUseActivity extends AppCompatActivity {
    //this activity is used to display the video of how to use this app
    private VideoView howToUseVideo;
    //Media controller is a view containing media controls
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use);
        howToUseVideo = (VideoView) findViewById(R.id.howToUseVideo);
        //setting the video path in the raw folder
        String videoPath= "android.resource://" + getPackageName() + "/" +R.raw.howtouse;
        mediaController= new MediaController(this);
        //controls display when user taps on the video
        mediaController.setAnchorView(howToUseVideo);
        howToUseVideo.setMediaController(mediaController);
        //setting the video path
        howToUseVideo.setVideoPath(videoPath);
        howToUseVideo.start();

    }
}
