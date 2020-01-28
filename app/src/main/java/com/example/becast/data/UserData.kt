package com.example.becast.data

import android.content.Context
import com.example.becast.R
import com.example.becast.data.radio.RadioData
import com.example.becast.data.radio.RadioDatabase
import com.example.becast.data.xml.XmlDatabase
import org.greenrobot.eventbus.EventBus
import java.time.format.DecimalStyle

object UserData {
    var uid:String?=null
    var name:String?=null
    var image:String?=null
    var phone:String?=null
    var password:String?=null
    const val version="0.0.2"

    var style:Int= R.style.AppTheme
    var delay:Int=0
//    const val BaseUrl="http://192.168.1.80:8080"
    const val BaseUrl="http://104.168.140.16:8080"
    var xmlSum=0
    var radioSum=0
    var timeSum=0


    fun getAll(context:Context) : UserData {
        val sp = context.getSharedPreferences("name", Context.MODE_PRIVATE)
        uid =sp.getString("uid", null)
        name =sp.getString("name", uid)
        image = sp.getString("image", null)
        style =sp.getInt("style",R.style.AppTheme)
        object:Thread(){
            override fun run() {
                super.run()
                val db= XmlDatabase.getDb(context)
                val xmlDao=db.xmlDao()
                xmlSum=xmlDao.getSum()
            }
        }.start()
        return this
    }

    fun clearAll(context: Context){
        val sp = context.getSharedPreferences("name", Context.MODE_PRIVATE)
        val edit=sp.edit()
        uid = null
        password = null
        name = null
        image = null
        edit.putString("uid", null)
        edit.putString("password",null)
        edit.putString("name", null)
        edit.putString("image", null)
        edit.apply()
    }

    fun change(context: Context,
               uid:String?=this.uid,
               name:String?=this.name,
               password:String?=this.password,
               phone:String?=this.phone,
               image:String?=this.image,
               style: Boolean=false){

        val sp = context.getSharedPreferences("name", Context.MODE_PRIVATE)
        val edit=sp.edit()
        if(uid!=this.uid){
            this.uid=uid
            edit.putString("uid",this.uid)
        }
        if(name!=this.name){
            this.name=name
            edit.putString("name",this.name)
        }
        if(password!=this.password){
            this.password=password
            edit.putString("password",this.password)
        }
        if(phone!=this.phone){
            this.phone=phone
            edit.putString("phone",this.phone)
        }
        if(image!=this.image){
            this.image=image
            edit.putString("image",this.image)
        }
        if(style){
            this.style = if(this.style ==R.style.AppTheme){
                R.style.DarkTheme
            }else{
                R.style.AppTheme
            }
            edit.putInt("style", this.style)
        }
        edit.apply()
        
    }
}

