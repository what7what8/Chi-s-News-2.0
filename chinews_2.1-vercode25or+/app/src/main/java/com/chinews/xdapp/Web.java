package com.chinews.xdapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class Web extends AppCompatActivity {
    //private static final String TAG = "MyActivity";
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        //int
        Intent intent = this.getIntent();
//取得傳遞過來的資料
        int web = intent.getIntExtra("web", 0);
//webview
        WebView webView = findViewById(R.id.web);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //loadurl
        if (web == 1) {
            webView.loadUrl("https://wa.me/message/DHNY6K6H62XXO1");
        } else if (web == 2) {
            webView.loadUrl("https://docs.google.com/document/d/e/2PACX-1vRDyf_AnEzShTlKqR1NuB3CAmNuVV3gMNn0hbCtLrLeHgFdOMDo7_exZoAP5JgJZmkEDO3bLih_H_cr/pub?embedded=true");
            webSettings.setUseWideViewPort(true);//设置webview推荐使用的窗口
            webSettings.setLoadWithOverviewMode(true);//设置webview加载的页面的模式
            webSettings.setDisplayZoomControls(false);//隐藏webview缩放按钮
            webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
            webSettings.setSupportZoom(true); // 支持缩放
            webSettings.setUseWideViewPort(true);//扩大比例的缩放
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);//自适应屏幕
            webSettings.setLoadWithOverviewMode(true);
        } else if (web == 3) {
            webView.loadUrl("https://docs.google.com/presentation/d/e/2PACX-1vRd64heUt7GTlHMS-zWlrmxXsJXZf04oC5nbFi8l1U-hL-Yu22dCpftkebm4ray3A/embed?start=false&loop=false&delayms=3000");
        } else if (web == 4) {
            webView.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSe0ipmnVon0xmncwsjYbHBbXqpcRhbL7mhDbQi8eSM4Zs2How/viewform?usp=pp_url&entry.2102043168=%E5%90%8C%E6%84%8F");
        } else {
            Toast.makeText(this, R.string.ac, Toast.LENGTH_SHORT).show();
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
