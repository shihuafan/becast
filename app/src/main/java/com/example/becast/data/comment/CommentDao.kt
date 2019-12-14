package com.example.becast.data.comment

import androidx.room.*


@Dao
interface CommentDao {

    @Query("SELECT  * FROM comment_db WHERE radio_url = (:radioUrl)")
    fun getAll(radioUrl:String): List<CommentData>

    @Insert
    fun insert(comment: CommentData?)

    @Delete
    fun delete(comment: CommentData)

    @Update
    fun updateItem(comment: CommentData)


}