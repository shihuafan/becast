package com.example.becast.data.radio

import androidx.room.*


@Dao
interface RadioDao {

    @Query("SELECT  * FROM radio_db  ORDER BY up_date DESC limit (:start),(:end)")
    fun getAll(start:Int,end:Int): List<RadioData>

    @Insert
    fun insert(users: RadioData?)

    @Insert
    fun insertAll(radioList: MutableList<RadioData>)
    @Delete
    fun delete(user: RadioData)

    @Update
    fun updateItem(user: RadioData)

    @Query("SELECT * FROM radio_db WHERE love_time != 0 ORDER BY up_date DESC ")
    fun getLove():List<RadioData>

    @Query("SELECT * FROM radio_db WHERE history_time != 0 ORDER BY history_time DESC")
    fun getHistory():List<RadioData>

    @Query("SELECT * FROM radio_db WHERE wait_time !=0  ORDER BY wait_time DESC limit (:start),(:end)")
    fun getWait(start:Int,end:Int):List<RadioData>

    @Query("DELETE FROM radio_db WHERE xml_url = (:link)")
    fun deleteByChannel(link:String)

    @Query("SELECT * FROM radio_db WHERE xml_url = (:link)  ORDER BY up_date DESC limit 0,50")
    fun getByChannel(link:String):List<RadioData>

    @Query("SELECT* FROM radio_db WHERE mix = (:mix) ")
    fun getByMix(mix:Long ):List<RadioData>

}