package com.example.mediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity {
    Bundle songExtraData;
    ArrayList<File> songFileList;
    SeekBar seekbar;
    TextView songtitle;
    ImageView playbtn;
    ImageView nextbtn;
    ImageView prevbtn;
    TextView currentTime;
    TextView totalTime;
    static MediaPlayer mediaPlayer;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        prevbtn = findViewById(R.id.prevBtn);
        playbtn = findViewById(R.id.playBtn);
        nextbtn = findViewById(R.id.nextBtn);
        songtitle = findViewById(R.id.SongTitle);
        seekbar = findViewById(R.id.seekBar);
        currentTime = findViewById(R.id.currentTime);
        totalTime = findViewById(R.id.totalTime);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        Intent songData = getIntent();
        songExtraData = songData.getExtras();

        songFileList = (ArrayList) songExtraData.getParcelableArrayList("songList");
        position = songExtraData.getInt("position", 0);

        initMusicPlayer(position);
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position < songFileList.size() - 1) {
                    position++;
                } else {
                    position = 0;
                }
                initMusicPlayer(position);
            }

        });
        prevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position > 0) {
                    position--;
                } else {
                    position = songFileList.size() - 1;
                }
                initMusicPlayer(position);
            }

        });


    }

    @SuppressLint("HandlerLeak")
    private void initMusicPlayer(int position) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
        }
        String name = songFileList.get(position).getName();
        songtitle.setText(name);

        Uri songResourceUri = Uri.parse(songFileList.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), songResourceUri);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                seekbar.setMax(mediaPlayer.getDuration());
                String totTime = createTimerLabel(mediaPlayer.getDuration());
                totalTime.setText(totTime);
                mediaPlayer.start();
                playbtn.setImageResource(R.drawable.pause_btn);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
              //  playbtn.setImageResource(R.drawable.play_btn);
                int currentSongPosition = position;
                if (currentSongPosition < songFileList.size() - 1) {
                    currentSongPosition ++;
                } else {
                    currentSongPosition  = 0;
                }
                initMusicPlayer(currentSongPosition );

            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progess, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progess);
                    seekBar.setProgress(progess);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    try {
                        if (mediaPlayer.isPlaying()) {
                            Message message = new Message();
                            message.what = mediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){

    @Override
    public void handleMessage(Message msg) {

        currentTime.setText(createTimerLabel(msg.what));
        seekbar.setProgress(msg.what);
    }


};
    private void play(){
        if(mediaPlayer !=null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            playbtn.setImageResource(R.drawable.play_btn);
        }
        else
        {
            mediaPlayer.start();
            playbtn.setImageResource(R.drawable.pause_btn);
        }
    }
    public String createTimerLabel(int duration){
       String timerLabel="";
       int min = duration/ 1000 /60;
       int sec = duration/1000 % 60;

       timerLabel +=min+ ":";
       if(sec<10) timerLabel+="0";
       timerLabel+=sec;
       return timerLabel;
    }
}