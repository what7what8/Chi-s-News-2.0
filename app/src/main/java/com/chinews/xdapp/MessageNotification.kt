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
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class MessageNotification : Service() {
    private var time = System.currentTimeMillis()
    val notificationc = Notification(this)
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationc.stopNotification()
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        FirebaseApp.initializeApp(this)
        time = System.currentTimeMillis()
        notificationc.startNotification(true)
        val resultIntent: Intent =
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
        val pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val mBuilder = NotificationCompat.Builder(applicationContext, "foreground")
                .setContentTitle("正在獲取新訊息")
                .setContentText("正在獲取新訊息，你可以輕按此訊息前往關閉通知。")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setContentIntent(pendingIntent)
        startForeground(1, mBuilder.build())
        return super.onStartCommand(intent, flags, startId)
    }



    private fun sendNotification(title: String, message: String, channel: String, url: String?, category: Int) {
        val resultIntent: Intent = when (category) {
            1 -> Intent(this, Help::class.java)
            2 -> if (channel == "vipmsg") Intent(this, CheckJson::class.java).putExtra("json", 2)
            else Intent(this, MainActivity::class.java)
            3 -> Intent(this, LastNews::class.java)
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
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.notify(1, mBuilder.build())
        if (category == 0) startForeground(1, mBuilder.build())
    }
    private val newsChildEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            if ((System.currentTimeMillis() - time) > 10000) {
                val hashMap = snapshot.value as HashMap<*,*>
                val cy = hashMap["cy"].toString()
                val title = "新的${cy}出爐了！！😆"
                val message = "快入去App查看新的${cy}吧！！😁😏"
                sendNotification(title,message,"news",null,3)
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
    private val childEventListener = object : ChildEventListener {
        private var username: String = ""

        @Suppress("ControlFlowWithEmptyBody")
        @SuppressLint("SimpleDateFormat")
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
            if ((System.currentTimeMillis() - time) > 10000) {
                val hashMap = dataSnapshot.value as HashMap<*,*>
                    username = when (hashMap[0].toString()) {
                        "null" -> {
                            getString(R.string.username) + " "
                        }
                        else -> {
                            hashMap[0].toString() + " "
                        }
                    }
                    sendNotification("你收到一條訊息", "${username}說: ${hashMap[2].toString()}", "chat", null, 1)
                    time = System.currentTimeMillis()
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
            val content: String
            val channel: String
            val title: String
            val url: String
            val can: Boolean
            Log.d("data", "onChildChanged: good")
            val hashMap = snapshot.value as HashMap<*,*>
            val category = AccountTool(this@MessageNotification).getCategory()
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


        override fun onChildRemoved(snapshot: DataSnapshot) {
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }
}