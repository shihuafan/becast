package com.example.becast.login_signup.login.login

import android.content.Context
import android.os.Handler
import android.os.Message
import com.example.becast.data.MURL
import com.example.becast.data.UserData
import okhttp3.*
import java.io.IOException
import okhttp3.RequestBody
import okhttp3.FormBody
import org.json.JSONException
import android.R.string
import org.json.JSONObject



class LoginViewModel {

    fun login(id:String,password:String,handler: Handler,context: Context){
        val url=MURL.BaseUrl+"/login"
        val formBody = FormBody.Builder()
            .add("phone", id)
            .add("password", password)
            .build()
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val dataString= response.body()!!.string()
                try {
                    val dataJson = JSONObject(dataString)
                    val msg=Message()
                    if(dataJson.getString("status") == "success"){
                        UserData.uid = dataJson.getString("uid")
                        UserData.phone = dataJson.getString("phone")
                        UserData.password = dataJson.getString("password")
                        UserData.setAll(context)
                        msg.what=0x001
                        msg.obj=dataJson.getString("status")
                    }
                    else{
                        UserData.setAll(context)
                        msg.what=0x001
                        msg.obj=dataJson.getString("status")
                    }
                    handler.sendMessage(msg)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    val msg = Message()
                    msg.what = 0x002
                    handler.sendMessage(msg)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                val msg=Message()
                msg.what=0x002
                handler.sendMessage(msg)
            }
        })
    }
}
