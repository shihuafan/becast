package com.example.becast.playpage.comment

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.becast.data.UserData
import com.example.becast.data.comment.CommentData
import com.example.becast.data.comment.CommentHttpHelper
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.FormBody

class CommentViewModel(private val commentData: CommentData){

    val list:MutableList<CommentData> = mutableListOf()
    val listLiveData: MutableLiveData<MutableList<CommentData>> = MutableLiveData()

    init{
        listLiveData.value=list
    }

    fun getComment(){
        val observer= object : Observer<MutableList<CommentData>> {
            override fun onError(e: Throwable) {}

            override fun onSubscribe(d: Disposable) {}

            override fun onComplete() { }

            override fun onNext(value: MutableList<CommentData>) {
                list.clear()
                list.addAll(value)
                listLiveData.postValue(list)
            }
        }
        CommentHttpHelper().getComment(commentData.xmlUrl,commentData.radioUrl,observer)
    }

    fun delete(position:Int){
        CommentHttpHelper().delComment(list.removeAt(position))
        listLiveData.value=list
    }

    fun changeNote(text:String,position:Int){
        list[position].comment=text
        listLiveData.value=list
    }

    fun addNote(commentData: CommentData){
        val temp=CommentData(
            uid = UserData.uid,
            comment = commentData.comment,
            createTime = commentData.createTime,
            radioUrl = commentData.radioUrl,
            xmlUrl = commentData.xmlUrl,
            startTime = commentData.startTime,
            endTime = commentData.endTime,
            xmlTitle = commentData.xmlTitle,
            title = commentData.title
        )
        list.add(temp)
        listLiveData.value=list
        CommentHttpHelper().addToNet(temp)
    }

    fun timeToStr(time: Int): String {
        return if (time % 60 < 10) {
            (time / 60).toString() + ":0" + time % 60
        } else {
            (time / 60).toString() + ":" + time % 60
        }
    }
}