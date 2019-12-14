package com.example.becast.playpage.comment

import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.becast.R
import com.example.becast.data.comment.CommentData
import kotlinx.android.synthetic.main.item_note.view.*
import kotlinx.android.synthetic.main.popup_share.view.*


class CommentAdapter (private val context: Context, private val mData : MutableList<CommentData>, private val handler: Handler) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    private lateinit var v:View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        v = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = mData.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textNote.text=mData[position].comment
//        @SuppressLint("SetTextI18n")
//        holder.textTime.text=mData[position].startTime.toString()+"-"+mData[position].endTime.toString()
        holder.layoutNote.setOnLongClickListener {
            val view = LayoutInflater.from(context).inflate(R.layout.popup_share, null, false)
            val popWindow = PopupWindow(
                view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true
            )
            popWindow.showAsDropDown(holder.layoutNote,-300,20)

            view.btn_note_edit.setOnClickListener {
                val msg=Message()
                msg.obj=position
                msg.what=0x001
                handler.sendMessage(msg)
                popWindow.dismiss()
            }
            view.btn_note_delete.setOnClickListener {
                val msg=Message()
                msg.obj=position
                msg.what=0x002
                handler.sendMessage(msg)
                popWindow.dismiss()
            }
            view.btn_note_share.setOnClickListener {
                val msg=Message()
                msg.obj=position
                msg.what=0x003
                handler.sendMessage(msg)
                popWindow.dismiss()
            }
            false
        }
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val layoutNote: LinearLayout =view.layout_note
        val textNote:TextView=view.text_note
        val textTime:TextView=view.text_note_time
    }

}

