package com.example.becast.mine.ui.unit

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.example.becast.R
import com.example.becast.unit.data.radioDb.RadioData
import kotlinx.android.synthetic.main.dialog_datil.view.*

class DetailDialog( item : RadioData, context: Context, handler: Handler) {

    init{

        val inflater: LayoutInflater = LayoutInflater.from(context)        //引用自定义dialog布局
        val view = inflater.inflate(R.layout.dialog_datil, null)

        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        builder.setCancelable(true)
        val alert = builder.create()
        alert.show()

        context.let {
            Glide.with(it)
                .load(Uri.parse(item.imageUri))
                .into(view.image_detail)
        }
        if(item.loveTime > 0){
            view.btn_detail_love.setBackgroundResource(R.drawable.heart_fill)
        }
        else {
            view.btn_detail_love.setBackgroundResource(R.drawable.heart)
        }
        view.text_dialog_title.text=("标题："+item.title)
        view.text_dialog_author.text=("作者:"+item.rssTitle)
        view.text_dialog_update.text=("更新："+item.pubDate)
        view.text_dialog_link.text=("节目链接："+item.imageUri)

        view.btn_detail_folder_add.setOnClickListener {
            AddToFolderDialog(context, handler, item)
            alert.dismiss()
        }

        view.btn_detail_love.setOnClickListener{
            val msg=Message()
            msg.what=0x102
            msg.obj=item
            handler.sendMessage(msg)
            alert.dismiss()
//            if(item.loveTime > 0){
//                view.btn_detail_love.setBackgroundResource(R.drawable.heart)
//            }
//            else{
//                view.btn_detail_love.setBackgroundResource(R.drawable.heart_fill)
//            }
        }

        view.btn_detail_play.setOnClickListener{
            val msg=Message()
            msg.what=0x103
            msg.obj=item
            handler.sendMessage(msg)
            alert.dismiss()
        }

        view.btn_detail_dismiss.setOnClickListener{
            alert.dismiss()
        }

    }
}