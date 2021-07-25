package com.chinews.xdapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class GoodLogin extends AppCompatActivity {

    private String category;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_login);
        TextView textView = findViewById(R.id.textView20);
        TextView textView1 = findViewById(R.id.textView22);
        TextView textView2 = findViewById(R.id.textView21);
        ImageView imageView = findViewById(R.id.imageView17);
        Intent intent = this.getIntent();
        StringBuilder json = new StringBuilder();
        String j = intent.getStringExtra("ajson");
        json.append(j);
        try {
            JSONObject jsonObject = new JSONObject(String.valueOf(json));
            category = jsonObject.getString("category");
            status = jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //if (status.equals("login") && category.equals("test")) {
        //    textView.setText(R.string.ad);
        //    imageView.setVisibility(View.GONE);
        //    textView1.setVisibility(View.GONE);
        //    textView2.setVisibility(View.GONE);
        //}
        imageView.setOnClickListener(v -> {
            try {
                FileOutputStream fileOutputStream = openFileOutput("cache_text", MODE_PRIVATE);
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(fileOutputStream));
                writer.write(json.toString());
                writer.flush();
                writer.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finishAffinity();
        });
    }
}