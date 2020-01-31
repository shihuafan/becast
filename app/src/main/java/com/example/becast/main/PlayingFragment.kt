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
import com.example.becast.playpage.play.PlayPageFragment
import com.example.becast.service.MediaHelper
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_playing.view.*
import java.util.*

open class PlayingFragment : Fragment(), View.OnClickListener {

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
        v.text_playing_title.isSelected=true
        return v
    }

    private fun setView(){
        MediaHelper().getPlayer()?.let {
            if(!it.radioItemEmpty()) {
                if(v.text_playing_title.text != it.getRadioItem().title){
                    v.text_playing_title.text = it.getRadioItem().title
                    context?.let {it_->
                        Glide.with(it_)
                            .load(it.getRadioItem().xmlImageUrl)
                            .apply(RequestOptions.overrideOf(100,100))
                            .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                            .into(v.image_playing_show)
                    }
                }
                if(it.isRadioPlaying()){
                    v.btn_playing_pause.setBackgroundResource(R.drawable.pause_light)
                }
                else{
                    v.btn_playing_pause.setBackgroundResource(R.drawable.play_light)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        MediaHelper().getPlayer()?.let {
            when(v!!.id){
                R.id.btn_playing_page->{
                    if(!it.radioItemEmpty()){
                        fragmentManager!!.findFragmentByTag("pageFragment")?.let {it_->
                            fragmentManager!!.beginTransaction()
                                .hide(it_)
                                .hide(fragmentManager!!.findFragmentByTag("pageFragment")!!)
                                .add(R.id.layout_main_all, PlayPageFragment(),"PlayPageFragment")
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                }
                R.id.btn_playing_pause->{
                    if(it.pauseRadio()){
                        v.btn_playing_pause.setBackgroundResource(R.drawable.pause_light)
                    }
                    else{
                        v.btn_playing_pause.setBackgroundResource(R.drawable.play_light)
                    }
                }
            }
        }
    }

}