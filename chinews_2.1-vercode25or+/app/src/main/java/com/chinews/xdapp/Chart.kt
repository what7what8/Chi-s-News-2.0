@file:Suppress("DEPRECATION")

package com.chinews.xdapp

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*


class Chart : AppCompatActivity() {
    private var button: Button? = null
    private var allday: Array<String?> = arrayOf()
    private var mouthdate: Array<String?> = arrayOf()
    private var notsick: Array<Float?> = arrayOf()
    private var mouthday: Array<String?> = arrayOf()
    private var mouth: Int = 0
    private var day: Int = 0
    private var alldeath: Array<Float?> = arrayOf()
    private var allcovid: Array<Float?> = arrayOf()

    //private var daycovidmax: Array<Float?> = arrayOf(0F)
    private var mouthcovid: Array<Float?> = arrayOf()
    private var lineChart: LineChart? = null
    private var daycovid: Array<Float?> = arrayOf()
    private var jsonArray: JSONArray = JSONArray()
    private var load: ProgressDialog? = null

    //private var reload : Boolean = true
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)
        button = findViewById(R.id.button5)
        covid()

        button?.setOnClickListener {
            findViewById<LineChart>(R.id.lc).animateXY(2000, 2000)
            findViewById<LineChart>(R.id.lc1).animateXY(2000, 2000)
            findViewById<LineChart>(R.id.lc2).animateXY(2000, 2000)
            //Toast.makeText(this, "由於過快點擊會導致數據未加載即載入折線\n圖，引致加載折線圖X軸時發生崩潰，\n因此已將該功能封鎖。", Toast.LENGTH_SHORT).show()
        }
        load?.setOnDismissListener {
            if (SimpleDateFormat("dd").format(System.currentTimeMillis()).toInt() >= 3) {
                runOnUiThread {
                lc2()
                lc1()
                lc()
                }
            }else{
                runOnUiThread {
                lc2()
                lc1()
                val lineChart = findViewById<LineChart>(R.id.lc)
                lineChart.visibility = View.GONE
                }
            }
        }
    }

    private fun lc1() {
        lineChart = findViewById(R.id.lc1)
        lineChart?.xAxis?.isEnabled = true
        lineChart?.axisRight?.isEnabled = false
        val datadaycovid: ArrayList<Entry> = ArrayList()
        val daycovidcopy = daycovid
        for (i in daycovidcopy.indices) {
            datadaycovid.add(Entry(i.toFloat(), daycovidcopy[i]!!))
        }
        val dataSet = LineDataSet(datadaycovid, getString(R.string.be)) // 图表绑定数据，设置图表折现备注
        dataSet.color = ContextCompat.getColor(this,R.color.covid) // 设置折线图颜色
        dataSet.valueTextColor = Color.WHITE // 设置数据值的颜色
        val description = lineChart?.description
        dataSet.setDrawCircles(false)
        description?.textSize = 100F
        description?.text = getString(R.string.bf) // 设置右下角备注
        //color
        description?.textColor = ContextCompat.getColor(this,R.color.black)
        lineChart?.xAxis?.textColor = ContextCompat.getColor(this,R.color.black)
        lineChart?.axisLeft?.textColor = ContextCompat.getColor(this,R.color.black)
        lineChart?.legend?.textColor = ContextCompat.getColor(this,R.color.black)
        //
        lineChart?.xAxis?.granularity = 1F
        try {
            lineChart?.xAxis?.valueFormatter = IAxisValueFormatter { value, _ ->
                Log.d("TAG", "----->getFormattedValue: $value")
                val index = value.toInt()
                val label = mouthday[index].toString()
                label
            }
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.bg), Toast.LENGTH_SHORT).show()
        }
        val data = LineData(dataSet)
        lineChart?.data = data
        lineChart?.animateXY(2000, 2000)

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun lc() {

        val lineChart = findViewById<LineChart>(R.id.lc)

        lineChart?.axisRight?.isEnabled = false
        val datadaycovid1: ArrayList<Entry> = ArrayList()
        val daycovidcopy1 = mouthcovid
        for (i in daycovidcopy1.indices) {
            datadaycovid1.add(Entry(i.toFloat(), daycovidcopy1[i]!!))
        }
        val dataSet1 = LineDataSet(datadaycovid1, getString(R.string.be)) // 图表绑定数据，设置图表折现备注
        dataSet1.color = ContextCompat.getColor(this,R.color.covid) // 设置折线图颜色
        dataSet1.valueTextColor = Color.WHITE // 设置数据值的颜色
        dataSet1.setDrawFilled(true)
        dataSet1.fillDrawable = ContextCompat.getDrawable(this,R.drawable.covid_line_background)
        dataSet1.setDrawCircles(true)
        dataSet1.setDrawCircleHole(true)
        dataSet1.setCircleColors(ContextCompat.getColor(this,R.color.covid))
        val description = lineChart?.description
        description?.textSize = 100F
        description?.text = getString(R.string.bi) // 设置右下角备注
        val data = LineData(dataSet1)
        //color
        description?.textColor = ContextCompat.getColor(this,R.color.black)
        lineChart?.xAxis?.textColor = ContextCompat.getColor(this,R.color.black)
        lineChart?.axisLeft?.textColor = ContextCompat.getColor(this,R.color.black)
        lineChart?.legend?.textColor = ContextCompat.getColor(this,R.color.black)
        //
        lineChart?.xAxis?.granularity = 1F
        lineChart?.data = data
        lineChart?.animateXY(2000, 2000)
        try {
            lineChart?.xAxis?.valueFormatter = IAxisValueFormatter { value, _ ->
                Log.d("TAG", "----->getFormattedValue: $value")
                val index = value.toInt()
                val label = mouthdate[index].toString()
                label
            }
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.bg), Toast.LENGTH_SHORT).show()
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun lc2() {

        val lineChart: LineChart? = findViewById(R.id.lc2)

        lineChart?.axisRight?.isEnabled = false
        val datadaycovid: ArrayList<Entry> = ArrayList()
        val daycovidcopy = allcovid
        for (i in daycovidcopy.indices) {
            datadaycovid.add(Entry(i.toFloat(), daycovidcopy[i]!!))
        }
        val datadaycovid1: ArrayList<Entry> = ArrayList()
        val daycovidcopy1 = alldeath
        for (i in daycovidcopy1.indices) {
            datadaycovid1.add(Entry(i.toFloat(), daycovidcopy1[i]!!))
        }
        val datadaycovid2: ArrayList<Entry> = ArrayList()
        val daycovidcopy2 = notsick
        for (i in daycovidcopy2.indices) {
            datadaycovid2.add(Entry(i.toFloat(), daycovidcopy2[i]!!))
        }
        val dataSet = LineDataSet(datadaycovid, getString(R.string.be))
        val dataSet1 = LineDataSet(datadaycovid1, getString(R.string.bl)) // 图表绑定数据，设置图表折现备注
        val dataSet2 = LineDataSet(datadaycovid2, getString(R.string.bj))
        dataSet.color = ContextCompat.getColor(this,R.color.covid) // 设置折线图颜色
        dataSet.valueTextColor = Color.WHITE // 设置数据值的颜色
        dataSet.setDrawFilled(true)
        dataSet.fillDrawable = ContextCompat.getDrawable(this,R.drawable.covid_line_background)
        dataSet.setDrawCircles(false)
        dataSet1.color = ContextCompat.getColor(this,R.color.death) // 设置折线图颜色
        dataSet1.valueTextColor = Color.WHITE // 设置数据值的颜色
        dataSet1.setDrawCircles(false)
        dataSet1.setDrawFilled(true)
        dataSet1.fillDrawable = ContextCompat.getDrawable(this,R.drawable.death_line_background)
        dataSet2.color = ContextCompat.getColor(this,R.color.notsick) // 设置折线图颜色
        dataSet2.valueTextColor = Color.WHITE // 设置数据值的颜色
        dataSet2.setDrawCircles(false)
        dataSet2.setDrawFilled(true)
        dataSet2.fillDrawable = ContextCompat.getDrawable(this,R.drawable.notsick_line_background)
        val description = lineChart?.description
        description?.textSize = 100F
        description?.text = getString(R.string.bk) // 设置右下角备注
        val data = LineData(dataSet2, dataSet1, dataSet)
        lineChart?.data = data
        //color
        description?.textColor = ContextCompat.getColor(this,R.color.black)
        lineChart?.xAxis?.textColor = ContextCompat.getColor(this,R.color.black)
        lineChart?.xAxis?.granularity = 1F
        lineChart?.axisLeft?.textColor = ContextCompat.getColor(this,R.color.black)
        lineChart?.legend?.textColor = ContextCompat.getColor(this,R.color.black)
        //
        try {
            lineChart?.xAxis?.valueFormatter = IAxisValueFormatter { value, _ ->
                Log.d("TAG", "----->getFormattedValue: $value")
                val index = value.toInt()
                val label = allday[index].toString()
                label
            }
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.bg), Toast.LENGTH_SHORT).show()
        }
        lineChart?.animateXY(2000, 2000)
        Log.d("data", "done")

    }

    @SuppressLint("SimpleDateFormat")
    private fun covid() {
        load = ProgressDialog.show(this, "讀取中", "請稍候", true)
        val window: Window? = load?.window
        val lp = window?.attributes
        lp?.alpha = 0.0f // 透明度
        lp?.dimAmount = 0.0f
        window?.attributes = lp
        allday = arrayOf()
        mouthdate = arrayOf()
        notsick = arrayOf()
        mouthday = arrayOf()
        mouth = 0
        day = 0
        alldeath = arrayOf()
        allcovid = arrayOf()
        mouthcovid = arrayOf()
        daycovid = arrayOf()
        jsonArray = JSONArray()
        Thread {
            try {
                val json = this.intent.getStringExtra("covid")
                json?.let { Log.d("data", it) }
                jsonArray = JSONArray(json)
                @SuppressLint("SimpleDateFormat")
                val simpleDateFormat = SimpleDateFormat("dd")
                val simpleDateFormat1 = SimpleDateFormat("MM")
                // 获取当前时间
                val date = Date(System.currentTimeMillis())
                day = simpleDateFormat.format(date).toInt()
                Log.d("data",((jsonArray.length() - day) + 1).toString())
                mouth = simpleDateFormat1.format(date).toInt()
                for (i in 1 until jsonArray.length()) {
                    val coviddatemouth = jsonArray.getJSONObject(i).getString("更新日期").split("/")[1].toInt()
                    val coviddateday = jsonArray.getJSONObject(i).getString("更新日期").split("/")[0].toInt()
                    mouthday = insert(mouthday, "$coviddateday/$coviddatemouth")
                    Log.d("data", mouthday[i - 1].toString())
                    daycovid = insert(daycovid, (jsonArray.getJSONObject(i).getInt("確診個案") - jsonArray.getJSONObject(i - 1).getInt("確診個案")).toFloat())
                }
                for (i in 0 until jsonArray.length()) {
                    val coviddatemouth = jsonArray.getJSONObject(i).getString("更新日期").split("/")[1].toInt()
                    val coviddateday = jsonArray.getJSONObject(i).getString("更新日期").split("/")[0].toInt()
                    allday = insert(allday, "$coviddateday/$coviddatemouth")
                    alldeath = insert(alldeath, jsonArray.getJSONObject(i).getInt("死亡").toFloat())
                    allcovid = insert(allcovid, jsonArray.getJSONObject(i).getInt("確診個案").toFloat())
                    notsick = insert(notsick, jsonArray.getJSONObject(i).getInt("出院").toFloat())
                }
                for (i in (jsonArray.length() - day) + 1 until jsonArray.length()) {
                    val coviddatemouth = jsonArray.getJSONObject(i).getString("更新日期").split("/")[1].toInt()
                    val coviddateday = jsonArray.getJSONObject(i).getString("更新日期").split("/")[0].toInt()
                    mouthdate = insert(mouthdate, "$coviddateday/$coviddatemouth")
                    mouthcovid = insert(mouthcovid, (jsonArray.getJSONObject(i).getInt("確診個案") - jsonArray.getJSONObject(i - 1).getInt("確診個案")).toFloat())
                }
                load?.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
    private fun insert(arr: Array<Float?>, float: Float): Array<Float?> {
        val size = arr.size //获取数组长度
        val tmp = arrayOfNulls<Float>(size + 1) //新建临时字符串数组，在原来基础上长度加一
        for (i in 0 until size) { //先遍历将原来的字符串数组数据添加到临时字符串数组
            tmp[i] = arr[i]
        }
        tmp[size] = float //在最后添加上需要追加的数据
        return tmp //返回拼接完成的字符串数组
    }

    private fun insert(arr: Array<String?>, str: String): Array<String?> {
        val size = arr.size //获取数组长度
        val tmp = arrayOfNulls<String>(size + 1) //新建临时字符串数组，在原来基础上长度加一
        for (i in 0 until size) { //先遍历将原来的字符串数组数据添加到临时字符串数组
            tmp[i] = arr[i]
        }
        tmp[size] = str //在最后添加上需要追加的数据
        return tmp //返回拼接完成的字符串数组
    }
}