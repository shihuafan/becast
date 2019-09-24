package com.example.becast.main

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
import com.example.becast.playpage.PlayPageFragment
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_playing.view.*
import java.util.*

open class PlayingFragment(private val mBinder: RadioService.LocalBinder) : Fragment(), View.OnClickListener {

    private lateinit var v:View
    private val mHandler: Handler = Handler{
        setView()
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_playing, container, false)

        Timer().schedule(object : TimerTask() {
            override fun run() { mHandler.sendEmptyMessage(1) }}, 0, 10)

        v.btn_playing_page.setOnClickListener(this)
        v.btn_playing_pause.setOnClickListener(this)
        return v
    }

    private fun setView(){
        if(!mBinder.radioItemEmpty()) {
            context?.let {
                Glide.with(context!!)
                    .load(mBinder.getRadioItem().imageUri)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(10)).circleCrop())
                    .into(v.image_playing_show)
            }
            v.text_playing_title.text= mBinder.getRadioItem().title
            v.text_playing_rsstitle.text= mBinder.getRadioItem().rssTitle
            if(mBinder.isRadioPlaying()){
                v.btn_playing_pause.setBackgroundResource(R.drawable.pause)
            }
            else{
                v.btn_playing_pause.setBackgroundResource(R.drawable.play)
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_playing_page->{
                fragmentManager!!.beginTransaction().replace(R.id.layout_main_all, PlayPageFragment(mBinder))
                    .addToBackStack(null)
                    .commit()
            }
            R.id.btn_playing_pause->{
                if(mBinder.pauseRadio()){
                    v.btn_playing_pause.setBackgroundResource(R.drawable.pause)
                }
                else{
                    v.btn_playing_pause.setBackgroundResource(R.drawable.play)
                }
            }
        }
    }

}