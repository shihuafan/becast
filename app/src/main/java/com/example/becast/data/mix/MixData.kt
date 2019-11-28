package com.example.becast.data.mix

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "mix_db",primaryKeys  = ["time"])
data class MixData(
    @ColumnInfo (name = "mix") var mix:String,
    @ColumnInfo (name = "time") var time:Long
)