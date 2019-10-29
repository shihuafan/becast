package com.example.becast.data.user

import android.content.Context
import com.example.becast.R


object UserData {
    var uid:Int=0
    var name:String?=" "
    var image:String?=" "
    var isLogin:Boolean=false
    var style:Int= R.style.AppTheme

    fun getAll(context:Context) :UserData{
        val sp = context.getSharedPreferences("name", Context.MODE_PRIVATE)
        uid=sp.getInt("uid", 0)
        name=sp.getString("name", "")
        image=sp.getString("image", "")
        isLogin=sp.getBoolean("is_login",false)
        style=sp.getInt("style",R.style.DarkTheme)
        return this
    }

    fun setAll(context:Context) {
        val sp = context.getSharedPreferences("name", Context.MODE_PRIVATE)
        val edit=sp.edit()
        edit.putInt("uid", 9)
        edit.putString("name", "拉普兰德")
        edit.putString("image", "http://raw.yiyoushuo.com/UGC/21f4bb93-9344-4a89-923c-6f9ce4a1b58b.jpg?x-oss-process=image/format,jpg")
        edit.putBoolean("is_login",true)
        edit.apply()
    }

    fun clearAll(context: Context){
        val sp = context.getSharedPreferences("name", Context.MODE_PRIVATE)
        val edit=sp.edit()
        uid=0
        name=""
        image=""
        isLogin=false
        edit.putInt("uid", 0)
        edit.putString("name", "")
        edit.putString("image", "")
        edit.putBoolean("is_login",false)
        edit.apply()
    }

    fun changeStyle(context: Context){
        style = if(style==R.style.AppTheme){
            R.style.DarkTheme
        }else{
            R.style.AppTheme
        }
        val sp = context.getSharedPreferences("name", Context.MODE_PRIVATE)
        val edit=sp.edit()
        edit.putInt("style", style)
        edit.apply()
    }

    fun saveSleepType(context: Context,type:Int){
        val sp = context.getSharedPreferences("name", Context.MODE_PRIVATE)
        val edit=sp.edit()
        edit.putInt("sleep_type", type)
        edit.apply()
    }

    fun getSleepType(context: Context):Int{
        val sp = context.getSharedPreferences("name", Context.MODE_PRIVATE)
        return sp.getInt("sleep_type", 0)
    }
}

