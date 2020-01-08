package com.example.becast.login_signup.register

import android.os.Handler
import android.os.Message
import com.example.becast.data.Becast
import com.example.becast.data.UserData
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class RegisterViewModel {

    fun login(id:String,code:String,password:String,handler: Handler){
        val url= UserData.BaseUrl+"/sign_up"
        val okHttpClient = OkHttpClient()
        val formBody = FormBody.Builder()
            .add("phone", id)
            .add("password", password)
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
                        UserData.uid = dataJson.getString("uid")
                        UserData.phone = dataJson.getString("phone")
                        UserData.password = dataJson.getString("password")
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
}
