package com.example.becast.data.comment

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "comment_db",primaryKeys  = ["radio_uri","rss_uri","time"])
data class CommentData(
    @ColumnInfo (name = "comment") var comment:String,
    @ColumnInfo (name = "time") var time:Long,
    @ColumnInfo (name = "radio_uri") var radioUri:String,
    @ColumnInfo (name = "rss_uri") var rssUri:String,
    @ColumnInfo (name = "star_time") var startTime:Long,
    @ColumnInfo (name = "end_time") var endTime:Long

)