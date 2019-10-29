package com.example.becast.data.rss

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.becast.data.rss.RssData

@Dao
interface RssDao {
    @Query("SELECT * FROM rss")
    fun getAll(): List<RssData>

    @Insert
    fun insert(users: RssData?)

    @Delete
    fun delete(user: RssData)

    @Query("SELECT * FROM rss WHERE rss_uri == (:rss_uri)")
    fun getRssData(rss_uri:String):RssData

}