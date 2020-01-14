package com.example.becast.channel

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.becast.R
import com.example.becast.data.Becast
import com.example.becast.data.radio.RadioData
import com.example.becast.data.xml.XmlData
import com.example.becast.main.page.RadioAdapter
import com.example.becast.playpage.detail.DetailFragment
import com.example.becast.service.RadioService
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.frag_channel.*
import kotlinx.android.synthetic.main.frag_channel.view.*

@SuppressLint("NewApi")
class ChannelFragment(private val xmlData: XmlData, private val mBinder: RadioService.LocalBinder):Fragment(), View.OnClickListener,
    View.OnScrollChangeListener {

    private lateinit var v:View
    private lateinit var channelViewModel:ChannelViewModel

    private val mHandler=Handler{
        when(it.what){
            Becast.OPEN_DETAIL_FRAGMENT->{
                fragmentManager!!.beginTransaction()
                    .hide(this)
                    .replace(R.id.layout_main_all,
                        DetailFragment(
                            it.obj as RadioData,
                            mBinder,
                            true
                        )
                    )
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
            }
        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_channel, container, false)
        channelViewModel= context?.let { ChannelViewModel(it,xmlData) }!!

        v.list_channel.layoutManager = LinearLayoutManager(context)
        v.list_channel.adapter = context?.let {
            RadioAdapter(it,channelViewModel.channelLiveData.value!!, mHandler)
        }

        //更新列表
        channelViewModel.channelLiveData.observe(this, Observer{
            v.list_channel.adapter?.notifyDataSetChanged()
        })

        v.text_channel_head_title.text=xmlData.title
        v.text_channel_title.text=xmlData.title
        v.text_channel_update.text=xmlData.pubDate
        v.text_channel_describe.text=xmlData.description

        v.scrollView_channel.setOnScrollChangeListener(this)
        v.btn_channel_back.setOnClickListener(this)
        v.btn_channel_subscribe.setOnClickListener(this)

        Glide.with(context!!)
            .load(xmlData.imageUrl)
            .apply(RequestOptions.overrideOf(100,100))
            .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
            .into(v.image_channel_show)

        return v
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_channel_back->{
                activity?.onBackPressed()
            }
            R.id.btn_channel_subscribe->{
                btn_channel_subscribe.background= resources.getDrawable(R.drawable.radius_concern_10_0xd8d8d8,null)
                val handler=Handler{
                    when(it.what){
                        0x000-> Snackbar.make(v, "已取消订阅", Snackbar.LENGTH_SHORT).show()
                        0x001-> Snackbar.make(v, "已订阅", Snackbar.LENGTH_SHORT).show()
                    }
                    false
                }
                if(channelViewModel.changeAll(handler)){
                    btn_channel_subscribe.background= resources.getDrawable(R.drawable.radius_concern_10_0xd8d8d8,null)
                    btn_channel_subscribe.text="取消订阅"
                }
                else{
                    btn_channel_subscribe.background= resources.getDrawable(R.drawable.radius_concern_40_0xf7c325,null)
                    btn_channel_subscribe.text="订阅"
                }
            }
        }

    }

    override fun onScrollChange(view: View?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        val height=v.layout_channel_head.height
        if(scrollY<height/2){
            v.text_channel_head_title.setTextColor(Color.argb(0,0,0,0))
        }else if(scrollY<height && scrollY>height/2){
            v.text_channel_head_title.setTextColor(Color.argb(255*(scrollY*2-height)/height,0,0,0))
        }else if(scrollY>=height){
            v.text_channel_head_title.setTextColor(Color.argb(255,0,0,0))
        }

    }
}