package com.example.becast.mine.ui.unit

import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.becast.R
import com.example.becast.data.radioList.RadioListData
import com.example.becast.data.radioList.RadioListDatabase
import com.example.becast.unit.data.radioDb.RadioData
import com.example.becast.mine.page.PageAdapter
import kotlinx.android.synthetic.main.dialog_add_to_folder.view.*

class AddToFolderDialog(context: Context,handler:Handler,item:RadioData) {

    init {
        val inflater: LayoutInflater = LayoutInflater.from(context)        //引用自定义dialog布局
        val view = inflater.inflate(R.layout.dialog_add_to_folder, null)

        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        builder.setCancelable(true)
        val alert = builder.create()
        alert.show()

        val db= Room.databaseBuilder(context,RadioListDatabase::class.java,"radio_list")
            .allowMainThreadQueries()
            .build()
        val mDao=db.radioListDao()
        val list=mDao.getAll() as MutableList<RadioListData>
        db.close()
        val mHandler=Handler{
            val msg= Message()
            val temp=item.copy()
            temp.radioList=(it.obj as RadioListData).id
            msg.obj=temp
            msg.what=0x101
            handler.sendMessage(msg)
            alert.dismiss()
            false
        }
        view.list_add_to_folder.layoutManager = LinearLayoutManager(context)
        view.list_add_to_folder.adapter=PageAdapter(context,list,mHandler)



    }
}