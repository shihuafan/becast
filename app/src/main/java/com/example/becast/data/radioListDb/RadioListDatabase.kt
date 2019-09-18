package com.example.becast.data.radioListDb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.becast.unit.data.radioDb.RadioDao
import com.example.becast.unit.data.radioDb.RadioData

@Database(entities = [RadioListData::class], version = 1)
abstract class RadioListDatabase : RoomDatabase() {
    private var INSTANCE: RadioListDatabase? = null
    private val sLock = Any()
    abstract fun radioListDao(): RadioListDao
}