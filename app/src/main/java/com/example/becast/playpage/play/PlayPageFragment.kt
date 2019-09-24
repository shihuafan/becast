package com.example.becast.playpage.play

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
            text_play_end_time.text=playPageViewModel.timeToStr(mBinder.getRadioCurrentPosition()/1000)
        }
        false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_playpage, container, false)

        val rotate0= AnimationUtils.loadAnimation(context, R.anim.rotate0)
        v.layout_pin.visibility=View.GONE
        v.image_play_loading.startAnimation(rotate0)
        v.btn_play_back.setOnClickListener(this)
        v.btn_play_pre.setOnClickListener(this)
        v.btn_play_pause.setOnClickListener(this)
        v.btn_play_next.setOnClickListener(this)
        v.seekBar_play.setOnSeekBarChangeListener(this)
        v.layout_play.setOnClickListener(this)
        v.btn_play_pin.setOnTouchListener{ _: View, motionEvent: MotionEvent ->
            when(motionEvent.action){
                MotionEvent.ACTION_DOWN->{
                    //开始记录
                    v.layout_play_pin.visibility=View.VISIBLE
                    v.layout_pin.visibility=View.VISIBLE
                    flag=true
                    shareData.startTime=mBinder.getRadioCurrentPosition()
                    shareData.radioUri= mBinder.getRadioItem().radioUri
                    text_play_start_time.text=playPageViewModel.timeToStr(shareData.startTime/1000)

                    //时间显示动画
                    val translateAnimation = TranslateAnimation(0F, -300F, 0F, 0f)
                    translateAnimation.duration = 300
                    translateAnimation.repeatCount =0
                    translateAnimation.repeatMode = Animation.RESTART
                    translateAnimation.fillAfter=true
                    v.layout_pin.animation = translateAnimation
                    translateAnimation.start()

                    //红点闪烁动画
                    val alphaAnimation = AlphaAnimation(0.1f, 1.0f)
                    alphaAnimation.duration = 500
                    alphaAnimation.repeatCount = Animation.INFINITE
                    alphaAnimation.repeatMode = Animation.RESTART
                    v.image_play_point.animation = alphaAnimation
                    alphaAnimation.start()
                }
                //记录结束
                MotionEvent.ACTION_UP->{
                    shareData.endTime=mBinder.getRadioCurrentPosition()
                    v.layout_pin.visibility=View.GONE
                    v.layout_play_pin.visibility=View.GONE
                    flag=false

                    //将信息传到Dialog中
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
            override fun run() { mHandler.sendEmptyMessage(1) }}, 0, 10)

        setView()
        return v
    }

    //更新播放界面状态
    private fun setView(){
        context?.let { Glide.with(it).load(Uri.parse(mBinder.getRadioItem().imageUri)).into(v.image_play_show) }
        v.text_play_title.text= mBinder.getRadioItem().title
        v.text_play_rsstitle.text= mBinder.getRadioItem().rssTitle
        v.seekBar_play.max=mBinder.getRadioDuration()
        v.seekBar_play.progress = mBinder.getRadioCurrentPosition()
        v.text_play_duration.text= mBinder.getRadioItem().duration
        v.text_play_position.text= playPageViewModel.timeToStr(mBinder.getRadioCurrentPosition()/1000)
        if(mBinder.isRadioPlaying()){
            v.image_play_loading.clearAnimation()
            v.image_play_loading.visibility=View.INVISIBLE
            v.btn_play_pause.setBackgroundResource(R.drawable.pause)
        }
        else{
            v.btn_play_pause.setBackgroundResource(R.drawable.play)
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_play_back->{
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
//            R.id.btn_play_back->{
//                activity?.onBackPressed()
//            }
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

