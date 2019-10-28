package com.example.becast.playpage.play

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.becast.R
import com.example.becast.data.ShareData
import com.example.becast.playpage.share.ShareFragment
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_playpage.*
import kotlinx.android.synthetic.main.frag_playpage.view.*
import java.util.*


class PlayPageFragment(private val mBinder: RadioService.LocalBinder) : Fragment(),  View.OnClickListener, SeekBar.OnSeekBarChangeListener{

    private lateinit var v: View
    private val playPageViewModel= PlayPageViewModel()
    private var flag=false
    private var shareData=ShareData(0,0,"",null)
    private val mHandler: Handler = Handler{
        if(!mBinder.radioItemEmpty()){
            setView()
        }
        if(flag){
           // text_play_end_time.text=playPageViewModel.timeToStr(mBinder.getRadioCurrentPosition()/1000)
        }
        false
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_playpage, container, false)

        context?.let {
            Glide.with(it)
                .load(Uri.parse(mBinder.getRadioItem().imageUri))
                .apply(RequestOptions.overrideOf(300,300))
                .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                .into(v.image_play_show) }
        v.text_play_title.text= mBinder.getRadioItem().title
        v.text_play_title.isSelected=true
        v.text_play_rsstitle.text= mBinder.getRadioItem().rssTitle

        v.layout_pin.visibility=View.GONE
        v.image_play_loading.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate0))


        v.btn_playpage_back.setOnClickListener(this)
        v.btn_play_pre.setOnClickListener(this)
        v.btn_play_pause.setOnClickListener(this)
        v.btn_play_next.setOnClickListener(this)
        v.seekBar_play.setOnSeekBarChangeListener(this)
        v.layout_play.setOnClickListener(this)
        v.btn_play_pin.setOnTouchListener{ _: View, motionEvent: MotionEvent ->
            when(motionEvent.action){
                MotionEvent.ACTION_DOWN->{
                    shareData.startTime=mBinder.getRadioCurrentPosition()/1000
                    v.layout_pin.visibility=View.VISIBLE

//                    val alphaAnimation = AlphaAnimation(0.1f, 1.0f)
//                    alphaAnimation.duration = 500
//                    alphaAnimation.repeatCount = Animation.INFINITE
//                    alphaAnimation.repeatMode = Animation.RESTART
//                    v.image_play_point.animation = alphaAnimation
//                    alphaAnimation.start()
                }
                MotionEvent.ACTION_MOVE->{
                    val time=mBinder.getRadioCurrentPosition()/1000
                    @SuppressLint("SetTextI18n")
                    v.text_playpage_pin.text=playPageViewModel.timeToStr(shareData.startTime)+"-"+playPageViewModel.timeToStr(time)
                }
                //记录结束
                MotionEvent.ACTION_UP->{
                    v.layout_pin.visibility=View.GONE
                    shareData.endTime=mBinder.getRadioCurrentPosition()
                    if((shareData.endTime-shareData.startTime)<3000){
                        Toast.makeText(context,"截取时长不应小于3秒",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val content=shareData.toString()
                        shareData.bitmap= playPageViewModel.createQRCodeBitmap(content)
                        fragmentManager!!.beginTransaction()
                            .replace(R.id.layout_main_all, ShareFragment(mBinder.getRadioItem(),shareData))
                            .addToBackStack(null)
                            .commit()
                    }
                }
            }
            false
        }
        Timer().schedule(object : TimerTask() {
            override fun run() { mHandler.sendEmptyMessage(1) }}, 0, 1000)
        return v
    }

    private fun setView(){
        v.seekBar_play.progress = mBinder.getRadioCurrentPosition()
        v.seekBar_play.max=mBinder.getRadioDuration()
        v.text_play_duration.text= playPageViewModel.timeToStr(mBinder.getRadioDuration()/1000)
        v.text_play_position.text= playPageViewModel.timeToStr(mBinder.getRadioCurrentPosition()/1000)
        if(mBinder.isPrepared()){
            v.image_play_loading.clearAnimation()
            v.image_play_loading.visibility=View.INVISIBLE
        }
        else{
            v.image_play_loading.visibility=View.VISIBLE
            v.image_play_loading.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate0))
        }
        if(mBinder.isRadioPlaying()){
            v.btn_play_pause.setBackgroundResource(R.drawable.pause)
        }
        else{
            v.btn_play_pause.setBackgroundResource(R.drawable.play)
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_playpage_back->{
                activity?.onBackPressed()
            }
            R.id.btn_play_pre->{
                mBinder.playPreRadio()
            }
            R.id.btn_play_pause->{
                if(mBinder.pauseRadio()){
                    v.btn_play_pause.setBackgroundResource(R.drawable.pause)
                }
                else{
                    v.btn_play_pause.setBackgroundResource(R.drawable.play)
                }
            }
            R.id.btn_play_next->{
                mBinder.playNextRadio()
            }
            R.id.layout_play->{ }
        }
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        mBinder.seekRadioTo(v.seekBar_play.progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (enter) {
           AnimationUtils.loadAnimation(activity, R.anim.in_from_bottom)
        } else {
            AnimationUtils.loadAnimation(activity, R.anim.out_from_top)
        }

    }
}

