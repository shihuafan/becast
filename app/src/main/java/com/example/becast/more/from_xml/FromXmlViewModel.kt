package com.example.becast.more.from_xml


import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.Log
import android.util.Xml
import androidx.lifecycle.MutableLiveData
import com.example.becast.data.UserData
import com.example.becast.data.radio.RadioData
import com.example.becast.data.radio.RadioDatabase
import com.example.becast.data.xml.XmlData
import com.example.becast.data.xml.XmlDatabase
import com.example.becast.data.xml.XmlHttpHelper
import io.reactivex.Observer
import okhttp3.*
import org.xmlpull.v1.XmlPullParser
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import io.reactivex.internal.disposables.DisposableHelper.dispose
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableHelper.dispose






class FromXmlViewModel(val context: Context,private val url:String,private val handler: Handler) {

    private lateinit var xmlData: XmlData
    val xmlDataLiveData : MutableLiveData<XmlData> = MutableLiveData()
    private val radioList : MutableList<RadioData> = mutableListOf()
    private val radioListCache : MutableList<RadioData> = mutableListOf()
    val radioListLiveData : MutableLiveData<MutableList<RadioData>> = MutableLiveData()
    @Volatile
    var flag=true

    init {
        radioListLiveData.value=radioList

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val input= response.body()!!.byteStream()
                try {
                    readXml(input)
                    input.close()
                }catch (e:Exception){
                    val msg=Message()
                    msg.what=404
                    msg.obj="xml文档解析出错"
                    handler.sendMessage(msg)
                }

            }
            override fun onFailure(call: Call, e: IOException) {
                val msg=Message()
                msg.what=404
                msg.obj="网络连接失败"
                handler.sendMessage(msg)
            }
        })
    }

    fun readXml(input: InputStream){

        xmlData = XmlData(uid=UserData.uid.toString(),xmlUrl = url)
        var radioData = RadioData()
        var flag=true
        val parser= Xml.newPullParser()
        parser.setInput(input,"UTF-8")
        var eventType=parser.eventType

        while (eventType!= XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_DOCUMENT -> {
                    // opmlList = mutableListOf()
                }
                XmlPullParser.START_TAG -> {
                    if("item"==parser.name){
                        radioData= RadioData(
                            imageUrl = xmlData.imageUrl,
                            xmlImageUrl = xmlData.imageUrl,
                            xmlUrl = xmlData.xmlUrl,
                            xmlTitle = xmlData.title)
                        flag=false
                    }
                    if(flag){
                        when(parser.name){
                            "title"->xmlData.title=parser.nextText()
                            "link"->xmlData.link=parser.nextText()
                            "pubDate"->xmlData.pubDate=parser.nextText()
                            "image"->xmlData.imageUrl=parser.getAttributeValue(null,"href")
                            "description"->xmlData.description=parser.nextText()
                            "author"->xmlData.author=parser.nextText()
                            "language"->xmlData.language=parser.nextText()
                        }
                    }else
                    {
                        when(parser.name){
                            "title"->radioData.title=parser.nextText()
                            "duration"->radioData.duration=parser.nextText()
                            "enclosure"->radioData.radioUrl=parser.getAttributeValue(null,"url")
                            "image"->radioData.imageUrl=parser.getAttributeValue(null,"href")
                            "description"->radioData.description=parser.nextText()
                            "pubDate"->radioData.pubDate=parser.nextText()
                            "link"->radioData.link=parser.nextText()
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    if ("item" == parser.name) {
                        radioData.upDate=getLongTime(radioData.pubDate)
                        radioListCache.add(radioData)
                    }
                    if(radioListCache.size==50){
                        radioList.clear()
                        radioList.addAll(radioListCache)
                        xmlDataLiveData.postValue(xmlData)
                        radioListLiveData.postValue(radioList)
                    }
                }
            }
            eventType = parser.next()
        }
        if(radioListCache.size<50){
            radioList.clear()
            radioList.addAll(radioListCache)
            xmlDataLiveData.postValue(xmlData)
            radioListLiveData.postValue(radioList)
        }


    }

    private fun getLongTime(str:String):Long{
        val sdf = SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
        return try {
            val date = sdf.parse(str)
            date.time
        } catch (e: Exception) {
            0
        }

    }

    fun getMore(){
        if(flag){
            object:Thread(){
                override fun run() {
                    super.run()
                    if(radioListCache.size-radioList.size>30){
                        radioList.addAll(radioListCache.subList(radioList.size,radioList.size+30))
                    }else{
                        radioList.addAll(radioListCache.subList(radioList.size,radioListCache.size))
                        flag=false
                    }
                    radioListLiveData.postValue(radioList)
                }
            }.start()
        }
    }

    fun subscribeAll(handler: Handler){
        object : Thread() {
            override fun run() {
                try{
                    subscribeRss()
                    subscribeRadio()
                    handler.sendEmptyMessage(0x000)
                }
                catch(e:Exception){
                    handler.sendEmptyMessage(0x001)
                }
            }
        }.start()
    }

    fun subscribeRss(){
        val db = XmlDatabase.getDb(context)
        val mDao=db.xmlDao()
        try{
            mDao.insert(xmlData)
        }catch (e:Exception){
            if(!e.message!!.contains("SQLITE_CONSTRAINT_PRIMARYKEY")){
                throw e
            }
        }
        XmlDatabase.closeDb()
        XmlHttpHelper().addToNet(xmlData)
    }

    fun subscribeRadio(){
        val db = RadioDatabase.getDb(context)
        val mDao=db.radioDao()
        mDao.insertAll(radioListCache)
//        for(item : RadioData in radioListCache){
//            try{
//                mDao.insert(item)
//            }catch (e:Exception){
//                if(e.message!!.contains("SQLITE_CONSTRAINT_PRIMARYKEY")){
//                    continue
//                }
//                else{
//                    throw e
//                }
//            }
//        }
        RadioDatabase.closeDb()
    }
}

//private fun getInfoFromXml() = object : Thread() {
//    override fun run() {
//        val factory = DocumentBuilderFactory.newInstance()
//        val builder = factory.newDocumentBuilder()
//        val doc = builder.parse(url)
//
//        rssData=getRssFromXml(url,doc)
//        xmlDataLiveData.postValue(rssData)
//        getListFromXml(url,doc)
//        if(radioListCache.size<50){
//            radioList.clear()
//            radioList.addAll(radioListCache)
//            radioListLiveData.postValue(radioList)
//        }
//    }
//}.start()
//
//private fun getListFromXml(url:String,doc:Document){
//    val node = doc.getElementsByTagName("item")
//    for (i: Int in 0 until node.length) {
//        val e = node.item(i) as Element
//
//        var title =" "
//        if (e.getElementsByTagName("title").length != 0) {
//            title = e.getElementsByTagName("title").item(0).firstChild.nodeValue
//        }
//        var duration =""
//        if (e.getElementsByTagName("itunes:duration").length != 0) {
//            duration = e.getElementsByTagName("itunes:duration").item(0).firstChild.nodeValue
//        }
//
//        //获取歌曲播放链接
//        var radioUrl=""
//        if (e.getElementsByTagName("enclosure").length != 0) {
//            val enclosure = e.getElementsByTagName("enclosure").item(0) as Element
//            radioUrl = enclosure.getAttribute("url")
//        }
//
//        var imageUrl: String = rssData.imageUri
//        if (e.getElementsByTagName("itunes:image").length != 0) {
//            val image = e.getElementsByTagName("itunes:image").item(0) as Element
//            imageUrl = image.getAttribute("href")
//        }
//
//        var description=" "
//        if (e.getElementsByTagName("description").length != 0) {
//            description = e.getElementsByTagName("description").item(0).firstChild.nodeValue
//        }
//
//        var pubDate = " "
//        if (e.getElementsByTagName("pubDate").length != 0) {
//            pubDate = e.getElementsByTagName("pubDate").item(0).firstChild.nodeValue
//        }
//
//        val update=getLongTime(pubDate)
//
//        var link =" "
//        if (e.getElementsByTagName("link").length != 0) {
//            link = e.getElementsByTagName("link").item(0).firstChild.nodeValue
//        }
//        val temp = RadioData(
//            title,
//            duration,
//            link,
//            imageUrl,
//            radioUrl,
//            pubDate,
//            update,
//            description,
//            url,
//            rssData.title,
//            0,
//            0,
//            0,
//            0
//        )
//        radioListCache.add(temp)
//        if(radioListCache.size==50){
//            radioList.clear()
//            radioList.addAll(radioListCache.subList(0,50))
//            radioListLiveData.postValue(radioList)
//        }
//    }
//}
//
//private fun getRssFromXml(url:String,doc:Document): XmlData {
//
//    var title=""
//    if(doc.getElementsByTagName("title").length != 0){
//        title = doc.getElementsByTagName("title").item(0).firstChild.nodeValue
//    }
//
//    var link=""
//    if(doc.getElementsByTagName("link").length != 0){
//        link = doc.getElementsByTagName("link").item(0).firstChild.nodeValue
//    }
//
//    var pubDate=""
//    if(doc.getElementsByTagName("pubDate").length != 0){
//        pubDate = doc.getElementsByTagName("pubDate").item(0).firstChild.nodeValue
//    }
//
//    var imageUri=""
//    if(doc.getElementsByTagName("itunes:image").length != 0){
//        val image = doc.getElementsByTagName("itunes:image").item(0) as Element
//        imageUri = image.getAttribute("href")
//    }
//
//    var description=""
//    if(doc.getElementsByTagName("description").length != 0){
//        description = doc.getElementsByTagName("description").item(0).firstChild.nodeValue
//    }
//
//    var author=""
//    if(doc.getElementsByTagName("itunes:author").length != 0){
//        author = doc.getElementsByTagName("itunes:author").item(0).firstChild.nodeValue
//    }
//
//    var language=""
//    if(doc.getElementsByTagName("language").length != 0){
//        language = doc.getElementsByTagName("language").item(0).firstChild.nodeValue
//    }
//    return XmlData(
//        title,
//        link,
//        imageUri,
//        pubDate,
//        description,
//        url,
//        author,
//        language
//    )
//}