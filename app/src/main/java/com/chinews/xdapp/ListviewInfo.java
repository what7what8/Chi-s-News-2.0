package com.chinews.xdapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListviewInfo extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_info);
        ArrayList<ArrayList<String>> content = CheckJson.content;
        Intent intent = this.getIntent();
        int pos = intent.getIntExtra("pos", 0);
        TextView title = findViewById(R.id.textView26);
        TextView info = findViewById(R.id.textView27);
        //webview
        WebView webView = findViewById(R.id.listweb);
        webView.getSettings().setJavaScriptEnabled(true);
        title.setText(content.get(pos).get(0));
        info.setText(content.get(pos).get(1));
        if (!content.get(pos).get(2).equals("null")) {
            webView.loadUrl(content.get(pos).get(2));
        } else {
            info.setTextSize(2, 30);
            webView.setVisibility(View.GONE);
        }
        //setWebViewClient
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url == null) return false;

                try {
                    if (url.startsWith("whatsapp://")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) {//防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }
}