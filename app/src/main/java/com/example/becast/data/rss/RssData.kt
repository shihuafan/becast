package com.example.becast.data.rss

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rss")
data class RssData(
    @PrimaryKey(autoGenerate = true) var uid:Int,
    @ColumnInfo(name = "title")var title:String,
    @ColumnInfo(name = "link")var link:String,
    @ColumnInfo(name = "image_uri")var imageUri:String,
    @ColumnInfo(name = "pub_date")var pubDate:String,
    @ColumnInfo(name = "description")var description: String,
    @ColumnInfo(name = "rss_uri")var rssUri:String,
    @ColumnInfo(name = "author")var author:String,
    @ColumnInfo(name = "language")var language:String

)