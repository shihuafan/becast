package com.example.becast.data

import android.content.Context
import com.example.becast.R

object UserData {
    var uid:String?="0294374072"
    var name:String?=""
    var image:String?=""
    var phone:String?=""
    var password:String?=""
    var isLogin:Boolean=false
    var style:Int= R.style.AppTheme
    var delay:Int=0
    var downloadWhenLove:Boolean=false
    var downloadWhenWait:Boolean=false
    val BaseUrl="http://104.168.140.16:8080"


    fun getAll(context:Context) : UserData {
        val sp = context.getSharedPreferences("name", Context.MODE_PRIVATE)
        uid =sp.getString("uid", "")
        name =sp.getString("name", "")
        image =sp.getString("image", "")
        isLogin =sp.getBoolean("is_login",false)
        style =sp.getInt("style",R.style.DarkTheme)
        downloadWhenWait=sp.getBoolean("download_when_wait", false)
        downloadWhenLove=sp.getBoolean("download_when_love", false)
        return this
    }

    fun setAll(context:Context,uid:String?=this.uid) {
        val sp = context.getSharedPreferences("name", Context.MODE_PRIVATE)
        val edit=sp.edit()

        edit.putString("uid", uid)
        edit.putString("name", "拉普兰德")
        edit.putString("image", "http://raw.yiyoushuo.com/UGC/21f4bb93-9344-4a89-923c-6f9ce4a1b58b.jpg?x-oss-process=image/format,jpg")
        edit.putBoolean("is_login",true)
        edit.apply()
    }

    fun clearAll(context: Context){
        val sp = context.getSharedPreferences("name", Context.MODE_PRIVATE)
        val edit=sp.edit()
        uid = ""
        password = ""
        name = ""
        image = ""
        isLogin =false
        edit.putString("uid", "")
        edit.putString("password","")
        edit.putString("name", "")
        edit.putString("image", "")
        edit.putBoolean("is_login",false)
        edit.apply()
    }

    fun changeStyle(context: Context){
        style = if(style ==R.style.AppTheme){
            R.style.DarkTheme
        }else{
            R.style.AppTheme
        }
        val sp = context.getSharedPreferences("name", Context.MODE_PRIVATE)
        val edit=sp.edit()
        edit.putInt("style", style)
        edit.apply()
    }

    fun changeDownloadWhenLove(context:Context,flag:Boolean){
        this.downloadWhenLove=flag
        val sp = context.getSharedPreferences("name", Context.MODE_PRIVATE)
        val edit=sp.edit()
        edit.putBoolean("download_when_love", this.downloadWhenLove)
        edit.apply()
    }

    fun changeDownloadWhenWait(context:Context,flag:Boolean){
        this.downloadWhenWait=flag
        val sp = context.getSharedPreferences("name", Context.MODE_PRIVATE)
        val edit=sp.edit()
        edit.putBoolean("download_when_wait", this.downloadWhenWait)
        edit.apply()

    }

}

