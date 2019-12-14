package com.example.becast.data.comment

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [CommentData::class], version = 1)
abstract class CommentDatabase : RoomDatabase() {
    private var INSTANCE: CommentDatabase? = null
    private val sLock = Any()

    abstract fun commentDao(): CommentDao
}