package com.example.becast.service

import android.util.Xml
import com.example.becast.data.radioDb.RadioData
import com.example.becast.data.rss.RssData
import okhttp3.*
import org.xmlpull.v1.XmlPullParser
import java.io.IOException
import java.io.InputStream

class sss {
     fun getInfo(url:String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val input= response.body()!!.byteStream()
                readXml(input)
                input.close()
            }

            override fun onFailure(call: Call, e: IOException) {}

        })
    }

    fun readXml(input: InputStream){

        val parser= Xml.newPullParser()
        parser.setInput(input,"UTF-8")
        var eventType=parser.eventType

        while (eventType!= XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_DOCUMENT -> {
                    // opmlList = mutableListOf()
                }
                XmlPullParser.START_TAG -> {
                    print(parser.name)
                }
                XmlPullParser.END_TAG -> {
                }
            }
            eventType = parser.next()
        }
    }
}