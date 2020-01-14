package com.example.becast.login_signup.login

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Handler
import android.util.Log
import com.example.becast.data.Becast
import com.example.becast.data.UserData
import com.example.becast.data.radio.RadioData
import com.example.becast.data.radio.RadioDatabase
import com.example.becast.data.radio.RadioHttpHelper
import com.example.becast.data.xml.XmlData
import com.example.becast.data.xml.XmlDatabase
import com.example.becast.data.xml.XmlHttpHelper
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class LoginViewModel {

    fun login(id:String,password:String,handler: Handler,context: Context){
        val url= UserData.BaseUrl+"/login"

        val formBody = FormBody.Builder()
            .add("phone", id)
            .add("password", password)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()
        val call= OkHttpClient().newCall(request)
        call.enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val dataString= response.body()!!.string()
                try {
                    val dataJson = JSONObject(dataString)
                    if(dataJson.getString("status") == "success"){
                        UserData.uid = dataJson.getString("uid")
                        UserData.phone = dataJson.getString("phone")
                        UserData.password = dataJson.getString("password")
                        UserData.setAll(context)
                        getXml(context,handler)
                        getRadio(context,handler)
                        handler.sendEmptyMessage(Becast.LOGIN_SUCCESS)
                    }
                    else{
                        UserData.setAll(context)
                        handler.sendEmptyMessage(Becast.LOGIN_FAIL)
                    }
                } catch (e:Exception){
                    UserData.setAll(context)
                    handler.sendEmptyMessage(Becast.LOGIN_FAIL)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                handler.sendEmptyMessage(Becast.NET_ERROR)
            }
        })
    }

    //同步xml和radio的数据
    fun getXml(context:Context,handler: Handler){
        val observer= object : Observer<MutableList<XmlData>> {
            override fun onError(e: Throwable) {}

            override fun onSubscribe(d: Disposable) {}

            override fun onComplete() {
                handler.sendEmptyMessage(Becast.LOADING_SUCCESS)
            }

            override fun onNext(value: MutableList<XmlData>) {
                val db = XmlDatabase.getDb(context)
                val mDao=db.xmlDao()
                try {
                    mDao.insertAll(value)
                } catch (e:Exception){
                    Log.d(TAG,e.toString())
                }
                XmlDatabase.closeDb()
            }
        }

        XmlHttpHelper().getListFromNet(observer)

    }


    fun getRadio(context:Context,handler:Handler){
        val observer= object : Observer<MutableList<RadioData>> {
            override fun onError(e: Throwable) {}

            override fun onSubscribe(d: Disposable) {}

            override fun onComplete() {
                handler.sendEmptyMessage(Becast.LOADING_SUCCESS)
            }

            override fun onNext(value: MutableList<RadioData>) {
                Runnable {
                    val db = RadioDatabase.getDb(context)
                    val mDao=db.radioDao()
                    try {
                        mDao.insertAll(value)
                    }
                    catch (e:Exception){
                        Log.d(TAG,e.toString())
                    }
                    RadioDatabase.closeDb()
                }.run()
            }
        }
        RadioHttpHelper().getListFromNet(observer)
    }

}
