package com.example.becast.data.mix

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [MixData::class], version = 1)
abstract class MixDatabase : RoomDatabase() {
    private var INSTANCE: MixDatabase? = null
    private val sLock = Any()

    abstract fun mixDao(): MixDao
}