package com.example.becast.data.rss


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RssData::class], version = 1)
abstract class RssDatabase : RoomDatabase() {
    private var INSTANCE: RssDatabase? = null
    private val sLock = Any()

    abstract fun rssDao(): RssDao
}