package com.chinews.xdapp;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;

public class MainActivity extends AppCompatActivity {
    private long exitTime = 0;

    private void createNotificationChannel(CharSequence channel_name, String channel_description, String CHANNEL_ID) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channel_name, importance);
            channel.setDescription(channel_description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //exit
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), R.string.ae, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(/*context=*/ this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());
        setContentView(R.layout.activity_logon);
        ImageView imageview = findViewById(R.id.imageView5);
        String username = new AccountTool(this).getUserName();
        boolean status = new AccountTool(this).isLogin();
        createNotificationChannel("應用程式更新通知", "用於在程式有可用更新時通知您", "update");
        createNotificationChannel("志報更新通知", "用於在志報有更新時通知您", "news");
        createNotificationChannel("其他通知", "其他的通知", "other");
        createNotificationChannel("客服聊天通知", "用於在需要幫助頁面中，向客服查詢問題或聊天時收到訊息的通知聲", "chat");
        createNotificationChannel("前景服務通知", "死人爛鬼系統逼我send通知", "foreground");
        createNotificationChannel("會員消息通知", "在開通會員服務後，有新的會員消息時通知", "vipmsg");
        startService(new Intent(getBaseContext(), MessageNotification.class));
        TextView textView = findViewById(R.id.textView36);
        TextView textView1 = findViewById(R.id.textView2);
        //textView1.setVisibility(View.VISIBLE
            textView1.setVisibility(View.VISIBLE);
            imageview.setVisibility(View.VISIBLE);
            textView.setText(getString(R.string.h));
            if (status) {
                textView.setText(getString(R.string.ai) + username);
                textView1.setVisibility(View.VISIBLE);
                imageview.setVisibility(View.VISIBLE);
            }
        imageview.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CheckJson.class);
            intent.putExtra("json", 3);
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
                if (!getResources().getConfiguration().locale.getLanguage().startsWith("en")) {
                    today_gold_text();
                } else {
                    textclock.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                }
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
                if (!getResources().getConfiguration().locale.getLanguage().startsWith("en")) {
                    today_gold_text();
                } else {
                    textclock.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                }
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
                textgold.setOnClickListener(v -> {
                });
                break;
        }
    }

}
