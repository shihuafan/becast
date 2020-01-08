package com.example.becast.more.from_opml

import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.becast.R
import com.example.becast.data.Becast

class FileAdapter (private val mData : MutableList<String>, private val handler: Handler)
    : RecyclerView.Adapter<FileAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val dot = mData[position].lastIndexOf("/")
        val name = mData[position].substring(dot + 1)
        holder.textItemName.text=name
        holder.btnItem.setOnClickListener {
            val msg=Message()
            msg.what=Becast.OPEN_OPML_FRAGMENT
            msg.obj=mData[position]
            handler.sendMessage(msg)
        }
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val textItemName:TextView= view.findViewById(R.id.text_item_name)
        val btnItem : Button = view.findViewById(R.id.btn_item_file)
//        val imageItemShow: ImageView = view.findViewById(R.id.image_item_wait)
//        val textItemTitle:TextView= view.findViewById(R.id.text_item_wait_name)
//        val textItemDate:TextView=view.findViewById(R.id.text_item_wait_update)
//        val btnWaitCancel:Button=view.findViewById(R.id.btn_item_wait_cancel)
//        val itemWait: SwipeRevealLayout =view.findViewById(R.id.item_wait)

    }
}

