package com.chinews.xdapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class Warnsum : AppCompatActivity() {
    private var desc: String? = null
    private var wfc: String? = null
    private var wfac: String? = null
    private var wfrc: String? = null
    private var wfrac: String? = null
    private var whc: String? = null
    private var whac: String? = null
    private var wcc: String? = null
    private var wcac: String? = null
    private var wmc: String? = null
    private var wmac: String? = null
    private var wrc: String? = null
    private var wrac: String? = null
    private var wfnc: String? = null
    private var wfnac: String? = null
    private var wlc: String? = null
    private var wlac: String? = null
    private var wtc: String? = null
    private var wtac: String? = null
    private var wtmc: String? = null
    private var wtmac: String? = null
    private var wtsc: String? = null
    private var wtsac: String? = null
    private var catchDataw: String? = null
    private var gesi: String? = null
    private var tcif: String? = null
    private var fdwa: String? = null
    private var fope: String? = null
    private var fode: String? = null
    private var oulo: String? = null
    private var fupt: String? = null
    private var mnft: String? = null
    private var humd: String? = null
    private var updt: String? = null
    private var ramn = 0
    private var ramx = 0
    private var icon = 0
    private var temp = 0
    private var rami: String? = null
    private var wf: JSONObject? = null
    private var wfr: JSONObject? = null
    private var wh: JSONObject? = null
    private var wc: JSONObject? = null
    private var wm: JSONObject? = null
    private var wr: JSONObject? = null
    private var wfn: JSONObject? = null
    private var wl: JSONObject? = null
    private var wt: JSONObject? = null
    private var wtm: JSONObject? = null
    private var wts: JSONObject? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.load)
        val bundle = intent.extras
        gesi = bundle?.getString("gesi")
        tcif = bundle?.getString("tcif")
        fdwa = bundle?.getString("fdwa")
        fope = bundle?.getString("fope")
        fode = bundle?.getString("fode")
        oulo = bundle?.getString("oulo")
        fupt = bundle?.getString("fupt")
        rami = bundle?.getString("rami")
        mnft = bundle?.getString("mnft")
        humd = bundle?.getString("humd")
        updt = bundle?.getString("updt")
        ramx = bundle?.getInt("ramx") ?: 0
        icon = bundle?.getInt("icon") ?: 0
        temp = bundle?.getInt("temp") ?: 0
        ramn = bundle?.getInt("ramn") ?: 0
        desc = bundle?.getString("desc")
        catchWarnsum()
    }

    private fun catchWarnsum() {
        catchDataw = "https://data.weather.gov.hk/weatherAPI/opendata/weather.php?dataType=warnsum&lang=tc"
        Thread {
            try {
                val url = URL(catchDataw)
                val connection = url.openConnection() as HttpURLConnection
                val `is` = connection.inputStream
                val `in` = BufferedReader(InputStreamReader(`is`))
                var line = `in`.readLine()
                val json = StringBuilder()
                while (line != null) {
                    json.append(line)
                    line = `in`.readLine()
                }
                try {
                    val jsonObject = JSONObject(json.toString())
                    @Suppress("SENSELESS_COMPARISON")
                    if (jsonObject != null) {
                        if (jsonObject.toString().contains("WFIRE")) {
                            wf = jsonObject.getJSONObject("WFIRE")
                            wfc = wf?.getString("code")
                            wfac = wf?.getString("actionCode")
                        } else {
                            wfc = "null"
                            wfac = "null"
                        }
                        if (jsonObject.toString().contains("WHOT")) {
                            wh = jsonObject.getJSONObject("WHOT")
                            whc = wh?.getString("code")
                            whac = wh?.getString("actionCode")
                        } else {
                            whc = "null"
                            whac = "null"
                        }
                        if (jsonObject.toString().contains("WCOLD")) {
                            wc = jsonObject.getJSONObject("WCOLD")
                            wcc = wc?.getString("code")
                            wcac = wc?.getString("actionCode")
                        } else {
                            wcc = "null"
                            wcac = "null"
                        }
                        if (jsonObject.toString().contains("WMSGNL")) {
                            wm = jsonObject.getJSONObject("WMSGNL")
                            wmc = wm?.getString("code")
                            wmac = wm?.getString("actionCode")
                        } else {
                            wmc = "null"
                            wmac = "null"
                        }
                        if (jsonObject.toString().contains("WRAIN")) {
                            wr = jsonObject.getJSONObject("WRAIN")
                            wrc = wr?.getString("code")
                            wrac = wr?.getString("actionCode")
                        } else {
                            wrc = "null"
                            wrac = "null"
                        }
                        if (jsonObject.toString().contains("WFNTSA")) {
                            wfn = jsonObject.getJSONObject("WFNTSA")
                            wfnc = wfn?.getString("code")
                            wfnac = wfn?.getString("actionCode")
                        } else {
                            wfnc = "null"
                            wfnac = "null"
                        }
                        if (jsonObject.toString().contains("WL")) {
                            wl = jsonObject.getJSONObject("WL")
                            wlc = wl?.getString("code")
                            wlac = wl?.getString("actionCode")
                        } else {
                            wlc = "null"
                            wlac = "null"
                        }
                        if (jsonObject.toString().contains("WTCSGNL")) {
                            wt = jsonObject.getJSONObject("WTCSGNL")
                            wtc = wt?.getString("code")
                            wtac = wt?.getString("actionCode")
                        } else {
                            wtc = "null"
                            wtac = "null"
                        }
                        if (jsonObject.toString().contains("WTMW")) {
                            wtm = jsonObject.getJSONObject("WTMW")
                            wtmc = wtm?.getString("code")
                            wtmac = wtm?.getString("actionCode")
                        } else {
                            wtmc = "null"
                            wtmac = "null"
                        }
                        if (jsonObject.toString().contains("WTS")) {
                            wts = jsonObject.getJSONObject("WTS")
                            wtsc = wts?.getString("code")
                            wtsac = wts?.getString("actionCode")
                        } else {
                            wtsc = "null"
                            wtsac = "null"
                        }
                        if (jsonObject.toString().contains("WFROST")) {
                            wfr = jsonObject.getJSONObject("WFROST")
                            wfrc = wfr?.getString("code")
                            wfrac = wfr?.getString("actionCode")
                        } else {
                            wfrc = "null"
                            wfrac = "null"
                        }
                        val bundle1 = Bundle()
                        bundle1.putString("gesi", gesi)
                        bundle1.putString("tcif", tcif)
                        bundle1.putString("fdwa", fdwa)
                        bundle1.putString("fope", fope)
                        bundle1.putString("fode", fode)
                        bundle1.putString("oulo", oulo)
                        bundle1.putString("fupt", fupt)
                        bundle1.putString("wfc", wfc)//
                        bundle1.putString("wfac", wfac)
                        bundle1.putString("wfrc", wfrc)
                        bundle1.putString("wfrac", wfrac)
                        bundle1.putString("whc", whc)
                        bundle1.putString("whac", whac)
                        bundle1.putString("wcc", wcc)
                        bundle1.putString("wcac", wcac)
                        bundle1.putString("wmc", wmc)
                        bundle1.putString("wmac", wmac)
                        bundle1.putString("wrc", wrc)
                        bundle1.putString("wrac", wrac)
                        bundle1.putString("wlc", wlc)
                        bundle1.putString("wlac", wlac)
                        bundle1.putString("wtc", wtc)
                        bundle1.putString("wtac", wtac)
                        bundle1.putString("wtmc", wtmc)
                        bundle1.putString("wtmac", wtmac)
                        bundle1.putString("wtsc", wtsc)
                        bundle1.putString("wtsac", wtsac)
                        bundle1.putString("wfnc", wfnc)
                        bundle1.putString("wfnac", wfnac)
                        bundle1.putInt("ramn", ramn)
                        bundle1.putInt("ramx", ramx)
                        bundle1.putString("rami", rami)
                        bundle1.putInt("icon", icon)
                        bundle1.putInt("temp", temp)
                        bundle1.putString("mnft", mnft)
                        bundle1.putString("humd", humd)
                        bundle1.putString("updt", updt)
                        bundle1.putString("desc", desc)
                        val intent3 = Intent()
                        intent3.setClass(this, Weather::class.java)
                        intent3.putExtras(bundle1)
                        startActivity(intent3)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }
}