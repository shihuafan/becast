package com.example.becast.unit.data.radioDb

import androidx.room.ColumnInfo
import androidx.room.Entity
//,"radio_list","rss_uri"
@Entity(tableName = "radio",primaryKeys  = ["radio_uri","rss_uri","radio_list"])
data class RadioData(
    @ColumnInfo (name = "title") var title:String ,
    @ColumnInfo (name = "duration") var duration:String ,
    @ColumnInfo (name = "link") var link:String,
    @ColumnInfo (name = "image_uri")var imageUri:String ,
    @ColumnInfo (name = "radio_uri")var radioUri:String ,
    @ColumnInfo (name = "pub_date")var pubDate:String,
    @ColumnInfo (name = "up_date")var upDate:Long,
    @ColumnInfo (name = "description")var description: String,
    @ColumnInfo (name = "rss_uri")var rssUri:String ,
    @ColumnInfo (name = "rss_title")var rssTitle:String ,
    @ColumnInfo (name = "love_time")var loveTime:Long,
    @ColumnInfo (name = "history_time")var historyTime:Long ,
    @ColumnInfo (name = "radio_list")var radioList:Long
)