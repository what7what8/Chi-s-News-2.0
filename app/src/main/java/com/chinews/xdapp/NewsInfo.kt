package com.chinews.xdapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity

class NewsInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_info)
        val gridview = findViewById<View>(R.id.GridView) as GridView
        val newsObj =
        if (intent.getIntExtra("newsclass",1) == 0){
            getLastNewsObj!!
        }else {
            getOldNewsObj!!
        }
        val saMenuItem = NewsInfoGridViewAdapter(this,newsObj,AccountTool(this).isLogin())

//新增Item到網格中
        //新增Item到網格中
        gridview.adapter = saMenuItem
        gridview.onItemClickListener = OnItemClickListener { arg0: AdapterView<*>?, arg1: View?, arg2: Int, arg3: Long ->
            println("click index:$arg2")
        }
    }
}