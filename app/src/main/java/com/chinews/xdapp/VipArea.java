package com.chinews.xdapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import static com.chinews.xdapp.R.id.imageView22;
import static com.chinews.xdapp.R.id.imageView23;
import static com.chinews.xdapp.R.id.imageViewaa;
import static com.chinews.xdapp.R.id.textView24;
import static com.chinews.xdapp.R.id.textView25;
import static com.chinews.xdapp.R.id.textViewaa;

public class VipArea extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VipArea.this, CheckJson.class);
        intent.putExtra("json", 3);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vvip);

        TextView textView = findViewById(textView24);
        ImageView imageView = findViewById(imageView23);
        TextView news = findViewById(textViewaa);
        ImageView but = findViewById(imageViewaa);
        imageView.setOnClickListener(v -> {
            try {
                FileOutputStream fileOutputStream = openFileOutput("cache_text", MODE_PRIVATE);
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(fileOutputStream));
                writer.write("logout out now");
                writer.flush();
                writer.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finishAffinity();
        });
        textView.setOnClickListener(v -> {
            try {
                FileOutputStream fileOutputStream = openFileOutput("cache_text", MODE_PRIVATE);
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(fileOutputStream));
                writer.write("logout out now");
                writer.flush();
                writer.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finishAffinity();
        });
        TextView textView1 = findViewById(textView25);
        ImageView imageView1 = findViewById(imageView22);

        imageView1.setOnClickListener(v -> {
            Intent intent = new Intent(VipArea.this, CheckJson.class);
            intent.putExtra("json", 2);
            startActivity(intent);
        });
        textView1.setOnClickListener(v -> {
            Intent intent = new Intent(VipArea.this, CheckJson.class);
            intent.putExtra("json", 2);
            startActivity(intent);
        });
        news.setOnClickListener(v -> startActivity(new Intent(VipArea.this, VipNews.class)));
        but.setOnClickListener(v -> startActivity(new Intent(VipArea.this, VipNews.class)));
    }
}