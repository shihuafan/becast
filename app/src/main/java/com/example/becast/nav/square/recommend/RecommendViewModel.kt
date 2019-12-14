package com.example.becast.nav.square.recommend

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class RecommendViewModel {

    private var recommend : RecommendData =
        RecommendData()
    val recommendModelLiveData: MutableLiveData<RecommendData> = MutableLiveData()

    init {
        recommendModelLiveData.value=recommend
    }

    fun getJson(url:String,handler:Handler){
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url("https://raw.githubusercontent.com/shihuafan/cubing/master/shf1211.json")
            .build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                handler.sendEmptyMessage(0x404)
            }
            override fun onResponse(call: Call, response: Response) {
                recommend= Gson().fromJson(response.body()!!.string(), RecommendData::class.java)
                recommendModelLiveData.postValue(recommend)
            }
        })
    }
}