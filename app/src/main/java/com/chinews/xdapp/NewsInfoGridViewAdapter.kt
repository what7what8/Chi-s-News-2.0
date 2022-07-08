package com.chinews.xdapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.util.*


class NewsInfoGridViewAdapter(private val context: Context, newsObj: NewsObj, val color: Boolean) : BaseAdapter() {
    private val mLayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var mItemList: ArrayList<Bitmap> = newsObj.bitmaps
    private var dialog: Dialog? = null

    override fun getCount(): Int {
        //取得 GridView 列表 Item 的數量
        return if (mItemList.size != 0){
            mItemList.size
        } else 1
    }

    override fun getItem(position: Int): Any {
        //取得 GridView列表於 position 位置上的 Item
        val error = drawableToBitmap(ContextCompat.getDrawable(App.getContext(), android.R.drawable.ic_menu_report_image)!!)
        if (mItemList.isNullOrEmpty()){
            mItemList.add(error)
        } else if (mItemList.contains(error)) mItemList.remove(error)
        return mItemList
    }

    override fun getItemId(position: Int): Long {
        //取得 GridView 列表於 position 位置上的 Item 的 ID
        return position.toLong()
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v: View = mLayoutInflater.inflate(R.layout.newsinfoopt, parent, false)
        if (mItemList.isNullOrEmpty()){
            mItemList.add(drawableToBitmap(ContextCompat.getDrawable(App.getContext(), android.R.drawable.ic_menu_report_image)!!))
        }
        val imgView = v.findViewById(R.id.imageView3) as ImageView
        val pos = position
        imgView.setImageBitmap(mItemList[position])
        if (!color){
            val cm = ColorMatrix()
            cm.setSaturation(0f) // 设置饱和度
            val grayColorFilter = ColorMatrixColorFilter(cm)
            imgView.colorFilter = grayColorFilter
        }
        imgView.setOnClickListener {
            //大图所依附的dialog
            dialog = Dialog(context)
            dialog!!.show()
            val mImageView = getImageView(mItemList[pos])
            dialog!!.setContentView(mImageView)

            //大图的点击事件（点击让他消失）
            mImageView.setOnClickListener{
                dialog!!.dismiss()
            }
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
        if (!color){
            val cm = ColorMatrix()
            cm.setSaturation(0f) // 设置饱和度
            val grayColorFilter = ColorMatrixColorFilter(cm)
            iv.colorFilter = grayColorFilter
        }
        return iv
    }
    @Suppress("SameParameterValue")
    private fun getBitmapOption(inSampleSize: Int): BitmapFactory.Options {
        val options = BitmapFactory.Options()
        options.inPurgeable = true
        options.inSampleSize = inSampleSize
        return options
    }
    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }
        // 取 drawable 的颜色格式,Bitmap.createBitmap 第三个参数
        val config = if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        val bitmap: Bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(1, 1, config) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }
        //将drawable内容画到画布中
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}