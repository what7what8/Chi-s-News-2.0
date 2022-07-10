package com.chinews.xdapp

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class Notification constructor(val context: Context) {
    var foreground = false

    fun startNotification(foreground: Boolean) {
        this.foreground = foreground
        val database = Firebase.database("https://chi-s-news-default-rtdb.europe-west1.firebasedatabase.app/").reference
        val ref = database.child("message")
        val newsRef = database.child("news")
        database.addChildEventListener(notification)
        if (foreground) {
            ref.addChildEventListener(childEventListener)
        } else {
            ref.removeEventListener(childEventListener)
        }
        newsRef.addChildEventListener(newsChildEventListener)
    }

    fun stopNotification() {
        val database = Firebase.database("https://chi-s-news-default-rtdb.europe-west1.firebasedatabase.app/").reference
        val ref = database.child("message")
        val newsRef = database.child("news")
        database.removeEventListener(notification)
        ref.removeEventListener(childEventListener)
        newsRef.removeEventListener(newsChildEventListener)
    }

    private fun sendNotification(title: String, message: String, channel: String, url: String?, category: Int) {
        val resultIntent: Intent = when (category) {
            1 -> Intent(context, Help::class.java)
            2 -> if (channel == "vipmsg") Intent(context, CheckJson::class.java).putExtra("json", 2)
            else Intent(context, MainActivity::class.java)
            3 -> Intent(context, LastNews::class.java)
            else -> Intent(context, MainActivity::class.java)
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val mBuilder = NotificationCompat.Builder(context, channel)
                .setContentTitle(title)
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setContentIntent(pendingIntent)
                .setColor(Color.argb(255, 125, 240, 210))
                .setVibrate(when (category) {
                    1, 2, 3 -> longArrayOf(200, 50, 200, 50, 300)
                    else -> longArrayOf(0)
                })
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
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1, mBuilder.build())
    }

    private val newsChildEventListener = object : ChildEventListener {
        var time = System.currentTimeMillis()
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            if ((System.currentTimeMillis() - time) > 10000) {
                val hashMap = snapshot.getValue<HashMap<String, *>>()!!
                Log.d("data", hashMap.keys.toTypedArray().contentToString() + " " + hashMap["id"])
                val cy = hashMap["cy"].toString()
                val title = "新的${cy}出爐了！！😆"
                val message = "快入去App查看新的${cy}吧！！😁😏"
                sendNotification(title, message, "news", null, 3)
            }
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onCancelled(error: DatabaseError) {

        }
    }
    private val childEventListener = object : ChildEventListener {
        private var time = System.currentTimeMillis()
        private var username: String = ""

        @Suppress("ControlFlowWithEmptyBody")
        @SuppressLint("SimpleDateFormat")
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
            if ((System.currentTimeMillis() - time) > 10000) {
                val hashMap = dataSnapshot.value as ArrayList<*>
                username = when (hashMap[0].toString()) {
                    "null" -> {
                        context.getString(R.string.username) + " "
                    }
                    "" -> {
                        context.getString(R.string.username) + " "
                    }
                    else -> {
                        hashMap[0].toString() + " "
                    }
                }
                if (hashMap.size <= 3 && foreground) {
                    Log.d("data", "$foreground")
                    sendNotification("你收到一條訊息", "${username}說: ${hashMap[2]}", "chat", null, 1)
                }
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
            val content: String
            val channel: String
            val title: String
            val url: String
            val can: Boolean
            if (snapshot.key == "notification") {
                Log.d("data", "onChildChanged: good")
                val hashMap = snapshot.value as ArrayList<*>
                val category = AccountTool(context).getCategory()
                can = if (category == "cusser") true
                else category.let { hashMap[4].toString().contains(it) }
                Log.d("data", "onChildChanged: ${category},$can")
                if (can) {
                    Log.d("data", "onChildChanged: good1")
                    channel = hashMap[0].toString()
                    title = hashMap[1].toString()
                    content = hashMap[2].toString()
                    url = hashMap[3].toString()
                    sendNotification(title, content, channel, url, 2)
                    Log.d("data", "$title,$content,$channel,$url")
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