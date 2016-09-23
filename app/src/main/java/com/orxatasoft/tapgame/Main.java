package com.orxatasoft.tapgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class Main extends FragmentActivity {
    Loader l;
    boolean loading;
    ImageView button, splash, audioM, config;
    final int BLUE = 0x7F0000FF;
    final int RED = 0x7FFF0000;
    final int GREEN = 0x7F00FF00;
    final int WHITE = 0x7FFFFFFF;
    final int BLACK = 0x7F000000;
    final int PURPLE = 0x7F610B5E;
    final int ORANGE = 0x7FFF4000;
    final int YELLOW = 0x7FFFFF00;
    final int PINK = 0x7FFF00BF;
    final int GOTH = 0x7F610B5E;
    TextView begin, title, name, aut;
    int n = 0;
    Net net;
    static boolean musicPlaying, withConnection;
    SongList sl;
    ProtoSong s;
    LinearLayout rl;
    Random rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INITIALIZATIONS
        withConnection = true;
        net = new Net();
        rl = (LinearLayout) findViewById(R.id.lila);
        name = (TextView) findViewById(R.id.namSong);
        aut = (TextView) findViewById(R.id.namAut);
        rand = new Random();
        sl = new SongList(getApplicationContext());
        loading = false;
        musicPlaying = true;
        saveBool("music", musicPlaying);
        l = new Loader();
        s = null;

        splash = (ImageView) findViewById(R.id.splashy);
        try {
            if (net.execute("").get()) {
                l.execute("");
            } else {
                initialize();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }


    public void audioManage(ImageView ic) {
        if (musicPlaying) {
            musicPlaying = false;
            s.pause();
            audioM.setBackgroundResource(android.R.drawable.ic_lock_silent_mode);
//            audioM.setImageDrawable(getDrawable(android.R.drawable.ic_lock_silent_mode));
            audioM.setColorFilter(RED, PorterDuff.Mode.SRC_ATOP);
        } else {
            musicPlaying = true;
            show(s.getAut(), s.getName());
            audioM.setBackgroundResource(android.R.drawable.ic_lock_silent_mode_off);
//            audioM.setImageDrawable(getDrawable(android.R.drawable.ic_lock_silent_mode_off));
            audioM.setColorFilter(RED, PorterDuff.Mode.SRC_ATOP);
            playSong();
        }
        saveBool("music", musicPlaying);
    }

    @Override
    protected void onPause() {
        super.onPause();
        s.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (s == null) {
            withConnection = false;
            Toast.makeText(Main.this, "Sense connexió", Toast.LENGTH_SHORT).show();
        }
        if (s != null) {
            withConnection = true;
            if (s != null && s.isPlaying()) {
            } else {
                s.play(getApplicationContext());
            }
        }
    }

    public void playSong() {
        if (musicPlaying) {
            int song = rand.nextInt(sl.getSongs().size());
            s = sl.getSong(song);
            s.load();
            if (s == null) {
                withConnection = false;
                Toast.makeText(Main.this, "Sense connexió", Toast.LENGTH_SHORT).show();
            } else {
                s.play(Main.this);
            }
        }
    }

    public class Net extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            if (isNetworkAvailable()) {
                HttpURLConnection urlc = null;

                try {
                    urlc = (HttpURLConnection) (new URL("http://www.google.com")).openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Conntection", "close");
                urlc.setConnectTimeout(1500);
                try {
                    urlc.connect();

                    return (urlc.getResponseCode() == 200);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
            }
            return false;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null;
    }

    public class Loader extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = true;
            splash();
        }

        @Override
        protected String doInBackground(String... params) {
            playSong();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading = false;
            initialize();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.config_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.music) {
            if (musicPlaying) {
                musicPlaying = false;
                s.pause();
                item.setIcon(android.R.drawable.ic_lock_silent_mode);
            } else {
                musicPlaying = true;
                s.play(getApplicationContext());
                item.setIcon(android.R.drawable.ic_lock_silent_mode_off);
            }
            saveBool("music", musicPlaying);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void saveBool(String key, boolean bool) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor spEdit = sp.edit();
        spEdit.putBoolean(key, bool);
        spEdit.commit();
    }

    public void saveInt(String key, int n) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor spEdit = sp.edit();
        spEdit.putInt(key, n);
        spEdit.commit();
    }

    public void saveString(String key, String string) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor spEdit = sp.edit();
        spEdit.putString(key, string);
        spEdit.commit();
    }

    public void saveStrings(String key, Set<String> strings) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor spEdit = sp.edit();
        spEdit.putStringSet(key, strings);
        spEdit.commit();
    }

    public boolean getSavedBool(String key, boolean bool) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean spBoolean = sp.getBoolean(key, bool);
        return spBoolean;
    }

    public int getSavedInt(String key, int n) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int spInt = sp.getInt(key, n);
        return spInt;
    }

    public String getSavedString(String key, String string) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String spString = sp.getString(key, string);
        return spString;
    }

    public Set<String> getSavedStrings(String key, Set<String> strings) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Set<String> spSetString = sp.getStringSet(key, strings);
        return spSetString;
    }

    public void show(String autor, String nm) {
        Animation init = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.message_init);
        final Animation body = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.message_body);
        final Animation finale = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.message_final);
        name.setText(nm);
        aut.setText(autor);

        init.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                rl.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rl.startAnimation(body);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        body.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rl.startAnimation(finale);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        finale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rl.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        rl.startAnimation(init);
        rl.setVisibility(View.VISIBLE);
    }

    public void initialize() {
        this.splash.setVisibility(View.INVISIBLE);
        if (s != null) {
            show(s.getName(), s.getAut());
        }
        //ANIMATORS
        Animation anim = AnimationUtils.loadAnimation(Main.this, R.anim.first_main_anim);
        final Animation fanim = AnimationUtils.loadAnimation(Main.this, R.anim.final_main_anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                button.startAnimation(fanim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fanim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                button.startAnimation(animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //TITLE
        title = (TextView) findViewById(R.id.title);
        title.setVisibility(View.VISIBLE);

        //RED BUTTON BUTTON (LOL?)
        button = (ImageView) findViewById(R.id.imageView);
        button.setColorFilter(RED, PorterDuff.Mode.SRC_ATOP);
        button.startAnimation(anim);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (n < 9) {
                    n++;
                } else {
                    n = 0;
                }
                switch (n) {
                    case 0:
                        button.setColorFilter(RED, PorterDuff.Mode.SRC_ATOP);
                        break;
                    case 1:
                        button.setColorFilter(GREEN, PorterDuff.Mode.SRC_ATOP);
                        break;
                    case 2:
                        button.setColorFilter(BLUE, PorterDuff.Mode.SRC_ATOP);
                        break;
                    case 3:
                        button.setColorFilter(WHITE, PorterDuff.Mode.SRC_ATOP);
                        break;
                    case 4:
                        button.setColorFilter(BLACK, PorterDuff.Mode.SRC_ATOP);
                        break;
                    case 5:
                        button.setColorFilter(ORANGE, PorterDuff.Mode.SRC_ATOP);
                        break;
                    case 6:
                        button.setColorFilter(PURPLE, PorterDuff.Mode.SRC_ATOP);
                        break;
                    case 7:
                        button.setColorFilter(YELLOW, PorterDuff.Mode.SRC_ATOP);
                        break;
                    case 8:
                        button.setColorFilter(PINK, PorterDuff.Mode.SRC_ATOP);
                        break;
                    case 9:
                        button.setColorFilter(GOTH, PorterDuff.Mode.SRC_ATOP);
                        break;
                }
            }
        });

        //MUSIC BUTTON
        audioM = (ImageView) findViewById(R.id.audio);
        audioM.setColorFilter(RED, PorterDuff.Mode.SRC_ATOP);
        audioM.setVisibility(View.VISIBLE);
        audioM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioManage(audioM);
            }
        });

        //CONFIG BUTTON
        config = (ImageView) findViewById(R.id.config);
        config.setColorFilter(RED, PorterDuff.Mode.SRC_ATOP);
        config.setVisibility(View.VISIBLE);
        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main.this, Config.class);
                startActivity(i);
            }
        });

        //BEGIN BUTTON
        begin = (TextView) findViewById(R.id.tvBegin);
        begin.setText(getString(R.string.begin));
        begin.setVisibility(View.VISIBLE);
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Game.class);
              if(s!=null){  s.pause();}
                startActivity(i);
            }
        });
    }

    public void splash() {

        splash.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (!loading) {
                    splash.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (loading) {
                    splash.startAnimation(animation);
                } else {
                    splash.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if (!loading) {
                    splash.setVisibility(View.INVISIBLE);
                }
            }
        });
        splash.startAnimation(animation);
    }


}
