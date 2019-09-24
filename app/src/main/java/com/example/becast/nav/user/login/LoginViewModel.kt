package com.example.becast.nav.user.login

import android.os.Handler
import android.os.Message
import okhttp3.*
import java.io.IOException



class LoginViewModel {

    fun login(id:String,password:String,handler: Handler){
        val url="http://www.baidu.com"
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
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
