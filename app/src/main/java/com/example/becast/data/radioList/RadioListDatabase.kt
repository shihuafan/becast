package com.example.becast.data.radioList

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RadioListData::class], version = 1)
abstract class RadioListDatabase : RoomDatabase() {
    private var INSTANCE: RadioListDatabase? = null
    private val sLock = Any()
    abstract fun radioListDao(): RadioListDao
}