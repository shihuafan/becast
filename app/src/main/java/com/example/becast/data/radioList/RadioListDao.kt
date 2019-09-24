package com.example.becast.data.radioList

import androidx.room.*

@Dao
interface RadioListDao {
    @Query("SELECT  * FROM radio_list ORDER BY id DESC")
    fun getAll(): List<RadioListData>

    @Insert
    fun insert(item: RadioListData)

    @Delete
    fun delete(item: RadioListData)

    @Update
    fun updateItem(item: RadioListData)


}