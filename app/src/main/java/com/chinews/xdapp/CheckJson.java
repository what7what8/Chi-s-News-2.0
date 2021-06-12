package com.chinews.xdapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

import static java.lang.String.valueOf;

public class CheckJson extends AppCompatActivity {

    public static ArrayList<ArrayList<String>> content;
    private final ArrayList<ArrayList<String>> content_not_final = new ArrayList<>();
    String catchData;
    String catchWeather;
    String catchWeatherflw;
    int ramn = 0;
    private String rami;
    private String mnft;
    private String humd;
    private String updt;
    private String gesi;
    private String tcif;
    private String fdwa;
    private String fope;
    private String fode;
    private String oulo;
    private String fupt;
    private int icon;
    private int temp;
    private int ramx;
    //private int weahter = 0;
    private String catchTitle;
    private ProgressDialog dialogweather;
    private ProgressDialog dialogflw;
    private String catchSwt;
    private ProgressDialog dialogSwt;
    private StringBuilder desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load);

        Intent intent1 = this.getIntent();
//取得傳遞過來的資料
        int gojson = intent1.getIntExtra("json", 0);
        if (gojson == 2) catchData();
        else if (gojson == 1) {
            catchWeather();
            dialogweather.setOnDismissListener(dialog -> {
                catchWeatherflw();
                dialogflw.setOnDismissListener(dialog1 -> {
                    catchSwt();
                    dialogSwt.setOnDismissListener(dialog2 -> {
                        Bundle bundle = new Bundle();
                        bundle.putString("gesi", gesi);
                        bundle.putString("tcif", tcif);
                        bundle.putString("fdwa", fdwa);
                        bundle.putString("fope", fope);
                        bundle.putString("fode", fode);
                        bundle.putString("oulo", oulo);
                        bundle.putString("fupt", fupt);
                        bundle.putInt("ramn", ramn);
                        bundle.putInt("ramx", ramx);
                        bundle.putString("rami", rami);
                        bundle.putInt("icon", icon);
                        bundle.putInt("temp", temp);
                        bundle.putString("mnft", mnft);
                        bundle.putString("humd", humd);
                        bundle.putString("updt", updt);
                        bundle.putString("desc", String.valueOf(desc));
                        Intent intent3 = new Intent();
                        intent3.setClass(CheckJson.this, Warnsum.class);
                        intent3.putExtras(bundle);
                        startActivity(intent3);
                    });
                });
            });
        } else if (gojson == 3) catchTitle();
        else {
            Toast.makeText(this, R.string.ac, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //Bundle bundle = getIntent().getExtras();
    //int checkjson = bundle.getInt("checkjson");
    private void catchSwt() {
        catchSwt = "https://data.weather.gov.hk/weatherAPI/opendata/weather.php?dataType=swt&lang=tc";
        dialogSwt = ProgressDialog.show(this, "讀取中(1/4)"
                , "請稍候", true);
        Window window = dialogSwt.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.0f;// 透明度
        lp.dimAmount = 0.0f;
        window.setAttributes(lp);
        new Thread(() -> {
            try {
                URL url = new URL(catchSwt);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String line = in.readLine();
                StringBuilder json = new StringBuilder();
                while (line != null) {
                    json.append(line);
                    line = in.readLine();
                }
                JSONObject jsonObject = new JSONObject(json.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("swt");
                desc = new StringBuilder();
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (json.toString().contains("desc")) {
                        desc.append("\n").append(jsonArray.getJSONObject(i).getString("desc"));
                    } else {
                        desc.append("");
                    }
                }
                dialogSwt.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void catchData() {
        catchData = "https://spreadsheets.google.com/feeds/cells/18W5B7HDpp9qYmj6wftBfyThgr0vbxLGhnmPlKZB2YsE/1/public/full?alt=json";
        new Thread(() -> {
            try {
                URL url = new URL(catchData);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String line = in.readLine();
                StringBuilder json = new StringBuilder();
                while (line != null) {
                    json.append(line);
                    line = in.readLine();
                }
                JSONObject jsonObject = new JSONObject(valueOf(json));
                JSONObject jsonObject1 = jsonObject.getJSONObject("feed");
                JSONArray jsonArray = jsonObject1.getJSONArray("entry");
                ArrayList<String> all_content = new ArrayList<>();
                for (int i = 6; i < jsonArray.length(); i++) {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    JSONObject jsonObject3 = jsonObject2.getJSONObject("content");
                    all_content.add(jsonObject3.getString("$t"));
                }

                for (int i = 0; i < all_content.size(); i += 4) {
                    ArrayList<String> tmp = new ArrayList<>();
                    tmp.add(all_content.get(i));
                    tmp.add(all_content.get(i + 1));
                    tmp.add(all_content.get(i + 2));
                    tmp.add(all_content.get(i + 3));
                    content_not_final.add(tmp);
                }
                Collections.reverse(content_not_final);
                content = new ArrayList<>(new LinkedHashSet<>(content_not_final));
                //for (int i = 0; i < content.size(); i++) {
                //    ArrayList<String> ij = content.get(i);
                //    for (int j = 0; j < ij.size(); j++) {
                //        System.out.println(ij.get(j));
                //    }
                //}
                startActivity(new Intent(this, Listview.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();


    }

    private void catchWeather() {
        catchWeather = "https://data.weather.gov.hk/weatherAPI/opendata/weather.php?dataType=rhrread&lang=tc";
        dialogweather = ProgressDialog.show(this, "讀取中(1/4)"
                , "請稍候", true);
        Window window = dialogweather.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.0f;// 透明度
        lp.dimAmount = 0.0f;
        window.setAttributes(lp);
        new Thread(() -> {
            try {
                URL url = new URL(catchWeather);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String line = in.readLine();
                StringBuilder json = new StringBuilder();
                while (line != null) {
                    json.append(line);
                    line = in.readLine();
                }
                try {
                    JSONObject jsonObject = new JSONObject(valueOf(json));

                    JSONObject r = jsonObject.getJSONObject("rainfall");
                    JSONObject t = jsonObject.getJSONObject("temperature");
                    JSONObject h = jsonObject.getJSONObject("humidity");
                    JSONArray rd = r.getJSONArray("data");
                    JSONArray td = t.getJSONArray("data");
                    JSONArray hd = h.getJSONArray("data");
                    JSONArray ic = jsonObject.getJSONArray("icon");
                    JSONObject raf = rd.getJSONObject(9);
                    JSONObject tem = td.getJSONObject(15);
                    JSONObject hum = hd.getJSONObject(0);
                    //rainfall
                    ramx = raf.getInt("max");
                    if (ramx != 0) {
                        ramn = raf.getInt("min");
                    }
                    rami = raf.getString("main");
                    //icon
                    icon = ic.getInt(0);
                    //temperature
                    temp = tem.getInt("value");
                    mnft = jsonObject.getString("mintempFrom00To09");
                    //humidity
                    humd = valueOf(hum.getInt("value"));
                    //updateTime
                    updt = jsonObject.getString("updateTime");
                    dialogweather.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    private void catchTitle() {
        catchTitle = "https://spreadsheets.google.com/feeds/cells/18W5B7HDpp9qYmj6wftBfyThgr0vbxLGhnmPlKZB2YsE/1/public/full?alt=json";
        new Thread(() -> {
            try {
                URL url = new URL(catchTitle);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String line = in.readLine();
                StringBuilder json = new StringBuilder();
                while (line != null) {
                    json.append(line);
                    line = in.readLine();
                }

                try {
                    JSONObject jsonObject = new JSONObject(valueOf(json));
                    JSONObject jsonObject1 = jsonObject.getJSONObject("feed");
                    JSONArray jsonArray = jsonObject1.getJSONArray("entry");
                    JSONObject jsonObject2 = jsonArray.getJSONObject(1);
                    JSONObject jsonObject3 = jsonObject2.getJSONObject("content");
                    String tt = jsonObject3.getString("$t");
                    Intent intent = new Intent();
                    intent.setClass(CheckJson.this, Home.class);
                    intent.putExtra("tt", tt);
                    startActivity(intent);
                    Log.d("data", tt);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();


    }

    private void catchWeatherflw() {
        catchWeatherflw = "https://data.weather.gov.hk/weatherAPI/opendata/weather.php?dataType=flw&lang=tc";
        dialogflw = ProgressDialog.show(this, "讀取中(2/4)"
                , "請稍候", true);
        Window window = dialogflw.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.0f;// 透明度
        lp.dimAmount = 0.0f;
        window.setAttributes(lp);
        new Thread(() -> {
            try {
                URL url = new URL(catchWeatherflw);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String line = in.readLine();
                StringBuilder json = new StringBuilder();
                while (line != null) {
                    json.append(line);
                    line = in.readLine();
                }

                try {
                    JSONObject jsonObject = new JSONObject(valueOf(json));
                    gesi = jsonObject.getString("generalSituation");
                    tcif = jsonObject.getString("tcInfo");
                    fdwa = jsonObject.getString("fireDangerWarning");
                    fope = jsonObject.getString("forecastPeriod");
                    fode = jsonObject.getString("forecastDesc");
                    oulo = jsonObject.getString("outlook");
                    fupt = jsonObject.getString("updateTime");
                    dialogflw.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();


    }
}
