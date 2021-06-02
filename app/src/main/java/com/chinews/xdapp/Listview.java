package com.chinews.xdapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Listview extends AppCompatActivity {
    ArrayList<ArrayList<String>> arrayList = CheckJson.content;
    private RecyclerView recycler_view;

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(this, VipArea.class);
        startActivity(intent1);
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        Log.d("data", "onCreate: ");
        //for (int i = 0; i < arrayList.size(); i++) {
        //    ArrayList<String> ij = arrayList.get(i);
        //    for (int j = 0; j < ij.size(); j++) {
        //        Log.d("data", ij.get(j));
        //    }
        //}
        for (int i = 0; i < arrayList.size(); i++) {
            ArrayList<String> ij = arrayList.get(i);
            if (ij.get(0).equals("null")) ij.set(0, "");
            if (ij.get(1).equals("null") && !ij.get(0).equals("null")) ij.set(1, "沒有內容");
            if (ij.get(1).equals("null")) ij.set(1, "");
        }
        List<String> title = new ArrayList<String>() {{
            add(getString(R.string.ag));
            for (int i = 0; i < arrayList.size(); i++) {
                ArrayList<String> ij = arrayList.get(i);
                add(ij.get(0));
            }
        }};
        List<String> content = new ArrayList<String>() {{
            add(getString(R.string.aw));
            for (int i = 0; i < arrayList.size(); i++) {
                ArrayList<String> ij = arrayList.get(i);
                add(ij.get(1));
            }
        }};
        recycler_view = findViewById(R.id.listview);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        // 設置格線
        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        // 將資料交給adapter
        Adapter adapter = new Adapter(title, content);
        // 設置adapter給recycler_view
        recycler_view.setAdapter(adapter);
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refru);
        refreshLayout.setOnRefreshListener(() -> startActivity(new Intent(this, CheckJson.class).putExtra("json", 2)));
    }

    public void OnClick(View v) {
        int position = recycler_view.getChildAdapterPosition(v);
        //Log.d("data", "onCreate: "+position);
        if (position <= 0) {
            Intent intent = new Intent(this, CheckJson.class);
            intent.putExtra("json", 1);
            startActivity(intent);
        }else {
            boolean canc = Objects.equals(arrayList.get(position - 1).get(3), "2");
            if (canc) {
                Intent intent = new Intent(this, ListviewInfo.class);
                intent.putExtra("pos", position - 1);
                startActivity(intent);
            }
        }
    }
}