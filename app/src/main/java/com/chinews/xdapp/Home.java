package com.chinews.xdapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    private long exitTime = 0;
    private boolean status;

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
        status = new AccountTool(this).isLogin();
        //Button Image
        ImageView imageview = findViewById(R.id.imageView9);
        imageview.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, News.class);
            startActivity(intent);
        });
        ImageView imageview2 = findViewById(R.id.imageView10);
        imageview2.setOnClickListener(v -> {
            if (status) {
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
            if (status) {
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
        if (status) textView2.setText(R.string.af);
        textView2.setOnClickListener(v -> {
            if (status) {
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
    }
}