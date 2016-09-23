package com.orxatasoft.tapgame;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

public class ProtoSong {

    private MediaPlayer mPlayer;
    boolean prepared;
    private Context context;
    private String name;
    private String aut;
    private String link = "http://www.provant.com.es/~greenpoint/TapSongs/";
    private String src;


    public ProtoSong(String aut, String name, String src, MediaPlayer mp, Context context) {
        this.context = context;
        this.name = name;
        this.aut = aut;
        this.src = src;
        this.link = this.link + src;
        this.mPlayer = mp;
        prepared = false;
    }

    public void load() {
        try {
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mPlayer.setDataSource(link);
        } catch (IllegalArgumentException e) {
            Toast.makeText(context, "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(context, "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(context, "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mPlayer.prepare();
        } catch (IllegalStateException e) {
            Toast.makeText(context, "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context, "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
    }


    public class SongGetter extends AsyncTask<String, Void, MediaPlayer> {
        @Override
        protected MediaPlayer doInBackground(String... params) {

            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mPlayer.setDataSource(params[0]);
            } catch (IllegalArgumentException e) {
                Toast.makeText(context, "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (SecurityException e) {
                Toast.makeText(context, "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (IllegalStateException e) {
                Toast.makeText(context, "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mPlayer.prepare();
            } catch (IllegalStateException e) {
                Toast.makeText(context, "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(context, "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            }
            return mPlayer;
        }
    }

    public void play(final Context c) {
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPlayer.setLooping(true);
                mPlayer.setVolume(100, 100);
                mPlayer.start();
            }
        });
    }


    public void pause() {

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void replay(){
        load();
        play(context);

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAut() {
        return aut;
    }

    public void setAut(String aut) {
        this.aut = aut;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public boolean isPrepared() {
        prepared = false;
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                prepared = true;
            }
        });
        return prepared;
    }

    public void setPrepared(boolean prepared) {
        this.prepared = prepared;
    }

    public boolean isPlaying() {
        if (mPlayer!=null) {
            return true;
        } else {
            return false;
        }
    }


}
