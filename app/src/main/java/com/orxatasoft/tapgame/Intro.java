package com.orxatasoft.tapgame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Intro extends AppCompatActivity {
    boolean loading;
    ImageView splash;
    static TextView in1, inLoad;
   private static Context context;
    private static Intro intro;
    Animation animTitle, animLoad;
    int n = 0;
    RelativeLayout rl;
    String[] quotes = {"Carregant"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        loading = false;
        splash = (ImageView) findViewById(R.id.splash);
        intro = this;
        context = getApplicationContext();
        in1 = (TextView) findViewById(R.id.intro_title);
        final Intro_Animation iau = new Intro_Animation(in1, 500, 1000, 5000, new String[]{getString(R.string.intro1), getString(R.string.intro2), getString(R.string.intro3), getString(R.string.intro4)}, 1);
        iau.startAnimation();
        rl = (RelativeLayout) findViewById(R.id.rl);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iau.next();
            }
        });
        inLoad = (TextView) findViewById(R.id.intro_loadingTV);
        Intro_Animation ial = new Intro_Animation(inLoad, 300, 2000, 500, new String[]{getString(R.string.advice)}, 0);
        ial.startAnimation();


    }



    public static void goToMainMenu(){
        Intent i = new Intent(context, Main.class);
        intro.startActivity(i);

    }



    public void animatedIntro(final TextView[] text) {

        text[n].setAnimation(animTitle);

        animTitle.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                text[n].setVisibility(View.VISIBLE);
                Toast.makeText(Intro.this, "" + n, Toast.LENGTH_SHORT).show();
                n++;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animation.reset();
                text[n].setAnimation(animation);
                text[n - 1].setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void splash() {
        splash.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!loading) {
                    splash.setVisibility(View.INVISIBLE);
                } else {
                    splash.setAnimation(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        splash.setAnimation(animation);
    }
}
