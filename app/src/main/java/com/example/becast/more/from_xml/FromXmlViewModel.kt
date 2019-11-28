package com.example.becast.more.from_xml


import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.Xml
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.becast.data.radioDb.RadioData
import com.example.becast.data.radioDb.RadioDatabase
import com.example.becast.data.radioDb.RadioDatabaseHelper
import com.example.becast.data.rss.RssData
import com.example.becast.data.rss.RssDatabaseHelper
import com.example.becast.unit.data.rssDB.RssDatabase
import okhttp3.*
import org.xmlpull.v1.XmlPullParser
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


class FromXmlViewModel(val context: Context, url:String,private val handler: Handler) {

    private lateinit var rssData: RssData
    val rssDataLiveData : MutableLiveData<RssData> = MutableLiveData()
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

        rssData = RssData("","","","","","","","")
        var radioData = RadioData("","","","","","",0L,"",rssData.rssUri,rssData.title,0L,0L,0L,0,0L)
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
                        radioData= RadioData("","","",rssData.imageUri,"","",0L,"",rssData.rssUri,rssData.title,0L,0L,0L,0,0L)
                        flag=false
                    }
                    if(flag){
                        when(parser.name){
                            "title"->rssData.title=parser.nextText()
                            "link"->rssData.link=parser.nextText()
                            "pubDate"->rssData.pubDate=parser.nextText()
                            "image"->rssData.imageUri=parser.getAttributeValue(null,"href")
                            "description"->rssData.description=parser.nextText()
                            "author"->rssData.author=parser.nextText()
                            "language"->rssData.language=parser.nextText()
                        }
                    }else
                    {
                        when(parser.name){
                            "title"->radioData.title=parser.nextText()
                            "duration"->radioData.duration=parser.nextText()
                            "enclosure"->radioData.radioUri=parser.getAttributeValue(null,"url")
                            "image"->radioData.imageUri=parser.getAttributeValue(null,"href")
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
                        rssDataLiveData.postValue(rssData)
                        radioListLiveData.postValue(radioList)
                    }
                }
            }
            eventType = parser.next()
        }
        if(radioListCache.size<50){
            radioList.clear()
            radioList.addAll(radioListCache)
            rssDataLiveData.postValue(rssData)
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
                    subscribeRadio()
                    subscribeRss()
                    handler.sendEmptyMessage(0x000)
                }
                catch(e:Exception){
                    handler.sendEmptyMessage(0x001)
                }
            }
        }.start()
    }

    fun subscribeRss(){
        val db = RssDatabaseHelper.getDb(context)
        val mDao=db.rssDao()
        try{
            mDao.insert(rssData)
        }catch (e:Exception){
            if(!e.message!!.contains("SQLITE_CONSTRAINT_PRIMARYKEY")){
                throw e
            }
        }
        RssDatabaseHelper.closeDb()
    }

    fun subscribeRadio(){
        val db = RadioDatabaseHelper.getDb(context)
        val mDao=db.radioDao()
        for(item : RadioData in radioListCache){
            try{
                mDao.insert(item)
            }catch (e:Exception){
                if(e.message!!.contains("SQLITE_CONSTRAINT_PRIMARYKEY")){
                    continue
                }
                else{
                    throw e
                }
            }
        }
        RadioDatabaseHelper.closeDb()
    }
}


//private fun getInfoFromXml() = object : Thread() {
//    override fun run() {
//        val factory = DocumentBuilderFactory.newInstance()
//        val builder = factory.newDocumentBuilder()
//        val doc = builder.parse(url)
//
//        rssData=getRssFromXml(url,doc)
//        rssDataLiveData.postValue(rssData)
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
//private fun getRssFromXml(url:String,doc:Document): RssData {
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
//    return RssData(
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