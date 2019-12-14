package com.example.becast.data.xml


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [XmlData::class], version = 1)
abstract class XmlDatabase : RoomDatabase() {
    private var INSTANCE: XmlDatabase? = null
    private val sLock = Any()

    abstract fun xmlDao(): XmlDao
}