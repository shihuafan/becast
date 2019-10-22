package com.example.becast.nav.user.login

import android.content.Context
import android.os.Handler
import android.os.Message
import com.example.becast.data.user.UserData
import okhttp3.*
import java.io.IOException



class LoginViewModel {

    fun login(id:String,password:String,handler: Handler,context: Context){
        val url="http://www.baidu.com"
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                UserData.setAll(context)
                val msg=Message()
                msg.what=0x001
                handler.sendMessage(msg)
            }
            override fun onFailure(call: Call, e: IOException) {
                val msg=Message()
                msg.what=0x002
                handler.sendMessage(msg)
            }
        })
    }
}
