package com.chinews.xdapp;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;


public class News extends AppCompatActivity {
    int web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ImageView imageView = findViewById(R.id.imageView14);
        imageView.setOnClickListener(v -> {
            web = 3;
            Intent intent = new Intent(News.this, Web.class);
            intent.putExtra("web", web);
            startActivity(intent);
        });
        TextView textView = findViewById(R.id.textView16);
        textView.setOnClickListener(v -> {
            web = 3;
            Intent intent = new Intent(News.this, Web.class);
            intent.putExtra("web", web);
            startActivity(intent);
        });
        ImageView imageView1 = findViewById(R.id.imageView15);
        imageView1.setOnClickListener(v -> {
            web = 2;
            Intent intent = new Intent(News.this, Web.class);
            intent.putExtra("web", web);
            startActivity(intent);
        });
        TextView textView1 = findViewById(R.id.textView17);
        textView1.setOnClickListener(v -> {
            web = 2;
            Intent intent = new Intent(News.this, Web.class);
            intent.putExtra("web", web);
            startActivity(intent);
        });
        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                // textclock
                TextClock textclock = findViewById(R.id.textclock);
                textclock.setFormat24Hour("yyyy-MM-dd HH:mm:ss");
                textclock.setFormat12Hour("yyyy-MM-dd hh:mm:ssa");
                //random text
                TextView textView = findViewById(R.id.textView18);
                textclock.setGravity(Gravity.TOP | Gravity.END);
                textView.setGravity(Gravity.TOP | Gravity.START);
                today_gold_text();
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                // Code to be executed when an ad request fails.
                TextClock textclock = findViewById(R.id.textclock);
                textclock.setFormat24Hour("yyyy-MM-dd HH:mm:ss");
                textclock.setFormat12Hour("yyyy-MM-dd hh:mm:ssa");
                //random text
                TextView textView = findViewById(R.id.textView18);
                textclock.setGravity(Gravity.BOTTOM | Gravity.END);
                textView.setGravity(Gravity.BOTTOM | Gravity.START);
                today_gold_text();
            }
        });
    }

    private void today_gold_text() {
        int r;
        r = (int) (Math.random() * 6);
        TextView textgold = findViewById(R.id.textView18);
        //sound pool
        //noinspection deprecation
        SoundPool sPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        int music0 = sPool.load(this, R.raw.a, 1);
        int music1 = sPool.load(this, R.raw.b, 1);
        int music2 = sPool.load(this, R.raw.c, 1);
        int music3 = sPool.load(this, R.raw.d, 1);
        switch (r) {
            case 1:
                textgold.setText(R.string.s);
                textgold.setOnClickListener(v ->
                        sPool.play(music1, 1, 1, 0, 0, 1));
                break;
            case 2:
                textgold.setText(R.string.t);
                textgold.setOnClickListener(v ->
                        sPool.play(music3, 1, 1, 0, 0, 1));
                break;
            case 3:
                textgold.setText(R.string.u);
                textgold.setOnClickListener(v ->
                        sPool.play(music0, 1, 1, 0, 0, 1));
                break;
            case 4:
                textgold.setText(R.string.v);
                textgold.setOnClickListener(v -> {
                    Toast.makeText(getApplicationContext(), R.string.w, Toast.LENGTH_SHORT).show();
                    new CountDownTimer(4000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            Toast.makeText(getApplicationContext(), getString(R.string.x) + millisUntilFinished / 1000, Toast.LENGTH_SHORT).show();
                        }

                        public void onFinish() {
                            Toast.makeText(getApplicationContext(), R.string.y, Toast.LENGTH_SHORT).show();
                        }
                    }.start();
                    new CountDownTimer(10000, 1000) {
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            finish();
                            throw new RuntimeException(getString(R.string.z));
                        }
                    }.start();
                });
                break;
            case 5:
                textgold.setText(R.string.aa);
                textgold.setOnClickListener(v ->
                        sPool.play(music2, 1, 1, 0, 0, 1));
                break;
            default:
                textgold.setText(R.string.ab);
                textgold.setOnClickListener(v -> {});
                break;
        }
    }
}
