package com.chinews.xdapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

var getObj :NewsObj? = null
class LastNews : AppCompatActivity() {
    val newsObjArray = arrayListOf<NewsObj>()
    val cyArray = arrayListOf<String>()
    val lastNewsArray = arrayListOf<NewsObj>()
    var reverseNewsObjArray = arrayListOf<NewsObj>()
    private val lastChildHandler: Handler = Handler()
    private lateinit var lastChildRunnable :Runnable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_last_news)
        val news = Firebase.database("https://chi-s-news-default-rtdb.europe-west1.firebasedatabase.app/").reference.child("news")
        news.addChildEventListener(childEventListener)
            lastChildRunnable = Runnable {
                    Log.d("data", "thread")
                    reverseNewsObjArray = ArrayList(newsObjArray)
                    reverseNewsObjArray.reverse()
                    reverseNewsObjArray.forEach {
                        if (!cyArray.contains(it.cy)) {
                            cyArray.add(it.cy)
                            lastNewsArray.add(it)
                        }
                        Log.d("data", "forloop")
                    }
                Thread{
                    while (lastNewsArray[0].loading) {
                        if (!lastNewsArray[0].loading) break
                    }
                runOnUiThread {
                    val recyclerview = findViewById<RecyclerView>(R.id.reclist)
                    recyclerview.layoutManager = LinearLayoutManager(this)
                    // 設置格線
                    recyclerview.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
                    // 將資料交給adapter
                    // 設置adapter給recycler_view
                    recyclerview.adapter = NewsRecyclerViewAdapter(lastNewsArray)
                }
            }.start()
            }
        }
    @Suppress("LocalVariableName")
    fun OnClick(v: View) {
        val recycler_view = findViewById<RecyclerView>(R.id.reclist)
        val position: Int = recycler_view.getChildAdapterPosition(v)
        //Log.d("data", "onCreate: "+position);
        /*
        if (position <= 0) {
            val intent = Intent(this, CheckJson::class.java)
            intent.putExtra("json", 1)
            startActivity(intent)
        } else {
            val canc = arrayList.get(position - 1).get(3) == "2"
            if (canc) {
                val intent = Intent(this, VipRecyclerViewInfo::class.java)
                intent.putExtra("pos", position - 1)
                startActivity(intent)
            }
        }
         */
        val intent = Intent(this, NewsInfo::class.java)
        getObj = lastNewsArray[position]
        startActivity(intent)
    }
    private val childEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                lastChildHandler.removeCallbacks(lastChildRunnable);
                lastChildHandler.postDelayed(lastChildRunnable, 20);
            Log.d("data", "newsaddget")
            val newsHashMap = snapshot.value as HashMap<*, *>
            newsObjArray.add(NewsObj(newsHashMap["cy"]!!.toString(), snapshot.key!!.toLong(), newsHashMap["id"]!!.toString(), newsHashMap["newscode"]!!.toString()))
            Log.d("data", newsHashMap["cy"]!!.toString())
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
}