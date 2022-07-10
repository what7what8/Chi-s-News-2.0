package com.chinews.xdapp

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.TextView
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


var getOldNewsObj: NewsObj? = null

class OldNews : AppCompatActivity() {
    val newsObjArray = arrayListOf<NewsObj>()
    private val oldNewsObjArray = arrayListOf<NewsObj>()
    private val lastChildHandler: Handler = Handler()
    private lateinit var lastChildRunnable: Runnable
    private var isLogin: Boolean = false
    var progressDialog: ProgressDialog? = null

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_news)
        isLogin = AccountTool(this).isLogin()
        val news = Firebase.database("https://chi-s-news-default-rtdb.europe-west1.firebasedatabase.app/").reference.child("news")
        progressDialog = ProgressDialog(this)
        val listener = news.addChildEventListener(childEventListener)
        progressDialog!!.max = 1
        progressDialog!!.progress = 0
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog!!.setTitle("下載並載入圖片")
        progressDialog!!.show()
        lastChildRunnable = Runnable {
            news.removeEventListener(listener)
            Thread {
                newsObjArray.forEach {
                    if (Date(it.date).before(Date()) && it.cy == "志報") {
                        oldNewsObjArray.add(it)
                        progressDialog!!.max++
                        it.startToGetBitmaps()
                    }
                }
                oldNewsObjArray.reverse()
                progressDialog!!.max--
                Log.d("data", "thread")
                Log.d("data", "${oldNewsObjArray.size}")
                oldNewsObjArray.forEach {
                    do {
                        if (!it.loading) break
                    } while (it.loading)
                    progressDialog!!.progress++
                }
                Log.d("data", "while end")
                runOnUiThread {
                    val recyclerview = findViewById<RecyclerView>(R.id.reclist)
                    recyclerview.layoutManager = LinearLayoutManager(this)
                    // 設置格線
                    recyclerview.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
                    // 將資料交給adapter
                    // 設置adapter給recycler_view
                    recyclerview.adapter = NewsRecyclerViewAdapter(oldNewsObjArray, isLogin)
                    findViewById<SearchView>(R.id.searchView).setOnQueryTextListener(onQueryTextListener)
                    if (newsObjArray.isEmpty()) {
                        val no = findViewById<TextView>(R.id.no)
                        no.visibility = View.VISIBLE
                    }
                    Thread {
                        Thread.sleep(500)
                        progressDialog!!.dismiss()
                    }.start()
                }
            }.start()
        }
    }

    override fun onDestroy() {
        progressDialog?.dismiss()
        super.onDestroy()
    }

    private val onQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return try {
                val searchNewsObjs = arrayListOf<NewsObj>()
                val recyclerview = findViewById<RecyclerView>(R.id.reclist)
                oldNewsObjArray.forEach {
                    if (query?.let { it1 -> it.getSearchKeyWord().contains(it1) } == true) {
                        searchNewsObjs.add(it)
                    }
                }
                recyclerview.adapter = NewsRecyclerViewAdapter(searchNewsObjs, isLogin)
                true
            } catch (e: Exception) {
                false
            }
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return try {
                val searchNewsObjs = arrayListOf<NewsObj>()
                val recyclerview = findViewById<RecyclerView>(R.id.reclist)
                oldNewsObjArray.forEach {
                    if (newText?.let { it1 -> it.getSearchKeyWord().contains(it1) } == true) {
                        searchNewsObjs.add(it)
                    }
                }
                recyclerview.adapter = NewsRecyclerViewAdapter(searchNewsObjs, isLogin)
                true
            } catch (e: Exception) {
                false
            }
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
        getOldNewsObj = (recycler_view.adapter as NewsRecyclerViewAdapter).newsObjs[position]
        Thread {
            Thread.sleep(100)
            startActivity(intent)
        }.start()
    }

    private val childEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            try {
                lastChildHandler.removeCallbacks(lastChildRunnable)
                lastChildHandler.postDelayed(lastChildRunnable, 20)
                Log.d("data", "newsaddget")
                val newsHashMap = snapshot.value as HashMap<*, *>
                newsObjArray.add(NewsObj(newsHashMap["cy"]!!.toString(),
                        if (snapshot.key!!.lastIndexOf("|") == -1) {
                            snapshot.key!!.toLong()
                        } else {
                            snapshot.key!!.substring(0 until snapshot.key!!.lastIndexOf("|")).toLong()
                        }, newsHashMap["id"]!!.toString(), newsHashMap["newscode"]!!.toString()))
                Log.d("data", newsHashMap["cy"]!!.toString())
            } catch (e: NullPointerException) {

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
}