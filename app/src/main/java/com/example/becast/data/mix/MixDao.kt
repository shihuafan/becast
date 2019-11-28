package com.example.becast.data.mix

import androidx.room.*


@Dao
interface MixDao {

    @Query("SELECT  * FROM mix_db")
    fun getAll(): List<MixData>

    @Insert
    fun insert(mix: MixData?)

    @Delete
    fun delete(mix: MixData)

    @Update
    fun updateItem(mix: MixData)


}