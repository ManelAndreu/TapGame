package com.orxatasoft.tapgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.Set;

public class Game extends AppCompatActivity {
    boolean loading, withConnection;
    Chronometer vegadesPolsades;
    Loader l;
    ImageView button;
    PrinBut pb;
    ImageView splash;
    LinearLayout rl;
    Random rand;
    boolean jugant, musicActivated;
    SongList sl;
    Net net;
    static TextView pushed;
    TextView gaming, timer, plusTime, tvQu, name, aut;
    int polsacions, time, timerTime;
    Animation animate, animTv;
    final int RED = 0x7FFF0000;
    ProtoSong s;
    Animation init, body, finale, animQu, finaleQu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //INITIALIZATION
        withConnection = true;

        //PRINCIPAL BUTTON
        button = (ImageView) findViewById(R.id.button);

        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jugant) {
                    polsacions++;
                    pushed.setText(String.valueOf(polsacions));
                    recompensa(polsacions);
                }
            }
        });



        //PRINCIPAL BUTTON

        //INITIAL COUNTER
        gaming = (TextView) findViewById(R.id.gaming);
        gaming.setVisibility(View.VISIBLE);


        pb = new PrinBut(getApplicationContext(), button);
        rl = (LinearLayout) findViewById(R.id.lilaG);
        name = (TextView) findViewById(R.id.namSongG);
        aut = (TextView) findViewById(R.id.namAutG);
        polsacions = 0;
        pb.setPolsacions(polsacions);
        loading = false;
        time = 5;
        timerTime = 10;
        jugant = false;
        pb.setJugant(false);
        sl = new SongList(getApplicationContext());
        rand = new Random();
        s = null;
        splash = (ImageView) findViewById(R.id.splashyGame);
        net = new Net();
        l = new Loader();
        if (Main.withConnection) {
            l.execute("");
        } else {
            initialize();
        }


    }

    public void initialize() {
        //ANIMATORS
        if (s != null) {
            show(s.getName(), s.getAut());
        }

        animate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.plus_time_anim);

        //QUESTION TEXTVIEW
        tvQu = (TextView) findViewById(R.id.tvQu);
        tvQu.setVisibility(View.VISIBLE);

        //PUSHED TEXTVIEW
        pushed = (TextView) findViewById(R.id.pushed);
        pushed.setVisibility(View.VISIBLE);
        pushed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recompensa(Integer.parseInt(String.valueOf(s)));
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //COUNTER PUSH
        vegadesPolsades = (Chronometer) findViewById(R.id.tPolsat);
        vegadesPolsades.setVisibility(View.INVISIBLE);
        vegadesPolsades.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                time--;
                if (time > 0) {
                    gaming.setVisibility(View.VISIBLE);
                } else {
                    if (timerTime == 10) {
                        jugant = true;
                        pb.setJugant(true);
                    }
                    gaming.setVisibility(View.INVISIBLE);
                    if (jugant == true) {


                        timer.setText(timerTime + "");
                        timerTime--;
                        if (timerTime == -1) {
                            jugant = false;
                            pb.setJugant(false);
                            Intent i = new Intent(getApplicationContext(), Resultat.class);
                            i.putExtra("aconseguit", polsacions);
                            startActivityForResult(i, 5);
                        }
                    }
                }
                gaming.setText(time + "");

            }
        });
        vegadesPolsades.start();

        //PLUS TIME TEXTVIEW
        plusTime = (TextView) findViewById(R.id.plusTime);
        plusTime.setVisibility(View.INVISIBLE);

        //TIMER TEXTVIEW
        timer = (TextView) findViewById(R.id.timer);
        timer.setVisibility(View.VISIBLE);

        reset();
    }


    public void addTime(int t) {
        timerTime = timerTime + t;
        timer.setText(timerTime + "");
        plusTime.setVisibility(View.VISIBLE);
        plusTime.startAnimation(animate);
        plusTime.setText("+" + (t - 1));

    }

    public void recompensa(int pols) {
        if (0 == pols % 25) {
            addTime(2);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                reset();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("polsacions", polsacions);
        outState.putInt("temps", timerTime);
        outState.putInt("iniTemps", time);
        super.onSaveInstanceState(outState);
    }

    public void show(String autor, String nm) {
        init = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.message_init);
        body = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.message_body);
        finale = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.message_final);
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


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        polsacions = savedInstanceState.getInt("polsacions");
        pushed.setText(polsacions + "");
        timerTime = savedInstanceState.getInt("temps");
        timer.setText(timerTime + "");
        time = savedInstanceState.getInt("iniTemps");
        gaming.setText(time + "");


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (s != null) {
            s.pause();
        }
    }

    public void powerUp() {
    }

    public void reset() {
        if (s != null) {
            show(s.getName(), s.getAut());
        }
        if (tvQu != null) {
            tvQu.setVisibility(View.VISIBLE);
        }
        polsacions = 0;
        pb.setPolsacions(polsacions);
        pushed.setText(String.valueOf(polsacions));
        time = 4;
        timerTime = 10;
        timer.setText(String.valueOf(timerTime));
        jugant = false;
        pb.setJugant(false);
        vegadesPolsades.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                time--;
                if (time > 0) {
                    gaming.setVisibility(View.VISIBLE);
                } else {
                    if (timerTime == 10) {
                        jugant = true;
                        pb.setJugant(true);
                    }
                    gaming.setVisibility(View.INVISIBLE);
                    if (jugant == true) {

                        timer.setText(String.valueOf(timerTime));
                        timerTime--;
                        if (timerTime == -1) {
                            jugant = false;
                            pb.setJugant(false);
                            Intent i = new Intent(getApplicationContext(), Resultat.class);
                            polsacions = pb.getPolsacions();
                            i.putExtra("aconseguit", polsacions);
                            startActivityForResult(i, 1);
                        }
                    }
                }
                gaming.setText(time + "");

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(s!=null){
        if (s.isPlaying()) {
        } else {
            l = new Loader();
            if (Main.withConnection) {
                l.execute("");
            } else {
                initialize();
            }
        }
        }else{
        }
    }

    private class Net extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            if (isNetworkAvailable()) {
                HttpURLConnection urlc = null;

                try {
                    urlc = (HttpURLConnection) (new URL("http://www.google.com")).openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ongame_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.reset_menu) {
            reset();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void playSong() {
        int song = rand.nextInt(sl.getSongs().size());
        if (s == null) {
            s = sl.getSong(song);
        }
//        show(s.getAut(), s.getName());
        s.load();
        if (s == null) {
            withConnection = false;
        } else {
            withConnection = true;
            s.play(Game.this);
        }
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
