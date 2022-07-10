@file:Suppress("DEPRECATION")

package com.chinews.xdapp

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.chinews.xdapp.R.drawable.*
import com.chinews.xdapp.R.id.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import javax.annotation.Nullable


class Weather : AppCompatActivity() {
    private var json: StringBuilder = StringBuilder()
    private var covidnew: Int = 0
    private var covidold: Int = 0
    private var notsick: Int = 0
    private var death: String = ""
    private var date: String? = ""
    private var chartdate: String = ""
    private var load: ProgressDialog? = null
    private var img1 = 0
    private var img2 = 0
    private var img3 = 0
    private var img4 = 0
    private var img5 = 0
    private var img6 = 0
    private var img7 = 0
    private var img8 = 0
    private var img9 = 0
    override fun onBackPressed() {
        val intent1 = Intent(this, CheckJson::class.java)
        intent1.putExtra("json", 2)
        startActivity(intent1)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weahter)
        val imageView = findViewById<ImageView>(imageView20)
        val textView = findViewById<TextView>(textView29)
        val textView1 = findViewById<TextView>(textView15)
        val textView2 = findViewById<TextView>(textView34)
        val textView3 = findViewById<TextView>(day)
        val covidtext = findViewById<TextView>(covid)
        val button1 = findViewById<ImageView>(imageView19)
        val buttontext = findViewById<TextView>(textView38)
        val bundle = intent.extras
        val ramn = bundle!!.getInt("ramn")
        val ramx = bundle.getInt("ramx")
        //val rami = bundle.getString("rami")
        val icon = bundle.getInt("icon")
        val temp = bundle.getInt("temp")
        val mnft = bundle.getString("mnft")
        val humd = bundle.getString("humd")
        val updt = bundle.getString("updt")
        val gesi = bundle.getString("gesi")
        var tcif = bundle.getString("tcif")
        var fdwa = bundle.getString("fdwa")
        val fope = bundle.getString("fope")
        val fode = bundle.getString("fode")
        val oulo = bundle.getString("oulo")
        val fupt = bundle.getString("fupt")
        val wfc = bundle.getString("wfc")
        val wfac = bundle.getString("wfac")
        val wfrc = bundle.getString("wfrc")
        val wfrac = bundle.getString("wfrac")
        val whc = bundle.getString("whc")
        val whac = bundle.getString("whac")
        val wcc = bundle.getString("wcc")
        val wcac = bundle.getString("wcac")
        val wmc = bundle.getString("wmc")
        val wmac = bundle.getString("wmac")
        val wrc = bundle.getString("wrc")
        val wrac = bundle.getString("wrac")
        val wlc = bundle.getString("wlc")
        val wlac = bundle.getString("wlac")
        val wtc = bundle.getString("wtc")
        val wtac = bundle.getString("wtac")
        val wtmc = bundle.getString("wtmc")
        val wtmac = bundle.getString("wtmac")
        val wtsc = bundle.getString("wtsc")
        val wtsac = bundle.getString("wtsac")
        val wfnc = bundle.getString("wfnc")
        val wfnac = bundle.getString("wfnac")
        var desc = bundle.getString("desc")
        warnsumdo(wtmc, wtmac, ntsunami, "WTMW")
        warnsumdo(wtc, wtac, ntc10, "TC10")
        warnsumdo(wrc, wrac, nrainb, "WRAINB")
        warnsumdo(wrc, wrac, nrainr, "WRAINR")
        warnsumdo(wtc, wtac, ntc9, "TC9")
        warnsumdo(wfnc, wfnac, nntfl, "WFNTSA")
        warnsumdo(wlc, wlac, nlandslip, "WL")
        warnsumdo(wtc, wtac, tc8ne, "TC8NE")
        warnsumdo(wtc, wtac, ntc8b, "TC8SE")
        warnsumdo(wtc, wtac, tc8d, "TC8NW")
        warnsumdo(wtc, wtac, ntc8c, "TC8SW")
        warnsumdo(wrc, wrac, nraina, "WRAINA")
        warnsumdo(wmc, wmac, nsms, "WMSGNL")
        warnsumdo(wtc, wtac, ntc3, "TC3")
        warnsumdo(wfrc, wfrac, nfrost, "WFROST")
        warnsumdo(wtc, wtac, ntc1, "TC1")
        warnsumdo(whc, whac, nvhot, "WHOT")
        warnsumdo(wcc, wcac, ncold, "WCOLD")
        warnsumdo(wfc, wfac, nfirey, "WFIREY")
        warnsumdo(wfc, wfac, nfirer, "WFIRER")
        warnsumdo(wtsc, wtsac, nts, "WTS")
        covid()
        //covid
        load?.setOnDismissListener {
            covidtext.text = """${getString(R.string.香港疫情最新情況)}

${getString(R.string.新增確診)}${(covidnew - covidold)}
${getString(R.string.累計確診)}$covidnew
${getString(R.string.累計死亡)}$death
${getString(R.string.累計出院)}$notsick

更新日期:$date
來源:衞生署

圖表資料來源:資料一線通
圖表更新日期:$chartdate
"""
        }
        button1.setOnClickListener {
            val intentcl = Intent(this, Chart::class.java)
            intentcl.putExtra("covid", json.toString())
            startActivity(intentcl)
        }
        buttontext.setOnClickListener {
            val intentcl = Intent(this, Chart::class.java)
            intentcl.putExtra("covid", json.toString())
            startActivity(intentcl)
        }
        tcif = if (tcif == "") {
            ""
        } else {
            "\n\n颱風資訊\n$tcif"
        }
        fdwa = if (fdwa == "") {
            ""
        } else {
            "\n\n火災提示\n$fdwa"
        }
        desc = if (desc != "") {
            "\n\n特別天氣提示$desc"
        } else {
            ""
        }
        textView2.text = """天氣概況
$gesi

${fope?.replace("本港地區", "")}
$fode

展望未來天氣
$oulo$desc$fdwa${tcif}
"""

        val temps = temp.toString()
        textView1.text = "$temps°C"
        val rafm = ((ramn + ramx) / 2).toString()
        val mnfts: String = if (mnft == "") {
            "沒有記錄"
        } else {
            "$mnft".replace("從昨晚午夜至上午9時，天文台錄得最低氣溫為", "").replace("度。", "°C")
        }
        textView3.text = "來源:香港天文台\n更新時間:$fupt"
        textView.text = """${getString(R.string.一小時平均降雨量)}${rafm}mm
${getString(R.string.濕度)}$humd%
午夜至早上最低溫度:$mnfts
更新時間:$updt
來源:香港天文台"""
        when (icon) {
            50 ->
                when {
                    temp > 28 -> imageView.setImageResource(w50hot_w90)
                    temp > 23 -> imageView.setImageResource(w50mid_w91)
                    temp > 15 -> imageView.setImageResource(w50cold_w92)
                    temp <= 15 -> imageView.setImageResource(w50vcold_w93)
                    else -> imageView.setImageResource(w50mid_w91)
                }
            51 -> imageView.setImageResource(w51)
            52 -> imageView.setImageResource(w52)
            53, 54, 63, 62 -> imageView.setImageResource(w62_63_w53_54)
            60, 61 -> imageView.setImageResource(w60_61)
            64 -> imageView.setImageResource(w64)
            65 -> imageView.setImageResource(w65)
            in 71..75 -> imageView.setImageResource(w70_75)
            76 -> imageView.setImageResource(w76)
            77 -> imageView.setImageResource(w77)
            80 -> imageView.setImageResource(w80)
            81 -> imageView.setImageResource(w81)
            82 -> imageView.setImageResource(w82)
            83 -> imageView.setImageResource(w83)
            84 -> imageView.setImageResource(w84)
            85 -> imageView.setImageResource(w85)
            90 -> imageView.setImageResource(w50hot_w90)
            91 -> imageView.setImageResource(w50mid_w91)
            92 -> imageView.setImageResource(w50cold_w92)
            93 -> imageView.setImageResource(w50vcold_w93)
            else -> throw IllegalStateException("Unexpected value: $icon")
        }
        val refreshLayout = findViewById<SwipeRefreshLayout>(rfru1)
        refreshLayout.setOnRefreshListener {
            startActivity(Intent(this, CheckJson::class.java).putExtra("json", 1))
        }
    }

    private fun covid() {
        load = ProgressDialog.show(this, "讀取中", "請稍候", true)
        val url = "https://api.data.gov.hk/v2/filter?q=%7B%22resource%22%3A%22http%3A%2F%2Fwww.chp.gov.hk%2Ffiles%2Fmisc%2Flatest_situation_of_reported_cases_covid_19_chi.csv%22%2C%22section%22%3A1%2C%22format%22%3A%22json%22%7D"
        val url1 = "https://chp-dashboard.geodata.gov.hk/covid-19/data/keynum.json"
        Volley.newRequestQueue(this).add(
                StringRequest(Request.Method.GET, url, { json ->
                    Volley.newRequestQueue(this).add(
                            StringRequest(Request.Method.GET, url1, { jsoni ->
                                try {
                                    this.json = StringBuilder(json)
                                    Log.d("data", jsoni.toString())
                                    val jsonObject = JSONObject(jsoni.toString())
                                    val chartjson = JSONArray(json.toString())
                                    covidnew = jsonObject.getInt("Confirmed")
                                    covidold = jsonObject.getInt("P_Confirmed")
                                    death = jsonObject.getString("Death")
                                    chartdate = chartjson.getJSONObject(chartjson.length() - 1).getString("更新日期")
                                    date = formatData("dd/MM/yyyy HH:mm", jsonObject.getLong("As_of_date"))
                                    notsick = jsonObject.getInt("Discharged")
                                    Log.d("data", "新增確診:${(covidnew - covidold)}")
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                load?.dismiss()
                            }, Throwable::printStackTrace
                            ))
                }, Throwable::printStackTrace
                ))
    }

    private fun warnsumdo(@Nullable code: String?, @Nullable actcode: String?, res: Int, jsoncode: String) {
        val w1 = findViewById<ImageView>(w1)
        val w2 = findViewById<ImageView>(w2)
        val w3 = findViewById<ImageView>(w3)
        val w4 = findViewById<ImageView>(w4)
        val w5 = findViewById<ImageView>(w5)
        val w6 = findViewById<ImageView>(w6)
        val w7 = findViewById<ImageView>(w7)
        val w8 = findViewById<ImageView>(w8)
        val w9 = findViewById<ImageView>(w9)
        if (code != null) {
            if (code == jsoncode) {
                when {
                    img1 == 0 -> {
                        w1.setImageResource(res)
                        img1 = 1
                        if (actcode == "CANCEL") w1.alpha = 0.1F
                    }
                    img2 == 0 -> {
                        w2.setImageResource(res)
                        img2 = 1
                        if (actcode == "CANCEL") w2.alpha = 0.1F
                    }
                    img3 == 0 -> {
                        w3.setImageResource(res)
                        img3 = 1
                        if (actcode == "CANCEL") w3.alpha = 0.1F
                    }
                    img4 == 0 -> {
                        w4.setImageResource(res)
                        img4 = 1
                        if (actcode == "CANCEL") w4.alpha = 0.1F
                    }
                    img5 == 0 -> {
                        w5.setImageResource(res)
                        img5 = 1
                        if (actcode == "CANCEL") w5.alpha = 0.1F
                    }
                    img6 == 0 -> {
                        w6.setImageResource(res)
                        img6 = 1
                        if (actcode == "CANCEL") w6.alpha = 0.1F
                    }
                    img7 == 0 -> {
                        w7.setImageResource(res)
                        img7 = 1
                        if (actcode == "CANCEL") w7.alpha = 0.1F
                    }
                    img8 == 0 -> {
                        w8.setImageResource(res)
                        img8 = 1
                        if (actcode == "CANCEL") w8.alpha = 0.1F
                    }
                    img9 == 0 -> {
                        w9.setImageResource(res)
                        img9 = 1
                        if (actcode == "CANCEL") w9.alpha = 0.1F
                    }
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun formatData(dataFormat: String?, timeStamp: Long): String? {
        if (timeStamp == 0L) {
            return ""
        }
        //timeStampi *= 1000
        val result: String
        val format = SimpleDateFormat(dataFormat)
        result = format.format(Date(timeStamp))
        return result
    }
}
