package com.example.becast.unit.data.rssDB


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.becast.data.rssDB.RssData

@Database(entities = [RssData::class], version = 1)
abstract class RssDatabase : RoomDatabase() {
    private var INSTANCE: RssDatabase? = null
    private val sLock = Any()

    abstract fun rssDao(): RssDao
}