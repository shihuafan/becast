package com.example.becast.nav.setting

import android.os.Handler
import com.example.becast.data.Becast
import com.example.becast.data.UserData
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class SettingViewModel {

    fun getLatestVersion(handler: Handler){
        val url=UserData.BaseUrl+"/version"
        val request=Request.Builder()
            .get()
            .url(url)
            .build()
        val call=OkHttpClient().newCall(request)
        call.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                handler.sendEmptyMessage(Becast.NET_ERROR)
            }
            override fun onResponse(call: Call, response: Response) {
                val result = JSONObject(response.body()?.string())
                if(UserData.version==result.getString("version")){
                    handler.sendEmptyMessage(Becast.IS_LATEST)
                }else{
                    handler.sendEmptyMessage(Becast.NOT_LATEST)
                }
            }
        })
    }
    
}