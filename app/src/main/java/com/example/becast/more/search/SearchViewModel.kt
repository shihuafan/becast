package com.example.becast.more.search

import androidx.lifecycle.MutableLiveData
import com.example.becast.data.xml.XmlData
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class SearchViewModel(term: String) {

    val list : MutableList<XmlData> = mutableListOf()
    val  listLiveData: MutableLiveData<MutableList<XmlData>> = MutableLiveData()

    init {
        listLiveData.value=list
        val url= "https://itunes.apple.com/search?term=$term&entity=podcast"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val content=response.body()!!.string()
                readJson(content)
            }
            override fun onFailure(call: Call, e: IOException) {

            }
        })
    }

    fun readJson(json:String){
        val jsonArray = JSONObject(json).getJSONArray("results")
        for(i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.get(i) as JSONObject
            val xmlUrl=jsonObject.getString("feedUrl")
            val imageUri=jsonObject.getString("artworkUrl60")
            val title=jsonObject.getString("collectionName")
            val rssData = XmlData("",title,"",imageUri,"","",xmlUrl,"","")
            list.add(rssData)
        }
        listLiveData.postValue(list)
    }
}