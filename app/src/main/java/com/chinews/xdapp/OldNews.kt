package com.chinews.xdapp

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class OldNews : AppCompatActivity() {
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oldnews)
        findViewById<Button>(R.id.button2).setOnClickListener {
            initPopWindow()
        }
        //.post {  }
    }
    @SuppressLint("ClickableViewAccessibility", "InflateParams", "SimpleDateFormat")
    private fun initPopWindow() {
        val view: View = LayoutInflater.from(this@OldNews).inflate(R.layout.item_popup, null, false)
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        val popWindow = PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        //popWindow.setAnimationStyle(R.anim.anim_pop) //设置加载动画

        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.isTouchable = true
        popWindow.setTouchInterceptor { _, _ -> false}
        popWindow.setBackgroundDrawable(ColorDrawable(0x000000)) //要为popWindow设置一个背景才有效

        val dm = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(dm)
        val den = dm.density
        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(findViewById(R.id.view2), (-150*den).roundToInt(), (-150*den).roundToInt())

        //设置popupWindow里的按钮的事件
        runOnUiThread {
            val aa = view.findViewById<MaterialCalendarView>(R.id.c)
            val time = System.currentTimeMillis()
            val y = SimpleDateFormat("yyyy").format(time).toInt()
            val m = SimpleDateFormat("MM").format(time).toInt()

            val day = listOf(31, if (y % 4 != 0) 28 else 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            aa.state().edit()
                    .setMinimumDate(CalendarDay.from(2020, 7, 1))
                    .setMaximumDate(CalendarDay.from(y, m, day[m - 1]))
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .commit()
            val aaa = ArrayList<CalendarDay>()
            aaa.add(CalendarDay.from(2021, 6, 3))
            //aa.addDecorator(EventDecorator(aaa, this))
           aa.setOnDateChangedListener { _, date, _ ->
               Log.d("data", "${date.year}-${date.month}-${date.day}")
               popWindow.dismiss()
           }
        }
    }
    private val childEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            snapshot.key
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            
        }

        override fun onCancelled(error: DatabaseError) {
            
        }

    }
}