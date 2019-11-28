package com.example.becast.playpage.share

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.becast.data.comment.CommentData
import com.example.becast.data.comment.CommentDatabase
import com.example.becast.data.comment.CommentDatabaseHelper

class ShareViewModel(private val context: Context,private val radioUri:String,private val rssUri:String){

    val list:MutableList<CommentData> = mutableListOf()
    val listLiveData: MutableLiveData<MutableList<CommentData>> = MutableLiveData()

    init{
        listLiveData.value=list
        object : Thread(){
            override fun run() {
                super.run()
                val db = CommentDatabaseHelper.getDb(context)
                val mDao=db.commentDao()
                list.addAll(mDao.getAll(radioUri))
                listLiveData.postValue(list)
                CommentDatabaseHelper.closeDb()
            }
        }.start()
    }

    fun delete(position:Int){
        object : Thread(){
            override fun run() {
                super.run()
                val db = CommentDatabaseHelper.getDb(context)
                val mDao=db.commentDao()
                mDao.delete(list.removeAt(position))
                listLiveData.postValue(list)
                CommentDatabaseHelper.closeDb()
            }
        }.start()
    }

    fun changeNote(text:String,position:Int){
        list[position].comment=text
        listLiveData.value=list
        object : Thread(){
            override fun run() {
                super.run()
                val db = CommentDatabaseHelper.getDb(context)
                val mDao=db.commentDao()
                mDao.updateItem(list[position])
                CommentDatabaseHelper.closeDb()
            }
        }.start()

    }

    fun addNote(text:String,startTime:Long,endTime:Long){
        list.add(CommentData(text,System.currentTimeMillis(),radioUri,rssUri,startTime,endTime))
        listLiveData.value=list
        object : Thread(){
            override fun run() {
                super.run()
                val db = CommentDatabaseHelper.getDb(context)
                val mDao=db.commentDao()
                mDao.insert(list[list.size-1])
                CommentDatabaseHelper.closeDb()
            }
        }.start()
    }


}