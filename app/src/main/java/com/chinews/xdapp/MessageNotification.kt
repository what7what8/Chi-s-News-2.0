package com.chinews.xdapp

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MessageNotification : Service() {
    private var time = System.currentTimeMillis()
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        FirebaseApp.initializeApp(this)
        sendNotification("正在獲取新訊息", "正在獲取新訊息，你可以輕按此訊息前往關閉通知。", "foreground", null, 0)
        time = System.currentTimeMillis()
        val database = Firebase.database("https://chi-s-news-default-rtdb.europe-west1.firebasedatabase.app/").reference
        val ref = database.child("message")
        val notificationRef = database.child("notification")
        notificationRef.addChildEventListener(notification)
        ref.addChildEventListener(childEventListener)

        return super.onStartCommand(intent, flags, startId)
    }
    fun getCategory(): String? {
        var category : String? = "no login"
        try {
            val fileInputStream = openFileInput("cache_text")
            val bufferedReader = BufferedReader(InputStreamReader(fileInputStream))
            var line = bufferedReader.readLine()
            val json = StringBuilder()
            while (line != null) {
                // Log.d("data", "" + line);
                json.append(line)
                line = bufferedReader.readLine()
            }
            try {
                val jsonObject1 = JSONObject(json.toString())
                //email = jsonObject.getString("email");
                category = jsonObject1.getString("category")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            bufferedReader.close()
            fileInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }finally {
            return category
        }
    }
    private fun sendNotification(title: String, message: String, channel: String, url: String?, category: Int) {
        val resultIntent : Intent = when (category) {
            1 -> Intent(this, Help::class.java)
            2 -> if (channel == "vipmsg") Intent(this, MainActivity::class.java)
                 else Intent(this, CheckJson::class.java).putExtra("json",2)
            0 -> {
                if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.O) {
                    Intent()
                            .setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .addCategory(Intent.CATEGORY_DEFAULT)
                            .setData(Uri.parse("package:" + this.packageName))
                } else {
                    Intent()
                            .setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                            .putExtra(Settings.EXTRA_APP_PACKAGE, this.packageName)
                            .putExtra(Settings.EXTRA_CHANNEL_ID, this.applicationInfo.uid)
                }
            }
                else -> Intent(this, MainActivity::class.java)
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val mBuilder = NotificationCompat.Builder(applicationContext, channel)
                .setContentTitle(title)
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(if (category != 0) {
                    NotificationCompat.PRIORITY_DEFAULT
                } else {
                    NotificationCompat.PRIORITY_MIN
                })
                .setContentIntent(pendingIntent)
                .setColor(Color.argb(255, 125, 240, 210))
        if (url != "" && url != null) {
            val bitmap: Bitmap?
            try {
                val httpURLConnection = URL(url).openConnection() as HttpURLConnection
                httpURLConnection.connect()
                bitmap = BitmapFactory.decodeStream(httpURLConnection.inputStream)
                mBuilder.setLargeIcon(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        //设置点击通知之后的响应，启动SettingActivity类
        //通过 builder.build() 拿到 notification
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.notify(1, mBuilder.build())
        if (category == 0) startForeground(1, mBuilder.build())
    }

    private val childEventListener = object : ChildEventListener {
        private var username: String = ""

        @Suppress("ControlFlowWithEmptyBody")
        @SuppressLint("SimpleDateFormat")
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
            if ((System.currentTimeMillis() - time) > 10000) {
                val datab = Firebase.database("https://chi-s-news-default-rtdb.europe-west1.firebasedatabase.app/").reference.child("message").child(dataSnapshot.key!!)
                datab.child("0").get().addOnSuccessListener {
                    username = when (it.value.toString()) {
                        "null" -> {
                            getString(R.string.username) + " "
                        }
                        else -> {
                            it.value.toString() + " "
                        }
                    }
                }
                datab.child("2").get().addOnSuccessListener { it2 ->
                    sendNotification("你收到一條訊息", "${username}說: ${it2.value.toString()}", "chat", null, 1)
                    time = System.currentTimeMillis()
                }
            } else {
                time = System.currentTimeMillis()
            }
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
    private val notification = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            var content: String
            var channel: String
            var title: String
            var url: String
            var can: Boolean
            Log.d("data", "onChildChanged: good")
            val database = Firebase.database("https://chi-s-news-default-rtdb.europe-west1.firebasedatabase.app/").reference
            val notificationRef = database.child("notification")
            notificationRef.child("4").get().addOnSuccessListener { audience ->
                val value = audience.value.toString()
                can = getCategory()?.let { value.contains(it) } == true
                Log.d("data", "onChildChanged: ${getCategory()},$can")
                if (can) {
                    Log.d("data", "onChildChanged: good1")
                    notificationRef.child("0").get().addOnSuccessListener {
                        channel = it.value.toString()
                        notificationRef.child("1").get().addOnSuccessListener { it1 ->
                            title = it1.value.toString()
                            notificationRef.child("2").get().addOnSuccessListener { it2 ->
                                content = it2.value.toString()
                                notificationRef.child("3").get().addOnSuccessListener { it3 ->
                                    url = it3.value.toString()
                                    sendNotification(title, content, channel, url, 2)
                                    Log.d("data", "$title,$content,$channel,$url")
                                }
                            }
                        }
                    }
                }
            }
        }


        override fun onChildRemoved(snapshot: DataSnapshot) {
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }
}