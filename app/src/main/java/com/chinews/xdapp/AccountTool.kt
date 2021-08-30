package com.chinews.xdapp

import android.content.Context
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class AccountTool(context: Context) {
    var jsonObject1 = JSONObject()
    init {
        try {
            val fileInputStream = context.openFileInput("cache_text")
            val bufferedReader = BufferedReader(InputStreamReader(fileInputStream))
            var line = bufferedReader.readLine()
            val json = StringBuilder()
            while (line != null) {
                // Log.d("data", "" + line);
                json.append(line)
                line = bufferedReader.readLine()
            }
            bufferedReader.close()
            fileInputStream.close()
            jsonObject1 = JSONObject(json.toString())
        }catch (e: IOException){}
    }
    fun isLogin(): Boolean {
        var status = false
        try{
            //email = jsonObject.getString("email");
            if (jsonObject1.getString("status") == "login"){
                status = true
            }
        } catch (e: IOException) {

        } finally {
            return status
        }
    }
    fun getCategory(): String {
        var category = "no login"
            try {
                category = jsonObject1.getString("category")
        } catch (e: IOException) {

        } finally {
            return category
        }
    }
    fun getUserName(): String? {
        var username: String? = null
        try {
            username = jsonObject1.getString("username")
        } catch (e: IOException) {

        } finally {
            return username
        }
    }
}