package com.example.becast.unit.data.radioDb

import androidx.room.*


@Dao
interface RadioDao {
    @Query("SELECT  * FROM radio WHERE radio_list == 0 ORDER BY up_date DESC")
    fun getAll(): List<RadioData>

    @Insert
    fun insert(users: RadioData?)

    @Delete
    fun delete(user: RadioData)

    @Update
    fun updateItem(user: RadioData)

    @Query("SELECT * FROM radio WHERE love_time != 0 AND radio_list == 0 ORDER BY up_date DESC")
    fun getLove():List<RadioData>

    @Query("SELECT * FROM radio WHERE history_time != 0 AND radio_list == 0 ORDER BY history_time DESC")
    fun getHistory():List<RadioData>

    @Query("SELECT * FROM radio WHERE radio_list == (:id) ORDER BY up_date DESC")
    fun getRadioList(id:Long):List<RadioData>

}