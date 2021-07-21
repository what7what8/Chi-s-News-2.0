package com.chinews.xdapp

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class NewsInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_info)
        val gridview = findViewById<View>(R.id.GridView) as GridView
        val newsObj = getObj!!
        val id = arrayListOf<Int>()
        for (i in newsObj.bitmaps.indices){
            BitmapDrawable(newsObj.bitmaps[i])
            id.add(newsObj.bitmaps[i].generationId)
        }
        val meumList = ArrayList<HashMap<String, Any?>>()
        for (i in 0 until newsObj.bitmaps.size) {
            val map = HashMap<String, Any?>()
            map["ItemImage"] = id[i]
            map["ItemText"] = "NO.$i"
            meumList.add(map)
        }
        val saMenuItem = NewsInfoGridViewAdapter(this,newsObj)

//新增Item到網格中
        //新增Item到網格中
        gridview.adapter = saMenuItem
        gridview.onItemClickListener = OnItemClickListener { arg0: AdapterView<*>?, arg1: View?, arg2: Int, arg3: Long ->
            println("click index:$arg2")
        }
    }
}