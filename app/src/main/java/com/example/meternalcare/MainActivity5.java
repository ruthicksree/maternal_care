package com.example.meternalcare;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

    public class MainActivity5 extends AppCompatActivity {
        ImageView play,prev,next,imageView;
        TextView songTitle;
        SeekBar mSeekBarTime,mSeekBarVolume;
        static MediaPlayer mMediaPlayer;
        private Runnable runnable;
        private AudioManager mAudioManager;
        int currentIndex=0;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main5);
            mAudioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);

            play = findViewById(R.id.play);
            prev = findViewById(R.id.prev);
            next = findViewById(R.id.next);
            songTitle=findViewById(R.id.songTitle);
            imageView=findViewById(R.id.imageView);
            mSeekBarTime = findViewById(R.id.seekBarTime);
            mSeekBarVolume = findViewById(R.id.seekBarVol);
            ArrayList<Integer> songs = new ArrayList<>();

            songs.add(0, R.raw.anjali);
            songs.add(1, R.raw.kesariya);
            songs.add(2, R.raw.krishna);
            songs.add(3, R.raw.vennilave);

            mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));

            int maxV=mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int curV=mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            mSeekBarVolume.setMax(maxV);
            mSeekBarVolume.setProgress(curV);

            mSeekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSeekBarTime.setMax(mMediaPlayer.getDuration());
                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                        play.setImageResource(R.drawable.play);
                    } else {
                        mMediaPlayer.start();
                        play.setImageResource(R.drawable.pause);
                    }
                    SongNames();
                }
            });


            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mMediaPlayer != null) {
                        play.setImageResource(R.drawable.pause);
                    }
                    if (currentIndex < songs.size() - 1) {
                        currentIndex++;
                    } else {
                        currentIndex = 0;
                    }
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                    }
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));
                    mMediaPlayer.start();
                    SongNames();
                }

            });

            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mMediaPlayer != null) {
                        play.setImageResource(R.drawable.pause);
                    }
                    if (currentIndex > 0) {
                        currentIndex--;
                    } else {
                        currentIndex = songs.size() - 1;
                    }
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                    }
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));
                    mMediaPlayer.start();
                    SongNames();

                }
            });
        }
        private void SongNames() {
            if (currentIndex == 0) {
                songTitle.setText("anjali");
                imageView.setImageResource(R.drawable.headphones);
            }
            if (currentIndex == 1) {
                songTitle.setText("kesariya");
                imageView.setImageResource(R.drawable.headphones);
            }
            if (currentIndex == 2) {
                songTitle.setText("krishna");
                imageView.setImageResource(R.drawable.headphones);
            }
            if (currentIndex == 3) {
                songTitle.setText("vennilave");
                imageView.setImageResource(R.drawable.headphones);
            }

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mSeekBarTime.setMax(mMediaPlayer.getDuration());
                    mMediaPlayer.start();
                }
            });

            mSeekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUse) {
                    if (fromUse) {
                        mMediaPlayer.seekTo(progress);
                        mSeekBarTime.setProgress(progress);
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
                    while (mMediaPlayer != null) {
                        try {
                            if (mMediaPlayer.isPlaying()) {
                                Message message = new Message();
                                message.what = mMediaPlayer.getCurrentPosition();
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

        @SuppressLint("Handler Leak")
        Handler handler=new Handler (){
            @Override
            public void handleMessage (Message msg){
                mSeekBarTime.setProgress(msg.what);
            }
        };
    }