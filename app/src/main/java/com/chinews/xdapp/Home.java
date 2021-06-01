package com.chinews.xdapp;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Home extends AppCompatActivity {
    private long exitTime = 0;
    private String status;


    //exit
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), R.string.ae, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finishAffinity();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        try {
            FileInputStream fileInputStream = openFileInput("cache_text");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line = bufferedReader.readLine();
            StringBuilder json = new StringBuilder();
            while (line != null) {
                json.append(line);
                line = bufferedReader.readLine();
            }
            try {
                JSONObject jsonObject1 = new JSONObject(String.valueOf(json));
                //username = jsonObject.getString("username");
                //email = jsonObject.getString("email");
                //category = jsonObject.getString("category");
                status = jsonObject1.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            bufferedReader.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (status == null) {
            status = "not login";
        }
        Log.i("Chi's News", status);
        //Button Image
        ImageView imageview = findViewById(R.id.imageView9);
        imageview.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, News.class);
            startActivity(intent);
        });
        ImageView imageview2 = findViewById(R.id.imageView10);
        imageview2.setOnClickListener(v -> {
            if (status.equals("login")) {
                Intent intent = new Intent(Home.this, VipArea.class);
                startActivity(intent);
            } else {
                Intent intent1 = new Intent(Home.this, BuyVip.class);
                startActivity(intent1);
            }
        });
        ImageView imageview3 = findViewById(R.id.imageView11);
        imageview3.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Todaynews.class);
            startActivity(intent);
        });
        ImageView imageviewhelp = findViewById(R.id.imageView12);
        imageviewhelp.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Help.class);
            startActivity(intent);
        });
//Button Background
        ImageView imageview4 = findViewById(R.id.imageView);
        imageview4.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, News.class);
            startActivity(intent);
        });
        ImageView imageview5 = findViewById(R.id.imageView2);
        imageview5.setOnClickListener(v -> {
            if (status.equals("login")) {
                Intent intent = new Intent(Home.this, VipArea.class);
                startActivity(intent);
            } else {
                Intent intent1 = new Intent(Home.this, BuyVip.class);
                startActivity(intent1);
            }
        });
        ImageView imageview6 = findViewById(R.id.imageView7);
        imageview6.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Todaynews.class);
            startActivity(intent);
        });
        ImageView imageview7 = findViewById(R.id.imageView8);
        imageview7.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Help.class);
            startActivity(intent);
        });
        //Textview Button
        TextView textView1 = findViewById(R.id.textView11);
        textView1.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, News.class);
            startActivity(intent);
        });
        TextView textView2 = findViewById(R.id.textView12);
        if (status.equals("login")) textView2.setText(R.string.af);
        textView2.setOnClickListener(v -> {
            if (status.equals("login")) {
                Intent intent = new Intent(Home.this, VipArea.class);
                startActivity(intent);
            } else {
                Intent intent1 = new Intent(Home.this, BuyVip.class);
                startActivity(intent1);
            }
        });
        TextView textView3 = findViewById(R.id.textView13);
        textView3.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Todaynews.class);
            startActivity(intent);
        });
        TextView textView4 = findViewById(R.id.textView14);
        textView4.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Help.class);
            startActivity(intent);
        });
        TextView textView0 = findViewById(R.id.textView28);
        Intent intent = this.getIntent();
        String tt = intent.getStringExtra("tt");
        Log.d("data", tt);
        if (tt.equals("null")) {
            tt = "";
        }
        textView0.setText(tt);
        //Need help text go
        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);

        //Need help text end
        Animation operatingAnim1 = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        LinearInterpolator lin1 = new LinearInterpolator();
        operatingAnim1.setInterpolator(lin1);
        Animation rotate1 = AnimationUtils.loadAnimation(this, R.anim.retrun);

        //Need help text do
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // 在此处添加执行的代码
                imageviewhelp.startAnimation(rotate);
                new CountDownTimer(410, 410) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        imageviewhelp.startAnimation(rotate1);
                    }
                }.start();
                handler.postDelayed(this, 5000);// 5000是延时时长
            }
        };
        handler.postDelayed(runnable, 5000);// 打开定时器，执行操作
//ad
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
        //noinspection //deprecation
        @SuppressWarnings("deprecation") SoundPool sPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
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
                break;
        }
    }
}











