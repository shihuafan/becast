package com.example.becast.playpage.detail

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.becast.R
import com.example.becast.channel.ChannelFragment
import com.example.becast.data.Becast
import com.example.becast.data.mix.MixData
import com.example.becast.data.radio.RadioData
import com.example.becast.data.xml.XmlData
import com.example.becast.playpage.play.PlayPageFragment
import com.example.becast.service.MediaHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.frag_detail.view.*

class DetailFragment(private val radioData: RadioData, private val fromChannel:Boolean=false):Fragment(), View.OnClickListener {

    private lateinit var v:View
    private lateinit var detailViewModel: DetailViewModel
    private val mHandler=Handler{
        when(it.what){
            Becast.OPEN_CHANNEL_FRAGMENT->{
                val xmlData=it.obj as XmlData
                fragmentManager!!.beginTransaction()
                    .hide(this)
                    .add(R.id.layout_main_all, ChannelFragment(xmlData))
                    .addToBackStack(null)
                    .commit()
            }
        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_detail, container, false)
        detailViewModel= context?.let {
            DetailViewModel(
                it,
                radioData
            )
        }!!

        Glide.with(context!!)
            .load(radioData.xmlImageUrl)
            .apply(RequestOptions.overrideOf(100,100))
            .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
            .into(v.image_detail_show)
        v.text_detail_name.text=radioData.title
        v.text_detail_update.text=detailViewModel.getDateString(radioData.upDate)
        v.webview_detail_describe.loadDataWithBaseURL(null,radioData.description,"text/html","utf-8",null)
        v.btn_detail_back.setOnClickListener(this)
        v.btn_detail_play.setOnClickListener(this)
        v.btn_detail_wait.setOnClickListener(this)
        v.btn_detail_rss.setOnClickListener(this)
        v.layout_detail.setOnClickListener(this)
        return v
    }

    override fun onClick(v: View?) {
       when(v?.id){
           R.id.btn_detail_back->{
               activity?.onBackPressed()
           }
           R.id.btn_detail_play->{
               MediaHelper().getBinder()?.playRadio(radioData)
               fragmentManager!!.beginTransaction()
                   .hide(this)
                   .add(R.id.layout_main_all, PlayPageFragment(fromChannel))
                   .addToBackStack(null)
                   .commit()
           }
           R.id.btn_detail_rss->{
               detailViewModel.getXmlData(radioData.xmlUrl,mHandler)
           }
           R.id.btn_detail_wait->{
               MediaHelper().getBinder()
               val handler=Handler{
                   when(it.what) {
                       0x000->{
                           detailViewModel.changeLove()
                           Snackbar.make(v, "已加入收藏", Snackbar.LENGTH_SHORT).show()
                       }
                       0x001->{
                           MediaHelper().getBinder()?.addRadioItemToNext(radioData)
                           Snackbar.make(v, "已加入收听列表", Snackbar.LENGTH_SHORT).show()
                       }
                       0x002->{
                           MediaHelper().getBinder()?.addRadioItem(radioData)
                           Snackbar.make(v, "已加入收听列表", Snackbar.LENGTH_SHORT).show()
                       }
                       0x003->{
                           val text=(it.obj as MixData).mix
                           text?.let { Snackbar.make(v, text, Snackbar.LENGTH_SHORT).show() }
                       }
                   }
                   false
               }
               context?.let { AddItemBottomSheetDialog(it,handler) }
           }
       }
    }
}