package com.chinews.xdapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import java.io.InputStream
import kotlin.coroutines.coroutineContext


class NewsInfoGridViewAdapter(private val context: Context, newsObj: NewsObj) : BaseAdapter() {
    private val mLayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var mItemList: ArrayList<Bitmap> = newsObj.bitmaps
    private var dialog: Dialog? = null

    override fun getCount(): Int {
        //取得 GridView 列表 Item 的數量
        return mItemList.size
    }

    override fun getItem(position: Int): Any {
        //取得 GridView列表於 position 位置上的 Item
        return mItemList[position]
    }

    override fun getItemId(position: Int): Long {
        //取得 GridView 列表於 position 位置上的 Item 的 ID
        return position.toLong()
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v: View = mLayoutInflater.inflate(R.layout.newsinfoopt, parent, false)

        val imgView = v.findViewById(R.id.imageView3) as ImageView
        val txtView = v.findViewById(R.id.textView39) as TextView
        //大图所依附的dialog
        dialog = Dialog(context)
        val mImageView = getImageView(mItemList[position])
        dialog!!.setContentView(mImageView)

        //大图的点击事件（点击让他消失）
        mImageView.setOnClickListener{
                dialog!!.dismiss();
        }
        imgView.setImageBitmap(mItemList[position])
        txtView.text = "no.$position"
        imgView.setOnClickListener {
            dialog!!.show()
        }

        return v
    }

    //动态的ImageView
    private fun getImageView(bm: Bitmap): ImageView {
        val iv = ImageView(context)
        //宽高
        iv.layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        //设置Padding
        iv.setPadding(20, 20, 20, 20)
        //imageView设置图片
        iv.setImageBitmap(bm)
        return iv
    }
}