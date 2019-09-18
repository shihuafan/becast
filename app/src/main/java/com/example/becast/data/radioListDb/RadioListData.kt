package com.example.becast.data.radioListDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "radio_list")
data class RadioListData(
    @PrimaryKey var id:Long,
    @ColumnInfo var name:String,
    @ColumnInfo var image:String
)