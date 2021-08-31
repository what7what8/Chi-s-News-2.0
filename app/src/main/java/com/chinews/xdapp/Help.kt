package com.chinews.xdapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.Log
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Help : AppCompatActivity() {
    private lateinit var usernamev: String
    private var category: String? = "no login"
    private var allmessage = ""
    private val notificationc = Notification(this)
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
        notificationc.stopNotification()
        usernamev = (AccountTool(this).getUserName()) ?: "null"
        //email = jsonObject.getString("email");
        category = AccountTool(this).getCategory()
        Log.d("data", "category: $category")
        notificationc.startNotification(false)
        val database = Firebase.database("https://chi-s-news-default-rtdb.europe-west1.firebasedatabase.app/").reference
        val input = findViewById<EditText>(R.id.input)
        val button = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        val more = findViewById<FloatingActionButton>(R.id.floatingActionButton3)
        val ref = database.child("message")
        ref.addChildEventListener(childEventListener)
        button.setOnClickListener {
            if ("${input.text}" != "") {
                val message = listOf(usernamev, "$category", "${input.text}")
                ref.child(System.currentTimeMillis().toString()).setValue(message).addOnFailureListener {
                    Toast.makeText(this, getString(R.string.ac), Toast.LENGTH_SHORT).show()
                }.addOnSuccessListener {
                    //Log.d("data", category.toString())
                    input.setText("")
                }
            }
        }
        more.setOnClickListener {
            val items = arrayOf("傳送日誌", "Whatsapp")
            val listDialog = AlertDialog.Builder(this)
            listDialog.setTitle("更多選項")
            listDialog.setItems(items) { _, which -> // which 下标从0开始
                if (which == 0){
                    val message = listOf(usernamev, "$category", "不支援此訊息，請於更新後查看!","log")
                    ref.child(System.currentTimeMillis().toString()).setValue(message).addOnFailureListener {
                        Toast.makeText(this, getString(R.string.ac), Toast.LENGTH_SHORT).show()
                    }
                    val file = File.createTempFile("log","txt")
                    PrintStream(file).use {
                        it.print(getLogcatInfo())
                    }
                    Firebase.storage("gs://chi-s-news.appspot.com").reference.child("Log").child(
                            "${usernamev}_" +
                                    "${SimpleDateFormat("yyyy-MM-dd",Locale.CHINA)
                                            .format(Date())}.txt").putFile(file.toUri())
                } else {
                    val intent = Intent(this, Web::class.java)
                    intent.putExtra("web",1)
                    startActivity(intent)
                }
            }
            listDialog.show()
        }
    }
    private fun getLogcatInfo(): String {
        val strLogcatInfo = StringBuilder()
        var process: Process? = null
        try {
            val commandLine = ArrayList<String>()
            commandLine.add("logcat")
            commandLine.add("-d")
            val clearLog = ArrayList<String>() //设置命令  logcat -c 清除日志
            clearLog.add("logcat")
            clearLog.add("--clear")
            process = Runtime.getRuntime().exec(commandLine.toArray(arrayOfNulls(commandLine.size)))
            val bufferedReader = BufferedReader(InputStreamReader(process?.inputStream))
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                strLogcatInfo.append(line.toString()+"\n")
            }
            bufferedReader.close()
            Runtime.getRuntime().exec(clearLog.toArray(arrayOfNulls(clearLog.size)))
        } catch (ex: Exception) {
            process?.destroy()
        }
        return strLogcatInfo.toString()
    }
    @Suppress("DEPRECATION")
    private val childEventListener = object : ChildEventListener {
        @Suppress("ControlFlowWithEmptyBody")
        @SuppressLint("SimpleDateFormat")
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
            val text = findViewById<TextView>(R.id.textView19)
            val scroll = findViewById<ScrollView>(R.id.scroll)
            val scrollchild = findViewById<ConstraintLayout>(R.id.scrollchild)
            tm += 1
            //Log.d("data", "onChildAdded:" + dataSnapshot.key!!)
            //Log.d("data", "onChildAdded:$previousChildName")
            val hashMap = dataSnapshot.value as ArrayList<*>
            allmessage += when (hashMap[0].toString()) {
                "null" -> {
                    getString(R.string.username) + " "
                }
                "" -> {
                    getString(R.string.username) + " "
                }
                else -> {
                    hashMap[0].toString() + " "
                }
            }
            allmessage +=
                    when (hashMap[1].toString()) {
                        "vip" -> {
                            "會員 "
                        }
                        "test" -> {
                            "測試人員 "
                        }
                        "no login" -> {
                            "未登入 "
                        }
                        "cusser" -> {
                            "客服 "
                        }
                        else -> {
                            "未登入 "
                        }
                    }
            val date = dataSnapshot.key!!
            val time = SimpleDateFormat("yyyy-MM-dd HH:mm").format(date.toLong())
            allmessage += time + "\n"
            text.text = allmessage
            if (hashMap.size > 3){
                if (hashMap[3] == "log"){
                    text.append("訊息: ")
                    text.append(Html.fromHtml("<i>傳送了程式日誌，開發者可於後台查看!</i>"))
                    text.append("\n\n")
                }
            } else {
                allmessage += "訊息: " + hashMap[2].toString() + "\n\n"
                text.text = allmessage
            }
            Handler().post { scroll.smoothScrollTo(0, scrollchild.measuredHeight - scroll.height) }
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