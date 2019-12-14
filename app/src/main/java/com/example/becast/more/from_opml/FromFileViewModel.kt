package com.example.becast.more.from_opml

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData


class FromFileViewModel(private val context: Context) {

    val list : MutableList<String> = mutableListOf()
    val  listLiveData: MutableLiveData<MutableList<String>> = MutableLiveData()

    init {
        listLiveData.value=list
        object :Thread(){
            override fun run() {
                super.run()
                val projection = arrayOf(
                    MediaStore.Files.FileColumns.DATA
                )
                @SuppressLint("Recycle")
                val cursor = context.contentResolver.query(
                    Uri.parse("content://media/external/file"),
                    projection,
                    MediaStore.Files.FileColumns.DATA + " like ?",
                    arrayOf("%.opml"), null
                )
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        val dataIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                        do {
                            val path = cursor.getString(dataIndex)
                            list.add(path)
                        } while (cursor.moveToNext())
                        listLiveData.postValue(list)
                    }
                }
                cursor!!.close()
            }
        }.start()

    }

}