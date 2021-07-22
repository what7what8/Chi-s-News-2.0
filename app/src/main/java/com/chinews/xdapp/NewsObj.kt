package com.chinews.xdapp

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.Serializable

@Suppress("unused")
data class NewsObj(
        var cy: String,
        var date: Long,
        var id: String,
        var newscode: String,
        var content: Context){
    val bitmaps: ArrayList<Bitmap> = arrayListOf()
    var loading = true
    fun startToGetBitmaps() {
        val gsReference = Firebase.storage("gs://chi-s-news.appspot.com").reference.child(id)
        //android.R.drawable.ic_menu_report_image
        gsReference.listAll().addOnSuccessListener {
            for (i in 0 until it.items.size) {
                val localflie = File(content.cacheDir, "${it.items[i].name}.webp")
                if (localflie.exists()){
                    bitmaps.add(BitmapFactory.decodeFile(localflie.path, getBitmapOption(2)))
                    loading = false
                }else{
                    it.items[i].getFile(localflie).addOnSuccessListener {
                        bitmaps.add(BitmapFactory.decodeFile(localflie.path, getBitmapOption(2)))
                        Log.d("data", "just read")
                    }.addOnFailureListener { it1 ->
                        bitmaps.add(drawableToBitmap(ContextCompat.getDrawable(content, android.R.drawable.ic_menu_report_image)!!))
                        Log.d("data", "why error:${it1.stackTraceToString()}")
                    }.addOnCompleteListener {
                        loading = false
                    }.addOnCanceledListener {
                        loading = false
                    }
                }
            }
        }.addOnFailureListener {
            bitmaps.add(drawableToBitmap(ContextCompat.getDrawable(content, android.R.drawable.ic_menu_report_image)!!))
        }
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
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Single color bitmap will be created of 1x1 pixel
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