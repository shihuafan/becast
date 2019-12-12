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
import com.example.becast.data.mix.MixData
import com.example.becast.data.radioDb.RadioData
import com.example.becast.playpage.play.PlayPageFragment
import com.example.becast.service.RadioService
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.frag_detail.view.*

class DetailFragment(private val radioData: RadioData, private val mBinder: RadioService.LocalBinder):Fragment(), View.OnClickListener {

    private lateinit var v:View
    private lateinit var detailViewModel: DetailViewModel

    override fun onHiddenChanged(hidden:Boolean){
        for( temp in fragmentManager!!.fragments){
            println(temp)
        }
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
            .load(radioData.rssImageUri)
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
               mBinder.playRadio(radioData)
               fragmentManager!!.beginTransaction()
                   .hide(this)
                   .add(R.id.layout_main_all, PlayPageFragment(mBinder))
                   .addToBackStack(null)
                   .commit()
           }
           R.id.btn_detail_rss->{
               val rssData=detailViewModel.getRssData(radioData.rssUri)
               fragmentManager!!.beginTransaction()
                   .hide(this)
                   .add(R.id.layout_main_all, ChannelFragment(rssData,mBinder))
                   .addToBackStack(null)
                   .commit()
           }
           R.id.btn_detail_wait->{
               val handler=Handler{
                   when(it.what) {
                       0x000->{
                           detailViewModel.changeLove()
                           Snackbar.make(v, "已加入收藏", Snackbar.LENGTH_SHORT).show()
                       }
                       0x001->{
                           mBinder.addRadioItemToNext(radioData)
                           Snackbar.make(v, "已加入收听列表", Snackbar.LENGTH_SHORT).show()
                       }
                       0x002->{
                           mBinder.addRadioItem(radioData)
                           Snackbar.make(v, "已加入收听列表", Snackbar.LENGTH_SHORT).show()
                       }
                       0x003->{
                           Snackbar.make(v, (it.obj as MixData).mix, Snackbar.LENGTH_SHORT).show()
                       }
                   }
                   false
               }
               context?.let { AddItemBottomSheetDialog(it,handler) }
           }
       }
    }
}