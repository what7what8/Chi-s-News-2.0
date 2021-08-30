package com.chinews.xdapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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
import java.text.SimpleDateFormat


class Help : AppCompatActivity() {
    private lateinit var usernamev: String
    private var category: String? = "no login"
    private var allmessage = ""
    val notificationc = Notification(this)
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
        usernamev = (AccountTool(this).getUserName()) ?: "null"
        //email = jsonObject.getString("email");
        category = AccountTool(this).getCategory()
        notificationc.startNotification(false)
        val database = Firebase.database("https://chi-s-news-default-rtdb.europe-west1.firebasedatabase.app/").reference
        val input = findViewById<EditText>(R.id.input)
        val button = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        val ref = database.child("message")
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
            category = hashMap[1].toString()
            Log.d("data", "onChildAdded: $category")
            val date = dataSnapshot.key!!
            val time = SimpleDateFormat("yyyy-MM-dd HH:mm").format(date.toLong())
            allmessage += time + "\n"
            allmessage += "訊息: " + hashMap[2].toString() + "\n\n"
            text.text = allmessage
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