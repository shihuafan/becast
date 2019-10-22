package com.example.becast.data.user

import android.content.Context


object UserData {
    var uid:Int=0
    var name:String?=" "
    var image:String?=" "
    var isLogin:Boolean=false

    fun getAll(context:Context) :UserData{
        val sp = context.getSharedPreferences("name", Context.MODE_PRIVATE)
        uid=sp.getInt("uid", 0)
        name=sp.getString("name", "")
        image=sp.getString("image", "")
        isLogin=sp.getBoolean("is_login",false)
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
}

