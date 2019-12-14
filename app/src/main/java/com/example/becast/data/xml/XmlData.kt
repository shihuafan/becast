package com.example.becast.data.xml

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "xml_db",primaryKeys  = ["link"])
data class XmlData(
    @ColumnInfo(name = "title")var title:String="",
    @ColumnInfo(name = "link")var link:String="",
    @ColumnInfo(name = "image_url")var imageUrl:String="",
    @ColumnInfo(name = "pub_date")var pubDate:String="",
    @ColumnInfo(name = "description")var description: String="",
    @ColumnInfo(name = "xml_url")var xmlUrl:String="",
    @ColumnInfo(name = "author")var author:String="",
    @ColumnInfo(name = "language")var language:String=""

)