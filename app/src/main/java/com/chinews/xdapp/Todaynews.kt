@file:Suppress("ReplaceManualRangeWithIndicesCalls", "DEPRECATION")

package com.chinews.xdapp

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class Todaynews : AppCompatActivity() {
    private var process: Process? = null
    private var button: Button? = null
    private var rand1: Random? = null
    private var ram1 = 0
    private var url = ""
    private var dialog: ProgressDialog? = null
    private var date: Date = Date(System.currentTimeMillis())

    @SuppressLint("SimpleDateFormat")
    private var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val db = Firebase.database("https://chi-s-news-default-rtdb.europe-west1.firebasedatabase.app/").reference
    //private var time: String = simpleDateFormat.format(date)


    //private Elements elements;
    //private int tm;
    //private String htmlt;
    //@RequiresApi(api = Build.VERSION_CODES.N)
    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_t_news)
        val webView = findViewById<WebView>(R.id.webviewtnews)
        button = findViewById(R.id.button4)
        html()
        button?.setOnClickListener {
            html()
            dialog?.setOnDismissListener { webView.loadUrl(url) }
        }
        dialog?.setOnDismissListener { webView.loadUrl(url) }
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.useWideViewPort = true //设置webview推荐使用的窗口
        webSettings.loadWithOverviewMode = true //设置webview加载的页面的模式
        webSettings.displayZoomControls = false //隐藏webview缩放按钮
        webSettings.builtInZoomControls = true // 设置显示缩放按钮
        webSettings.setSupportZoom(true) // 支持缩放
        webSettings.useWideViewPort = true //扩大比例的缩放
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL //自适应屏幕
        webSettings.loadWithOverviewMode = true
        webSettings.setAppCacheEnabled(false)
        webSettings.domStorageEnabled = true
        webView.webViewClient = object : WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                super.onReceivedSslError(view, handler, error)
                val ref = db.child("today_news-error")
                        .child("load")
                        .child("ssl")
                        .child(System.currentTimeMillis().toString())
                ref.child("primaryError").setValue(error?.primaryError.toString())
                ref.child("ssl-url").setValue(error?.url.toString())
                ref.child("url").setValue(webView.url.toString())
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                super.onReceivedHttpError(view, request, errorResponse)
                val ref = db.child("today_news-error")
                        .child("load")
                        .child("http")
                        .child(System.currentTimeMillis().toString())
                ref.child("url").setValue(webView.url)
                //data statusCode response(Headers Phrase)
                ref.child("statusCode").setValue(errorResponse?.statusCode)
                ref.child("responseHeaders").setValue(errorResponse?.responseHeaders)
                ref.child("reasonPhrase").setValue(errorResponse?.reasonPhrase.toString())
                ref.child("error-url").setValue(request?.url.toString())
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                //Toast.makeText(this@Todaynews, "${getString(R.string.ac)}(網頁加載)\n${error?.description}", Toast.LENGTH_SHORT).show()
                //button?.text = "發生錯誤"
                val ref = db.child("today_news-error")
                        .child("load")
                        .child("error")
                        .child(System.currentTimeMillis().toString())
                ref.child("description").setValue("${error?.description}")
                ref.child("code").setValue("${error?.errorCode}")
                ref.child("error-url").setValue(request?.url.toString())
                ref.child("url").setValue(webView.url)
            }

            //private val log: StringBuilder = StringBuilder()
            @SuppressLint("SimpleDateFormat")
            override fun onPageFinished(view: WebView?, url: String?) {
                runOnUiThread {
                    button?.text = "刷新"
                    //adb logcat -d -f /sdcard/logcat.txt chromium:I *:S
                    val logcat = getLogcatInfo().split("\n")
                    val error = arrayListOf<String>()
                    for (i in 0 until logcat.size) {
                        if (logcat[i].contains("Error: call id api error")) {
                            error.add(logcat[i])
                        }
                    }
                    for (i in 0 until error.size) {
                        Log.d("data", date.toString())
                        val year = SimpleDateFormat("yyyy").format(date)
                        simpleDateFormat.parse(year + "-" + error[i].split(".")[0])?.toString()?.let { Log.d("data", it) }
                        if (date.before(simpleDateFormat.parse(year + "-" + error[i].split(".")[0]))) {
                            //button?.text = "發生錯誤"
                            dialog?.dismiss()
                            html()
                            dialog?.setOnDismissListener { webView.loadUrl(this@Todaynews.url) }
                        }
                    }
                }
                //if (logcat.contains("Error: call id api error")) button?.text = "重試"
                //Log.d("data",logcat)
                //Log.d("data", logcat.size.toString())
            }
            //override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
            //    if (url == null) return false
            //    try {
            //        if (!url.startsWith("https://")&&!url.startsWith("http://")) {
            //            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            //            startActivity(intent)
            //            return true
            //        }
            //    } catch (e: java.lang.Exception) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
            //        return true //没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
            //    }
            //    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
            //    view.loadUrl(url)
            //    return true
            //}
        }
        MobileAds.initialize(this) {}
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    @SuppressLint("SetTextI18n")
    private fun html() {
        runOnUiThread {
            date = Date(System.currentTimeMillis())
            button?.text = "加載中..."
        }
        dialog = ProgressDialog.show(this, "讀取中", "請稍候", true)
        Thread {
            val urlcopy = url
            try {
                val ram = Random().nextInt(11)
                rand1 = Random()
                ram1 = rand1!!.nextInt(9)
                Log.d("data", ram.toString())
                when (ram) {
                    //5..9
                    10 -> {
                        val doc = Jsoup.connect("https://news.google.com/topstories?hl=zh-HK&gl=HK&ceid=HK:zh-Hant").get()
                        val element = doc.select(".DY5T1d.RZIKme")[ram1]
                        //return element.attr("herf");
                        url = element.attr("href").replace(".", "https://news.google.com")
                        Log.d("data", "html:$url")
                    }
                    in 0..4 -> {
                        val doc = Jsoup.connect("https://hk.news.yahoo.com/rss").get()
                        //return element.attr("herf");
                        url = doc.select("link")[ram1 + 2].text()
                        Log.d("data???", "html:$url")
                        while (url.contains("promotions")) {
                            rand1 = Random()
                            ram1 = rand1!!.nextInt(6)
                            url = doc.select("link")[ram1 + 2].text()
                        }
                        Log.d("data", "html:$url")
                    }
                    in 5..9 -> {
                        val doc = Jsoup.connect("https://www.google.com/search?q=minecraft+-%E8%B5%A4%E8%A3%B8-+-%E6%80%A7%E6%84%9F-&lr=lang_zh-CN%7Clang_zh-TW&safe=active&tbs=lr:lang_1zh-CN%7Clang_1zh-TW,sbd:1,qdr:m&tbm=nws&pws=0").get()
                        val element = doc.select(".dbsr")[ram1]
                        val elements = element.getElementsByTag("a").first()
                        //return element.attr("herf");
                        url = elements.attr("href")
                        Log.d("data???", "html:$url")
                        //while (url.contains("4gamers")) {
                        //    rand1 = Random()
                        //    ram1 = rand1!!.nextInt(6)
                        //    element = doc.select(".dbsr")[ram1]
                        //    elements = element.getElementsByTag("a").first()
                        //    url = elements.attr("href")
                        //}
                        Log.d("data", "html:$url")
                    }
                    in 7..9 -> {
                        @SuppressLint("SimpleDateFormat") val simpleDateFormat = SimpleDateFormat("yyyyMMdd") // HH:mm:ss
                        // 获取当前时间
                        val date = Date(System.currentTimeMillis())
                        Log.d("data", simpleDateFormat.format(date))
                        try {
                            val url1 = URL("https://api.news.tvb.com/news/v2.2.1/entry?category=focus&date=" + simpleDateFormat.format(date) + "&profile=web")
                            val connection = url1.openConnection() as HttpURLConnection
                            val `is` = connection.inputStream
                            val `in` = BufferedReader(InputStreamReader(`is`))
                            var line = `in`.readLine()
                            val json = StringBuilder()
                            while (line != null) {
                                json.append(line)
                                line = `in`.readLine()
                            }
                            val jsonObject = JSONObject(json.toString())
                            val jsonArray = jsonObject.getJSONArray("items")
                            val jsonObject1 = jsonArray.getJSONObject(ram1)
                            url = "https://news.tvb.com/local/" + jsonObject1.getString("id")
                            Log.d("data", "html:$url")
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
                if (url == urlcopy) {
                    dialog?.dismiss()
                    html()
                } else dialog?.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
                Thread {
                    val ref = db.child("today_news-error")
                            .child("url")
                            .child(System.currentTimeMillis().toString())
                    ref.child("message").setValue(e.message.toString())
                    ref.child("stackTrace").setValue(e.stackTrace.toList())
                    ref.child("url").setValue(url)
                }.start()
                dialog?.dismiss()
                runOnUiThread {
                    button?.text = "加載失敗"
                    Toast.makeText(this@Todaynews, "${getString(R.string.ac)}(產生網址)\n${e.cause}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun getLogcatInfo(): String {
        var strLogcatInfo = ""
        try {
            val commandLine = ArrayList<String>()
            commandLine.add("logcat")
            commandLine.add("-d")
            commandLine.add("chromium:I")
            commandLine.add("*:S")
            val clearLog = ArrayList<String>() //设置命令  logcat -c 清除日志
            clearLog.add("logcat")
            clearLog.add("-c")
            process = Runtime.getRuntime().exec(commandLine.toArray(arrayOfNulls(commandLine.size)))
            val bufferedReader = BufferedReader(InputStreamReader(process?.inputStream))
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                strLogcatInfo = "$strLogcatInfo${line.toString()}\n"
            }
            bufferedReader.close()
            Runtime.getRuntime().exec(clearLog.toArray(arrayOfNulls(clearLog.size)))
        } catch (ex: Exception) {
            process?.destroy()
        }
        return strLogcatInfo
    }
}