package com.chinews.xdapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class BuyVip extends AppCompatActivity {
    private final int REQUEST_CODE_SCAN = 10;
    int web;
    private String TEXT = "aaa";

    private static byte[] DecryptAES(byte[] iv, byte[] key, byte[] text) {
        try {
            AlgorithmParameterSpec mAlgorithmParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec mSecretKeySpec = new SecretKeySpec(key, "AES");
            Cipher mCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            mCipher.init(Cipher.DECRYPT_MODE,
                    mSecretKeySpec,
                    mAlgorithmParameterSpec);

            return mCipher.doFinal(text);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);

        ImageView imageView = findViewById(R.id.imageView22);
        TextView textView = findViewById(R.id.textView25);
        ImageView imageView1 = findViewById(R.id.imageView23);
        TextView textView1 = findViewById(R.id.textView24);
        TextView textView2 = findViewById(R.id.textView23);
        ImageView imageView2 = findViewById(R.id.imageView18);
        imageView.setOnClickListener(v -> {
            web = 4;
            Intent intent = new Intent(BuyVip.this, Web.class);
            intent.putExtra("web", web);
            startActivity(intent);

        });
        textView.setOnClickListener(v -> {
            web = 4;
            Intent intent = new Intent(BuyVip.this, Web.class);
            intent.putExtra("web", web);
            startActivity(intent);

        });
        imageView2.setOnClickListener(v -> {
            Intent intent = new Intent(BuyVip.this, Login.class);
            startActivity(intent);

        });
        textView2.setOnClickListener(v -> {
            Intent intent = new Intent(BuyVip.this, Login.class);
            startActivity(intent);

        });

        imageView1.setOnClickListener(v -> {
            //使用兼容库就无需判断系统版本
            int hasWriteStoragePermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED) {
                int hasCStoragePermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA);
                if (hasCStoragePermission == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(BuyVip.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                } else {
                    //没有权限，向用户请求权限
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 11);
                }
            } else {
                //没有权限，向用户请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        });
        textView1.setOnClickListener(v -> {
            //使用兼容库就无需判断系统版本
            int hasWriteStoragePermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED) {
                int hasCStoragePermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA);
                if (hasCStoragePermission == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(BuyVip.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                } else {
                    //没有权限，向用户请求权限
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 11);
                }
            } else {
                //没有权限，向用户请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                String IvAES = "u9ncRYMbGuSEp427";
                String KeyAES = "vNagtWc4h4gTfTAxv92WGSKq9fMU3tqQ";
                try {
                    byte[] TextByte;
                    TextByte = DecryptAES(IvAES.getBytes(StandardCharsets.UTF_8), KeyAES.getBytes(StandardCharsets.UTF_8), Base64.decode(content.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT));
                    TEXT = new String(TextByte, StandardCharsets.UTF_8);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject jsonObject = new JSONObject(TEXT);
                    String username = jsonObject.getString("username");
                    String email = jsonObject.getString("email");
                    String category = jsonObject.getString("category");
                    String status = jsonObject.getString("status");

                    try {
                        FileOutputStream fileOutputStream = openFileOutput("cache_text", MODE_PRIVATE);
                        PrintWriter writer = new PrintWriter(new OutputStreamWriter(fileOutputStream));
                        writer.write(TEXT);
                        writer.flush();
                        writer.close();
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent();
                    intent.putExtra("ajson", TEXT);
                    intent.setClass(this, GoodLogin.class);
                    //Bundle bundle = new Bundle();
                    //bundle.putDouble("age",age );//傳遞Double
                    //bundle.putString("name",name);//傳遞String
                    // 將Bundle物件傳給intent
                    //intent.putExtras(bundle);
                    //切換Activity
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, R.string.aj, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //通过requestCode来识别是否同一个请求
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                //用户不同意，向用户展示该权限作用
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder(this)
                            .setMessage(R.string.ak)
                            .setPositiveButton("OK", (dialog1, which) ->
                                    ActivityCompat.requestPermissions(this,
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1))
                            .setNegativeButton("Cancel", null)
                            .create()
                            .show();
                }
            }
        }
        if (requestCode == 11) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                //用户不同意，向用户展示该权限作用
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    new AlertDialog.Builder(this)
                            .setMessage(R.string.ak)
                            .setPositiveButton("OK", (dialog1, which) ->
                                    ActivityCompat.requestPermissions(this,
                                            new String[]{Manifest.permission.CAMERA}, 11))
                            .setNegativeButton("Cancel", null)
                            .create()
                            .show();
                }
            }
        }
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
