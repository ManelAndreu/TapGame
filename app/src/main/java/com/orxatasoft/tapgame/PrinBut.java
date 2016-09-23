package com.orxatasoft.tapgame;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * Created by Manel on 15/12/2015.
 */
public class PrinBut {
    ImageView imageView;
    float degrees;
    private double mCurrAngle = 0;
    private double mPrevAngle = 0;
    private static int polsacions;
    boolean canRot, jugant, notTouching;

    public PrinBut(Context context, final ImageView imageView) {
        this.imageView = imageView;
        canRot = false;
        notTouching = true;
        this.imageView.setBackgroundResource(R.drawable.normal);
        jugant = true;
        this.imageView.setOnTouchListener(new View.OnTouchListener() {
                                              @Override
                                              public boolean onTouch(View v, MotionEvent event) {
                                                  if (jugant && notTouching && !canRot) {
                                                      polsacions++;
                                                      Game.pushed.setText(String.valueOf(polsacions));
                                                      notTouching = false;
                                                  }else{
                                                      notTouching = true;
                                                  }

                                                  if (canRot) {
                                                      final float xc = imageView.getWidth() / 2;
                                                      final float yc = imageView.getHeight() / 2;
                                                      final float x = event.getX();
                                                      final float y = event.getY();

                                                      switch (event.getAction()) {
                                                          case MotionEvent.ACTION_DOWN: {
                                                              imageView.clearAnimation();
                                                              mCurrAngle = Math.toDegrees(Math.atan2(x - xc, yc - y));
                                                              break;
                                                          }
                                                          case MotionEvent.ACTION_MOVE: {
                                                              mPrevAngle = mCurrAngle;
                                                              mCurrAngle = Math.toDegrees(Math.atan2(x - xc, yc - y));
                                                              animate(mPrevAngle, mCurrAngle, 0);
                                                              break;
                                                          }
                                                          case MotionEvent.ACTION_UP: {
                                                              mPrevAngle = mCurrAngle = 0;
                                                              break;
                                                          }
                                                      }

                                                  }
                                                  return true;

                                              }

                                          }


        );
    }

    private void animate(double fromDegrees, double toDegrees, long durationMillis) {
        final RotateAnimation rotate = new RotateAnimation((float) fromDegrees, (float) toDegrees,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(durationMillis);
        rotate.setFillEnabled(true);
        rotate.setFillAfter(true);
        imageView.startAnimation(rotate);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public float getDegrees() {
        return degrees;
    }

    public void setDegrees(float degrees) {
        this.degrees = degrees;
    }

    public double getmCurrAngle() {
        return mCurrAngle;
    }

    public void setmCurrAngle(double mCurrAngle) {
        this.mCurrAngle = mCurrAngle;
    }

    public double getmPrevAngle() {
        return mPrevAngle;
    }

    public void setmPrevAngle(double mPrevAngle) {
        this.mPrevAngle = mPrevAngle;
    }

    public static int getPolsacions() {
        return polsacions;
    }

    public static void setPolsacions(int polsacions) {
        PrinBut.polsacions = polsacions;
    }

    public boolean isCanRot() {
        return canRot;
    }

    public void setCanRot(boolean canRot) {
        this.canRot = canRot;
    }

    public boolean isJugant() {
        return jugant;
    }

    public void setJugant(boolean jugant) {
        this.jugant = jugant;
    }

    public boolean isNotTouching() {
        return notTouching;
    }

    public void setNotTouching(boolean notTouching) {
        this.notTouching = notTouching;
    }
}
