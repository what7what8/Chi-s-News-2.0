package com.chinews.xdapp;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class News extends AppCompatActivity {
    int web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ImageView imageView = findViewById(R.id.imageView14);
        imageView.setOnClickListener(v -> {
            web = 3;
            Intent intent = new Intent(News.this, OldNews.class);
            intent.putExtra("web", web);
            startActivity(intent);
        });
        TextView textView = findViewById(R.id.textView16);
        textView.setOnClickListener(v -> {
            web = 3;
            Intent intent = new Intent(News.this, OldNews.class);
            intent.putExtra("web", web);
            startActivity(intent);
        });
        ImageView imageView1 = findViewById(R.id.imageView15);
        imageView1.setOnClickListener(v -> {
            web = 2;
            Intent intent = new Intent(News.this, LastNews.class);
            intent.putExtra("web", web);
            startActivity(intent);
        });
        TextView textView1 = findViewById(R.id.textView17);
        textView1.setOnClickListener(v -> {
            web = 2;
            Intent intent = new Intent(News.this, LastNews.class);
            intent.putExtra("web", web);
            startActivity(intent);
        });
    }
}
