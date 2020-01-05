package com.example.becast.login_signup.login.register

import android.os.Handler
import android.os.Message
import com.example.becast.data.MURL
import com.example.becast.data.UserData
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class RegisterViewModel {

    fun login(id:String,code:String,password:String,handler: Handler){
        val url= MURL.BaseUrl+"/sign_up"
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
                    val msg=Message()
                    if(dataJson.getString("status") == "success"){
                        UserData.uid = dataJson.getString("uid")
                        UserData.phone = dataJson.getString("phone")
                        UserData.password = dataJson.getString("password")
                        msg.what=0x001
                        msg.obj=dataJson.getString("status")
                    }
                    else{
                        msg.what=0x002
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
