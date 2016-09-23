package com.orxatasoft.tapgame;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;

/**
 * Created by Manel on 10/12/2015.
 */
public class SongList {
    Context context;
    ArrayList<ProtoSong> songs;

    public SongList(Context context) {
        this.context = context;
        songs = new ArrayList<ProtoSong>();


        addSong("Friendzoned", "S3RL", "Friendzoned.mp3");
        addSong("Forever", "S3RL", "Forever.mp3");
        addSong("R4V3 BOY","S3RL", "RaveBoy.mp3");
        addSong("Tell me what you want", "S3RL", "TellMeWhatYouWant.mp3");
        addSong("God is a girl", "Groove Coverage", "GodIsAGirl.mp3");
        addSong("Poison", "Tune Up & Groove Coverage", "Poison.mp3" );
        addSong("Pretty rave girl", "S3RL", "PrettyRaveGirl.mp3");
        addSong("Rainbow girl", "S3RL", "RainbowGirl.mp3");
        addSong("Feel the melody", "S3RL", "FeelTheMelody.mp3");
        addSong("MTC", "S3RL", "MTC.mp3");
        addSong("Space Invader", "S3RL", "SpaceInvader.mp3");
        addSong("Nightcore This", "S3RL", "Nightcore.mp3");
        addSong("Request", "S3RL", "Request.mp3");
        addSong("Mr Vain", "S3RL", "Vain.mp3");
        addSong("Candy", "S3RL", "Candy.mp3");
        addSong("Dragon Roost Island", "Will & Tim", "DRI.mp3");


    }

    public ProtoSong getSong(int id){
        return songs.get(id);
    }

    public void addSong(String nom, String aut, String src){
        songs.add(new ProtoSong(nom, aut, src, new MediaPlayer(), context));
    }



    public ArrayList<ProtoSong> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<ProtoSong> songs) {
        this.songs = songs;
    }
}
