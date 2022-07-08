package com.chinews.xdapp

import android.os.Bundle
import android.view.View
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class NewsInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_info)
        val gridview = findViewById<GridView>(R.id.GridView)
        val info = findViewById<TextView>(R.id.textView43)
        val download = findViewById<ImageView>(R.id.imageView4)
        val downtext = findViewById<TextView>(R.id.textView44)
        var loading = true
        val newsObj = when (intent.getIntExtra("newsclass", 1)){
            1 -> {
                getOldNewsObj!!
            }
            2 -> {
                getVipNewsObj!!
            }
            0 -> {
                getLastNewsObj!!
            }
            else -> {
                getOldNewsObj!!
            }
        }
        if (newsObj.newscode != "null"){
            info.text = String.format(resources.getString(R.string.bo), newsObj.cy, newsObj.newscode, newsObj.getDate())
        } else {
            info.text = String.format(resources.getString(R.string.bp), newsObj.cy, newsObj.getDate())
        }

        val saMenuItem = NewsInfoGridViewAdapter(this, newsObj, AccountTool(this).isLogin())
        //新增Item到網格中
        gridview.adapter = saMenuItem

        newsObj.let {
            runOnUiThread {
                gridview.adapter = NewsInfoGridViewAdapter(this, it, AccountTool(this).isLogin())
            }
            //val clone = NewsObj(it)
            //clone.bitmaps.clear()
            //clone.startToGetBitmaps()
            //Thread {
            //    do {
            //        if (!clone.loading) break
            //    } while (clone.loading)
            //    loading = false
            //    //新增Item到網格中
            //    runOnUiThread {
            //        gridview.adapter = NewsInfoGridViewAdapter(this, clone, AccountTool(this).isLogin())
            //    }
            //}.start()
        }

        val onClickListener = View.OnClickListener {
            Thread {
                //while (!loading) {}
                for (i in newsObj.bitmaps.indices){
                    SavePhotoTool().saveImageToGallery(
                            this,
                            newsObj.bitmaps[i],
                            newsObj.date,
                            "報章下載",
                            if (newsObj.newscode != "null") {
                                "${newsObj.cy} ${newsObj.newscode}-${newsObj.getDate()} No.${i + 1} "
                            } else {
                                "${newsObj.cy} ${newsObj.getDate()} No.${i + 1} "
                            }
                    )
                }
                runOnUiThread {
                    Toast.makeText(this,getString(R.string.dow),Toast.LENGTH_SHORT).show()
                }
            }.start()
        }
        download.setOnClickListener(onClickListener)
        downtext.setOnClickListener(onClickListener)
    }
}