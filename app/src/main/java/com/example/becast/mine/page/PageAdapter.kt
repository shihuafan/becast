package com.example.becast.mine.page

import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.becast.R
import com.example.becast.data.radioList.RadioListData


class PageAdapter (private val context: Context, private val mData : MutableList<RadioListData>, private val handler:Handler) : RecyclerView.Adapter<PageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_page_list, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context)
            .asBitmap()
            .load(R.drawable.list_icon)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(10)).circleCrop())
            .into(holder.imageShow)

        holder.textName.text=mData[position].name
        holder.btnPage.setOnClickListener {
            val msg=Message()
            msg.obj=mData[position]
            handler.sendMessage(msg)
        }

    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){

        val imageShow: ImageView = view.findViewById(R.id.image_item_page)
        val textName:TextView= view.findViewById(R.id.text_item_page)
        val btnPage:Button=view.findViewById(R.id.btn_item_page)

    }

}

