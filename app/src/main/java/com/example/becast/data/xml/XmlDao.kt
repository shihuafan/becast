package com.example.becast.data.xml

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.becast.data.radio.RadioData

@Dao
interface XmlDao {

    @Query("SELECT * FROM xml_db")
    fun getAll(): List<XmlData>

    @Insert
    fun insert(xmlData: XmlData?)

    @Insert
    fun insertAll(radioList: MutableList<XmlData>)

    @Delete
    fun delete(xmlData: XmlData)

    @Query("SELECT * FROM xml_db WHERE xml_url == (:xml_url)")
    fun getXmlData(xml_url:String):XmlData

    @Query("DELETE FROM xml_db")
    fun deleteAll()

    @Query("select count(*) from xml_db")
    fun getSum():Int
}