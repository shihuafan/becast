package com.example.becast.data.radioDb

import androidx.room.*


@Dao
interface RadioDao {
    @Query("SELECT  * FROM radio  ORDER BY up_date DESC limit (:start),(:end)")
    fun getAll(start:Int,end:Int): List<RadioData>

    @Insert
    fun insert(users: RadioData?)

    @Delete
    fun delete(user: RadioData)

    @Update
    fun updateItem(user: RadioData)

    @Query("SELECT * FROM radio WHERE love_time != 0 ORDER BY up_date DESC ")
    fun getLove():List<RadioData>

    @Query("SELECT * FROM radio WHERE history_time != 0 ORDER BY history_time DESC")
    fun getHistory():List<RadioData>

    @Query("SELECT * FROM radio WHERE wait_time !=0  ORDER BY wait_time DESC limit (:start),(:end)")
    fun getWait(start:Int,end:Int):List<RadioData>

    @Query("DELETE FROM radio WHERE rss_uri = (:link)")
    fun deleteByChannel(link:String)

    @Query("SELECT * FROM radio WHERE rss_uri = (:link)  ORDER BY up_date DESC ")
    fun getByChannel(link:String):List<RadioData>

}