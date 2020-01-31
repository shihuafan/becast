package com.example.becast.playpage.play

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.becast.R
import com.example.becast.data.UserData
import com.example.becast.data.comment.CommentData
import com.example.becast.playpage.comment.CommentFragment
import com.example.becast.playpage.share.ShareData
import com.example.becast.playpage.share.ShareFragment
import com.example.becast.service.MediaHelper
import com.example.becast.service.player.RadioIPlayer
import kotlinx.android.synthetic.main.frag_playpage.view.*
import java.util.*

class PlayPageFragment(private val fromChannel:Boolean = false) : Fragment(),  View.OnClickListener, SeekBar.OnSeekBarChangeListener,
    View.OnTouchListener {


    private lateinit var v: View
    private val playPageViewModel= PlayPageViewModel()
    private lateinit var mPlayer: RadioIPlayer
    private var commentData=CommentData()
    private var timer=Timer()
    private val mHandler= Handler{
        if(mPlayer.isPrepared()){
            try {
                v.seekBar_play.progress = mPlayer.getRadioCurrentPosition()
                v.seekBar_play.max = mPlayer.getRadioDuration()
                v.text_play_duration.text = playPageViewModel.timeToStr(mPlayer.getRadioDuration()/1000)
                v.text_play_position.text = playPageViewModel.timeToStr(mPlayer.getRadioCurrentPosition()/1000)

                if (mPlayer.isRadioPlaying()) {
                    v.btn_play_pause.setBackgroundResource(R.drawable.pause_light)
                    v.image_play_loading.clearAnimation()
                    v.image_play_loading.visibility = View.INVISIBLE
                } else {
                    if (!mPlayer.isPrepared()) {
                        v.image_play_loading.visibility = View.VISIBLE
                        v.image_play_loading.startAnimation(
                            AnimationUtils.loadAnimation(
                                context,
                                R.anim.rotate0
                            )
                        )
                    }
                    v.btn_play_pause.setBackgroundResource(R.drawable.play_light)
                }
            }catch (e:Exception){}
        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        v= inflater.inflate(R.layout.frag_playpage, container, false)

        MediaHelper().getPlayer()?.let {
            mPlayer=it
        }
        context?.let {
            Glide.with(it)
                .load(Uri.parse(mPlayer.getRadioItem().xmlImageUrl))
                .apply(RequestOptions.overrideOf(300,300))
                .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                .into(v.image_play_show) }
        v.text_play_title.text= mPlayer.getRadioItem().title
        v.text_play_title.isSelected=true
        v.text_play_rsstitle.text= mPlayer.getRadioItem().xmlTitle

        v.image_play_loading.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate0))

//        v.webview_playpage_describe.loadDataWithBaseURL(
//            null,mBinder.getRadioItem().description,"text/html","utf-8",null)

        v.btn_playpage_back.setOnClickListener(this)
        v.btn_play_pre.setOnClickListener(this)
        v.btn_play_pause.setOnClickListener(this)
        v.btn_play_next.setOnClickListener(this)
        v.seekBar_play.setOnSeekBarChangeListener(this)
        v.layout_play.setOnClickListener(this)
        v.btn_play_sleep.setOnClickListener(this)
        v.btn_play_speed.setOnClickListener(this)
        v.btn_play_share.setOnClickListener(this)
        v.btn_play_download.setOnClickListener(this)
        v.btn_play_channel.setOnClickListener(this)
        v.btn_play_pin.setOnTouchListener(this)
        v.btn_play_pin.setOnTouchListener(this)
        return v
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                commentData.startTime=mPlayer.getRadioCurrentPosition()/1000
            }
            MotionEvent.ACTION_MOVE->{
                val time=mPlayer.getRadioCurrentPosition()/1000
                if((time - commentData.startTime)>1){
                    this.v.layout_pin.visibility=View.VISIBLE
                }
                @SuppressLint("SetTextI18n")
                this.v.text_playpage_pin.text=playPageViewModel.timeToStr(commentData.startTime)+"-"+playPageViewModel.timeToStr(time)
            }
            //记录结束
            MotionEvent.ACTION_UP->{
                this.v.layout_pin.visibility=View.GONE
                commentData.endTime=mPlayer.getRadioCurrentPosition()/1000
                val radioData=mPlayer.getRadioItem()
                commentData.xmlUrl=radioData.xmlUrl
                commentData.radioUrl=radioData.radioUrl
                commentData.xmlTitle=radioData.xmlTitle
                commentData.title=radioData.title
                fragmentManager!!.beginTransaction()
                    .add(R.id.layout_main_all,
                        CommentFragment(commentData,(commentData.endTime-commentData.startTime)>1))
                    .addToBackStack(null)
                    .commit()

            }
        }
        return false
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_playpage_back->{
                activity?.onBackPressed()
            }
            R.id.btn_play_pre->{
                timer.cancel()
                mPlayer.playPreRadio()
            }
            R.id.btn_play_pause->{
                if(mPlayer.pauseRadio()){
                    v.btn_play_pause.setBackgroundResource(R.drawable.pause_light)
                }
                else{
                    v.btn_play_pause.setBackgroundResource(R.drawable.play_light)
                }
            }
            R.id.btn_play_next->{
                mPlayer.playNextRadio()
            }
            R.id.layout_play->{ }

            R.id.btn_play_sleep->{
                context?.let { SleepBottomSheetDialog(it) }
            }
            R.id.btn_play_speed->{
                when(mPlayer.changeRadioSpeed()){
                    10->{
                        v.background=resources.getDrawable(R.drawable.speed10)
                    }
                    15->{
                        v.background=resources.getDrawable(R.drawable.speed15)
                    }
                    20->{
                        v.background=resources.getDrawable(R.drawable.speed20)
                    }

                }
            }
            R.id.btn_play_share->{
                val radioData=mPlayer.getRadioItem()
                val shareData=ShareData(
                    uid=UserData.uid,
                    createTime = System.currentTimeMillis().toString(),
                    xmlUrl=radioData.xmlUrl,
                    radioUrl=radioData.radioUrl,
                    xmlTitle=radioData.xmlTitle,
                    title=radioData.title,
                    xmlImageUrl=radioData.xmlImageUrl,
                    radioImageUrl=radioData.imageUrl
                )
                fragmentManager!!.beginTransaction()
                    .add(R.id.layout_main_all,ShareFragment(shareData))
                    .addToBackStack(null)
                    .commit()
            }
            R.id.btn_play_download->{
                Toast.makeText(context, "已加入下载", Toast.LENGTH_SHORT).show()
                val radioData= MediaHelper().getPlayer()?.getRadioItem()
                radioData?.let {
                    context?.let { it1 -> playPageViewModel.download(it1,it) }
                }

            }
            R.id.btn_play_channel->{
                if(fromChannel){
                    activity?.onBackPressed()
                }
//                else{
//                    val rssData= context?.let { playPageViewModel.getXmlData(it,mBinder.getRadioItem()) }
//                    if(rssData!=null){
//                        fragmentManager!!.beginTransaction()
//                            .replace(R.id.layout_play, ChannelFragment(rssData,mBinder))
//                            .addToBackStack(null)
//                            .commit()
//                    }
//                }


            }
        }
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        mPlayer.seekRadioTo(v.seekBar_play.progress)
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

    override fun onStop() {
        Log.d(TAG,"timer停止")
        timer.cancel()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        timer=Timer()
        timer.schedule(object :TimerTask(){
            override fun run() {
                mHandler.sendEmptyMessage(0x001)
            }
        }, 0, 100)
    }

}

