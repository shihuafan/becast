package com.example.becast.data.xml

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface XmlDao {
    @Query("SELECT * FROM xml_db")
    fun getAll(): List<XmlData>

    @Insert
    fun insert(users: XmlData?)

    @Delete
    fun delete(user: XmlData)

    @Query("SELECT * FROM xml_db WHERE xml_url == (:xml_url)")
    fun getRssData(xml_url:String):XmlData


}