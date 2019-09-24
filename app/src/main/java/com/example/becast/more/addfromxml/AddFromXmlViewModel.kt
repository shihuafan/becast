package com.example.becast.more.addfromxml


import android.content.Context
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.becast.unit.data.radioDb.RadioData
import com.example.becast.unit.data.radioDb.RadioDatabase
import com.example.becast.data.rss.RssData
import com.example.becast.mine.ui.addfromxml.AddFromXmlModel
import org.w3c.dom.Element
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory


class AddFromXmlViewModel(val context: Context, val url:String) {

    val addFromXmlModel: AddFromXmlModel = AddFromXmlModel()
    val addFromXmlModelLiveData: MutableLiveData<AddFromXmlModel> = MutableLiveData()
    init {
        addFromXmlModelLiveData.value=addFromXmlModel
        getInfoFromXml()
    }

    fun subscribeAll(handler:Handler){
        val db = Room.databaseBuilder(context, RadioDatabase::class.java, "radio")
            .build()

        val mDao=db.radioDao()
        object : Thread() {
            override fun run() {
                for(item : RadioData in addFromXmlModel.list){
                    try{
                        mDao.insert(item)
                    }catch (e:Exception){}
                }
                db.close()
                handler.sendEmptyMessage(0x000)
            }
        }.start()
    }

    private fun getInfoFromXml() = object : Thread() {
        override fun run() {
            /* val url="https://getpodcast.xyz/data/ximalaya/3558668.xml" */
            addFromXmlModel.rssData=getRssFromXml(url)
            getListFromXml(addFromXmlModel.rssData,addFromXmlModel.list)
            addFromXmlModelLiveData.postValue(addFromXmlModel)
        }
    }.start()

    private fun getListFromXml(rss: RssData, list:MutableList<RadioData>){
        val url=rss.rssUri
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val doc = builder.parse(url)
        val node = doc.getElementsByTagName("item")
        for (i: Int in 0 until node.length) {

            val e = node.item(i) as Element

            var title =" "
            if (e.getElementsByTagName("title").length != 0) {
                title = e.getElementsByTagName("title").item(0).firstChild.nodeValue
            }
            var duration =""
            if (e.getElementsByTagName("itunes:duration").length != 0) {
                duration = e.getElementsByTagName("itunes:duration").item(0).firstChild.nodeValue
            }

            //获取歌曲播放链接
            var radioUrl=""
            if (e.getElementsByTagName("enclosure").length != 0) {
                val enclosure = e.getElementsByTagName("enclosure").item(0) as Element
                radioUrl = enclosure.getAttribute("url")
            }

            var imageUrl: String = rss.imageUri
            if (e.getElementsByTagName("itunes:image").length != 0) {
                val image = e.getElementsByTagName("itunes:image").item(0) as Element
                imageUrl = image.getAttribute("href")
            }

            var description=" "
            if (e.getElementsByTagName("description").length != 0) {
                description = e.getElementsByTagName("description").item(0).firstChild.nodeValue
            }

            var pubDate = " "
            if (e.getElementsByTagName("pubDate").length != 0) {
                pubDate = e.getElementsByTagName("pubDate").item(0).firstChild.nodeValue
            }

            val update=getLongTime(pubDate)

            var link =" "
            if (e.getElementsByTagName("link").length != 0) {
                link = e.getElementsByTagName("link").item(0).firstChild.nodeValue
            }
            val temp = RadioData(
                title,
                duration,
                link,
                imageUrl,
                radioUrl,
                pubDate,
                update,
                description,
                url,
                rss.title,
                0,
                0,
                0
            )
            list.add(temp)
        }
    }

    private fun getRssFromXml(url:String): RssData {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val doc = builder.parse(url)

        var title=""
        if(doc.getElementsByTagName("title").length != 0){
            title = doc.getElementsByTagName("title").item(0).firstChild.nodeValue
        }

        var link=""
        if(doc.getElementsByTagName("link").length != 0){
            link = doc.getElementsByTagName("link").item(0).firstChild.nodeValue
        }

        var pubDate=""
        if(doc.getElementsByTagName("pubDate").length != 0){
            pubDate = doc.getElementsByTagName("pubDate").item(0).firstChild.nodeValue
        }

        var imageUri=""
        if(doc.getElementsByTagName("itunes:image").length != 0){
            val image = doc.getElementsByTagName("itunes:image").item(0) as Element
            imageUri = image.getAttribute("href")
        }

        var description=""
        if(doc.getElementsByTagName("description").length != 0){
            description = doc.getElementsByTagName("description").item(0).firstChild.nodeValue
        }

        var author=""
        if(doc.getElementsByTagName("itunes:author").length != 0){
            author = doc.getElementsByTagName("itunes:author").item(0).firstChild.nodeValue
        }

        var language=""
        if(doc.getElementsByTagName("language").length != 0){
            language = doc.getElementsByTagName("language").item(0).firstChild.nodeValue
        }
        return RssData(
            1,
            title,
            link,
            imageUri,
            pubDate,
            description,
            url,
            author,
            language
        )
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

}