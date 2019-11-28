package com.example.becast.data.radioDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RadioData::class], version = 1)
abstract class RadioDatabase : RoomDatabase() {
    private var INSTANCE: RadioDatabase? = null
    private val sLock = Any()

    abstract fun radioDao(): RadioDao
}