package com.chinews.xdapp

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
import java.text.SimpleDateFormat


class Help : AppCompatActivity() {
    private lateinit var usernamev: String
    private var category: String? = "nologin"
    private var allmessage = ""
    var tm = 0
    override fun onBackPressed() {
        super.onBackPressed()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(Intent(baseContext, MessageNotification::class.java))
        } else {
            startService(Intent(baseContext, MessageNotification::class.java))
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        stopService(Intent(baseContext, MessageNotification::class.java))
        usernamev = "null"
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
                usernamev = jsonObject1.getString("username")
                //email = jsonObject.getString("email");
                category = jsonObject1.getString("category")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            bufferedReader.close()
            fileInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val database = Firebase.database("https://chi-s-news-default-rtdb.europe-west1.firebasedatabase.app/").reference
        val input = findViewById<EditText>(R.id.input)
        val button = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        val ref = database.child("message")
        val notificationRef = database.child("notification")
        notificationRef.addChildEventListener(notification)
        ref.addChildEventListener(childEventListener)
        button.setOnClickListener {
            if ("${input.text}" != "") {
                val message = listOf(usernamev, "$category", "${input.text}")
                ref.child(System.currentTimeMillis().toString()).setValue(message).addOnFailureListener {
                    Toast.makeText(this, getString(R.string.ac), Toast.LENGTH_SHORT).show()
                }.addOnSuccessListener {
                    input.setText("")
                }
            }
        }
    }

    fun getCategory(): String? {
        var category: String? = "no login"
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
        } finally {
            return category
        }
    }

    private fun sendNotification(title: String, message: String, channel: String, url: String?) {
        val resultIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val mBuilder = NotificationCompat.Builder(applicationContext, channel)
                .setContentTitle(title)
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
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
                                    sendNotification(title, content, channel, url)
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

    @Suppress("DEPRECATION")
    private val childEventListener = object : ChildEventListener {
        private var username: String = ""
        private var message: String = ""
        private var category: String = ""

        @Suppress("ControlFlowWithEmptyBody")
        @SuppressLint("SimpleDateFormat")
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
            val text = findViewById<TextView>(R.id.textView19)
            val scroll = findViewById<ScrollView>(R.id.scroll)
            val scrollchild = findViewById<ConstraintLayout>(R.id.scrollchild)
            tm += 1
            Log.d("data", "onChildAdded:" + dataSnapshot.key!!)
            Log.d("data", "onChildAdded:$previousChildName")
            val datab = Firebase.database("https://chi-s-news-default-rtdb.europe-west1.firebasedatabase.app/").reference.child("message").child(dataSnapshot.key!!)
            datab.child("0").get().addOnSuccessListener {
                allmessage += when (it.value.toString()) {
                    "null" -> {
                        getString(R.string.username) + " "
                    }
                    else -> {
                        it.value.toString() + " "
                    }
                }
                username = it.value.toString()
                Log.d("data", "onChildAdded: $username")
            }
            datab.child("1").get().addOnSuccessListener { it1 ->
                allmessage +=
                        when (it1.value.toString()) {
                            "vip" -> {
                                "會員 "
                            }
                            "test" -> {
                                "測試人員 "
                            }
                            "nologin" -> {
                                "未登入 "
                            }
                            else -> {
                                "未登入 "
                            }
                        }
                Log.d("data", "onChildAdded: $category")
            }
            datab.child("2").get().addOnSuccessListener { it2 ->
                val date = dataSnapshot.key!!
                val time = SimpleDateFormat("yyyy-MM-dd HH:mm").format(date.toLong())
                allmessage += time + "\n"
                allmessage += "訊息: " + it2.value.toString() + "\n\n"
                Log.d("data", "onChildAdded: $message")
                runOnUiThread {
                    text.text = allmessage
                    Handler().post { scroll.smoothScrollTo(0, scrollchild.measuredHeight - scroll.height) }
                }
            }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            try {
                Log.d("data", "onChildChanged:" + snapshot.key!!)
                Log.d("data", "onChildChanged:$previousChildName")
            } catch (e: NotImplementedError) {
            }

        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            Log.d("data", "remove")
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d("data", "move")
        }

        override fun onCancelled(error: DatabaseError) {
            runOnUiThread {
                Toast.makeText(this@Help, "${getString(R.string.ac)}\n$error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}