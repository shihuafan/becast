package com.example.becast.nav.square

import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.becast.R


class SquareAdapter (private val context: Context,
                     private val mData : MutableList<String>,
                     private val handler: Handler)
    : RecyclerView.Adapter<SquareAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_square, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context)
            .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_" +
                    "10000&sec=1576074107752&di=b26b97201da5dccd7b91804f7e41ebef&imgtype=0&src=h" +
                    "ttp%3A%2F%2Fimg.nga.178.com%2Fattachments%2Fmon_201901%2F30%2F-7Q5-jnepXeZ3uT3cS1hc-u0.png")
            .apply(RequestOptions.overrideOf(400,300))
            .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
            .into(holder.imageItemShow)

        holder.btnItem.setOnClickListener {
            val msg= Message()
            msg.what=0x003
            msg.obj=mData[position]
            handler.sendMessage(msg)
        }

    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val imageItemShow: ImageView = view.findViewById(R.id.image_item_square)
        val btnItem : Button = view.findViewById(R.id.btn_item_square)
    }
}

