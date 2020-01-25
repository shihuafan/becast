package com.example.becast.login_signup.sign_up

import android.content.Context
import android.os.Handler
import com.example.becast.data.Becast
import com.example.becast.data.UserData
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class SignUpViewModel {

    fun signUp(context:Context,phone:String,captcha:String,password:String,handler: Handler){
        val url= UserData.BaseUrl+"/sign_up"
        val okHttpClient = OkHttpClient()
        val formBody = FormBody.Builder()
            .add("phone", phone)
            .add("password", password)
            .add("captcha",captcha)
            .build()
        val request = Request.Builder()
            .post(formBody)
            .url(url)
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val dataString=response.body()!!.string()
                try {
                    val dataJson = JSONObject(dataString)
                    if(dataJson.getString("status") == "success"){
                        UserData.change(context,
                            uid = dataJson.getString("uid"),
                            phone = dataJson.getString("phone"),
                            password = dataJson.getString("password"))
                        handler.sendEmptyMessage(Becast.SIGN_UP_SUCCESS)
                    }
                    else{
                        handler.sendEmptyMessage(Becast.SIGN_UP_FAIL)
                    }
                } catch (e: JSONException) {
                    handler.sendEmptyMessage(Becast.SIGN_UP_FAIL)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                handler.sendEmptyMessage(Becast.NET_ERROR)
            }
        })
    }

    fun getCaptcha(phone : String,handler: Handler){
        val url= UserData.BaseUrl+"/captcha"
        val formBody = FormBody.Builder()
            .add("phone", phone)
            .build()
        val request = Request.Builder()
            .post(formBody)
            .url(url)
            .build()
        val call=OkHttpClient().newCall(request)
            call.enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.body()!!.string()=="fail"){
                    handler.sendEmptyMessage(Becast.ALREADY_SIGN_UP)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
            }
        })
    }
}
