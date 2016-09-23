package com.orxatasoft.tapgame;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Manel on 10/12/2015.
 */
public class Song {
    String name, aut;
    int src;
    MediaPlayer mediaPlayer;
    Context context;

    public Song(String aut, String name, int src, Context context) {
        this.context = context;
        this.mediaPlayer = new MediaPlayer();
        this.name = name;
        this.aut = aut;
        this.src = src;
    }

    public void load() {
        mediaPlayer = MediaPlayer.create(context, src);
    }

    public void play(final Context c) {

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.setLooping(true);
                mediaPlayer.setVolume(100, 100);
                mediaPlayer.start();
            }
        });


    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


  public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public String getAut() {
        return aut;
    }

    public void setAut(String aut) {
        this.aut = aut;
    }

    public boolean isPlaying(){
        if(mediaPlayer.isPlaying()){
            return true;
        }else{
            return false;
        }
    }
}
