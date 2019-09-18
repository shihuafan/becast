package com.example.becast.unit.data.rssDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.becast.data.rssDB.RssData

@Dao
interface RssDao {
    @Query("SELECT * FROM rss")
    fun getAll(): List<RssData>

    @Insert
    fun insert(users: RssData?)

    @Delete
    fun delete(user: RssData)

}