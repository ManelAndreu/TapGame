package com.orxatasoft.tapgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Resultat extends AppCompatActivity {
    TextView polsacions, record, back, best, share;
    ImageView whatsApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat);
        polsacions = (TextView) findViewById(R.id.polsacionsFinal);
        record = (TextView) findViewById(R.id.record);
        back = (TextView) findViewById(R.id.back);
        best = (TextView) findViewById(R.id.best);
        share = (TextView) findViewById(R.id.share);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        int recordFinsAra = preferences.getInt("record", 0);
        final int numActual = getIntent().getIntExtra("aconseguit", 0);


        whatsApp = (ImageView) findViewById(R.id.whatsApp);
        whatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.whats_1) + " " + numActual + getString(R.string.whats_2) + " mapean_p95@live.com.");
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
            }
        });


        SharedPreferences.Editor editor = preferences.edit();
        if (numActual > recordFinsAra) {
            best.setText(numActual + "");
            record.setVisibility(View.VISIBLE);
            editor.putInt("record", numActual);
            editor.commit();
        } else {
            best.setText(recordFinsAra + "");
            record.setVisibility(View.INVISIBLE);
        }

        polsacions.setText(numActual + "");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
