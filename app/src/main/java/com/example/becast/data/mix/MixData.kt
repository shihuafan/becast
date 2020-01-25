package com.example.becast.data.mix

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "mix_db",primaryKeys  = ["time"])
data class MixData(
    @ColumnInfo (name = "uid") var uid:String?=null,
    @ColumnInfo (name = "mix_id") var mixId:String?=null,
    @ColumnInfo (name = "mix") var mix:String?=null,
    @ColumnInfo (name = "time") var time:Long=0,
    @ColumnInfo (name = "image_url") var imageUrl:String?=null
)