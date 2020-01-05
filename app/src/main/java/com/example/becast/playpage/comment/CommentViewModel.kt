package com.example.becast.playpage.comment

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.becast.data.comment.CommentData
import com.example.becast.data.comment.CommentDatabase

class CommentViewModel{

    val list:MutableList<CommentData> = mutableListOf()
    val listLiveData: MutableLiveData<MutableList<CommentData>> = MutableLiveData()

    init{
        listLiveData.value=list
    }

    fun getComment(context:Context,radioUrl:String){
        object : Thread(){
            override fun run() {
                super.run()
                val db = CommentDatabase.getDb(context)
                val mDao=db.commentDao()
                list.addAll(mDao.getAll(radioUrl))
                listLiveData.postValue(list)
                CommentDatabase.closeDb()
            }
        }.start()
    }

    fun delete(context:Context,position:Int){
        object : Thread(){
            override fun run() {
                super.run()
                val db = CommentDatabase.getDb(context)
                val mDao=db.commentDao()
                mDao.delete(list.removeAt(position))
                listLiveData.postValue(list)
                CommentDatabase.closeDb()
            }
        }.start()
    }

    fun changeNote(context:Context,text:String,position:Int){
        list[position].comment=text
        listLiveData.value=list
        object : Thread(){
            override fun run() {
                super.run()
                val db = CommentDatabase.getDb(context)
                val mDao=db.commentDao()
                mDao.updateItem(list[position])
                CommentDatabase.closeDb()
            }
        }.start()

    }

    fun addNote(context:Context,text:String,startTime:Long,endTime:Long,radioUrl:String,xmlUrl:String){
        list.add(CommentData(text,System.currentTimeMillis(),radioUrl,xmlUrl,startTime,endTime))
        listLiveData.value=list
        object : Thread(){
            override fun run() {
                super.run()
                val db = CommentDatabase.getDb(context)
                val mDao=db.commentDao()
                mDao.insert(list[list.size-1])
                CommentDatabase.closeDb()
            }
        }.start()
    }


}