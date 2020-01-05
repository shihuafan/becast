package com.example.becast.data.radio

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "radio_db",primaryKeys  = ["radio_url","xml_url","mix"])
data class RadioData(
    @ColumnInfo (name = "title")var title:String="",
    @ColumnInfo (name = "duration")var duration:String="",
    @ColumnInfo (name = "link")var link:String="",
    @ColumnInfo (name = "image_url")var imageUrl:String="",
    @ColumnInfo (name = "xml_image_url")var xmlImageUrl:String="",
    @ColumnInfo (name = "radio_url")var radioUrl:String="",
    @ColumnInfo (name = "pub_date")var pubDate:String="",
    @ColumnInfo (name = "up_date")var upDate:Long=0,
    @ColumnInfo (name = "description")var description: String="",
    @ColumnInfo (name = "xml_url")var xmlUrl:String="",
    @ColumnInfo (name = "xml_title")var xmlTitle:String="",
    @ColumnInfo (name = "love_time")var loveTime:Long=0,
    @ColumnInfo (name = "history_time")var historyTime:Long=0,
    @ColumnInfo (name = "wait_time")var waitTime:Long=0,
    @ColumnInfo (name = "progress")var progress:Int=0,
    @ColumnInfo (name = "mix") var mix:Long=0
)