package com.chinews.xdapp

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class NewsRecyclerViewAdapter internal constructor(var newsObjs: ArrayList<NewsObj>) : RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 連結項目布局檔list_item
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.newslistopt, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            // 設置title要顯示的內容
            holder.cy.text = newsObjs[position].cy
            holder.date.text = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(newsObjs[position].date)
            holder.newscode.text =
                    if (newsObjs[position].newscode != "null") {
                        newsObjs[position].newscode
                    } else "報章"
            if (!newsObjs[position].bitmaps.isNullOrEmpty()){
                holder.news.setImageBitmap(newsObjs[position].bitmaps[0])
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return newsObjs.size
    }

    // 建立ViewHolder
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // 宣告元件
        val newscode: TextView
        val cy: TextView
        val date: TextView
        val news: ImageView

        init {
            Log.d("data", "view")
            newscode = itemView.findViewById(R.id.newscode)
            cy = itemView.findViewById(R.id.cy)
            date = itemView.findViewById(R.id.newsdate)
            news = itemView.findViewById(R.id.newsimg)
        }
    }
}