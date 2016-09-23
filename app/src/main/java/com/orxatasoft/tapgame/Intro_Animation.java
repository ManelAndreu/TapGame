package com.orxatasoft.tapgame;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

/**
 * Created by Manel on 09/12/2015.
 */
public class Intro_Animation {
    private static final int INTRO = 1;
    private static final int CREATED_BY = 2;
    private int id;
    private int n = 0;
    private int y = 0;
    TextView blobText;
    boolean pushed;
    public String[] text = new String[]{""};
    public int position = 0;
    Animation fadeiInAnimationObject = new AlphaAnimation(0f, 1f);
    Animation textDisplayAnimationObject;
    Animation delayBetweenAnimations;
    Animation fadeOutAnimationObject;
    int fadeEffectDuration;
    int delayDuration;
    int displayFor;

    public Intro_Animation(TextView textV, String[] textList, int id) {
        this(textV, 700, 1000, 2000, textList, id);
    }

    public Intro_Animation(TextView textView, int fadeEffectDuration, int delayDuration, int displayLength, String[] textList, int id) {
        this.id = id;
        pushed = false;
        blobText = textView;
        text = textList;
        this.fadeEffectDuration = fadeEffectDuration;
        this.delayDuration = delayDuration;
        this.displayFor = displayLength;
        InnitializeAnimation();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void startAnimation() {

        blobText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        blobText.startAnimation(fadeiInAnimationObject);
    }

    public void next() {
       Intro.goToMainMenu();

    }

    private void InnitializeAnimation() {
        fadeiInAnimationObject = new AlphaAnimation(0f, 1f);
        fadeiInAnimationObject.setDuration(fadeEffectDuration);
        textDisplayAnimationObject = new AlphaAnimation(1f, 1f);
        textDisplayAnimationObject.setDuration(displayFor);
        delayBetweenAnimations = new AlphaAnimation(0f, 0f);
        delayBetweenAnimations.setDuration(delayDuration);
        fadeOutAnimationObject = new AlphaAnimation(1f, 0f);
        fadeOutAnimationObject.setDuration(fadeEffectDuration);
        fadeiInAnimationObject.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(position<text.length) {
                    blobText.setText(text[position]);
                }
                position++;
                if (position > text.length) {
                    if(id == INTRO){
                        position = 0;
                        Intro.goToMainMenu();
                    }
                }



            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                blobText.startAnimation(textDisplayAnimationObject);
            }
        });
        textDisplayAnimationObject.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                blobText.startAnimation(fadeOutAnimationObject);
            }
        });
        fadeOutAnimationObject.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                blobText.startAnimation(delayBetweenAnimations);
            }
        });
        delayBetweenAnimations.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                blobText.startAnimation(fadeiInAnimationObject);
            }
        });
    }

    public void stopAnimation() {
        blobText.setAnimation(null);
    }

}
