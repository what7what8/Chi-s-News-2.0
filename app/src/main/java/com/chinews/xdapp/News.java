package com.chinews.xdapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


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
