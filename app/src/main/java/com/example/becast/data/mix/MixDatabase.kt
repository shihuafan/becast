package com.example.becast.data.mix

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.becast.data.radioDb.RadioDao
import com.example.becast.data.radioDb.RadioData
import com.example.becast.data.radioDb.RadioDatabase


@Database(entities = [MixData::class], version = 1)
abstract class MixDatabase : RoomDatabase() {
    private var INSTANCE: MixDatabase? = null
    private val sLock = Any()

    abstract fun mixDao(): MixDao
}