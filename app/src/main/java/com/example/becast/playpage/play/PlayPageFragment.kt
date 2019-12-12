package com.example.becast.playpage.play

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
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
import com.example.becast.data.ShareData
import com.example.becast.playpage.share.ShareFragment
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_playpage.view.*
import java.util.*

class PlayPageFragment(private val mBinder: RadioService.LocalBinder) : Fragment(),  View.OnClickListener, SeekBar.OnSeekBarChangeListener{

    private lateinit var v: View
    private val playPageViewModel= PlayPageViewModel()
    private var shareData=ShareData(0,0,"",null)
    private var timer=Timer()
    private val task=object : TimerTask() {
        override fun run() {
            setView()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_playpage, container, false)

        context?.let {
            Glide.with(it)
                .load(Uri.parse(mBinder.getRadioItem().rssImageUri))
                .apply(RequestOptions.overrideOf(300,300))
                .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                .into(v.image_play_show) }
        v.text_play_title.text= mBinder.getRadioItem().title
        v.text_play_title.isSelected=true
        v.text_play_rsstitle.text= mBinder.getRadioItem().rssTitle

        v.layout_pin.visibility=View.GONE
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
        v.btn_play_share.setOnClickListener(this)
        v.btn_play_wait_list.setOnClickListener(this)
        v.btn_play_channel.setOnClickListener(this)
        v.btn_play_pin.setOnTouchListener{ _: View, motionEvent: MotionEvent ->
            when(motionEvent.action){
                MotionEvent.ACTION_DOWN->{
                //    shareData.startTime=mBinder.getRadioCurrentPosition()/1000
                    shareData.startTime=System.currentTimeMillis()
                }
                MotionEvent.ACTION_MOVE->{
                    val time=mBinder.getRadioCurrentPosition()/1000
                    @SuppressLint("SetTextI18n")
                    v.text_playpage_pin.text=playPageViewModel.timeToStr(shareData.startTime.toInt())+"-"+playPageViewModel.timeToStr(time)
                }
                //记录结束
                MotionEvent.ACTION_UP->{
                    v.layout_pin.visibility=View.GONE
                  //  shareData.endTime=mBinder.getRadioCurrentPosition()/1000
                    shareData.endTime=System.currentTimeMillis()
                    if((shareData.endTime-shareData.startTime)<500  || (shareData.endTime-shareData.startTime)>3000){
                        val bundle=Bundle()
                        bundle.putBinder("Binder",mBinder)
                        bundle.putString("radio_uri",mBinder.getRadioItem().radioUri)
                        bundle.putString("rss_uri",mBinder.getRadioItem().rssUri)
                        bundle.putLong("start_time",shareData.startTime)
                        bundle.putLong("end_time",shareData.endTime)
                        val shareFragment=ShareFragment()
                        shareFragment.arguments=bundle
                        fragmentManager!!.beginTransaction()
                            .add(R.id.layout_main_all, shareFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    else {
                        Toast.makeText(context,"截取时长不应小于3秒",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            false
        }

        return v
    }

    private fun setView(){
        try {
                v.seekBar_play.progress = mBinder.getRadioCurrentPosition()
                v.seekBar_play.max=mBinder.getRadioDuration()
                v.text_play_duration.text= playPageViewModel.timeToStr(mBinder.getRadioDuration()/1000)
                v.text_play_position.text= playPageViewModel.timeToStr(mBinder.getRadioCurrentPosition()/1000)

                if(mBinder.isRadioPlaying()){
                    v.btn_play_pause.setBackgroundResource(R.drawable.pause_light)
                    v.image_play_loading.clearAnimation()
                    v.image_play_loading.visibility=View.INVISIBLE
                }
                else{
                    if(!mBinder.isPrepared()){
                        v.image_play_loading.visibility=View.VISIBLE
                        v.image_play_loading.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate0))
                    }
                    v.btn_play_pause.setBackgroundResource(R.drawable.play_light)

                }

        }catch (e:Exception){}
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
                    v.btn_play_pause.setBackgroundResource(R.drawable.pause_light)
                }
                else{
                    v.btn_play_pause.setBackgroundResource(R.drawable.play_light)
                }
            }
            R.id.btn_play_next->{
                mBinder.playNextRadio()
            }
            R.id.layout_play->{ }
            R.id.btn_play_sleep->{
                context?.let { SleepBottomSheetDialog(it) }
            }
            R.id.btn_play_share->{
                context?.let { ShareBottomSheetDialog(it,mBinder.getRadioItem().link) }
            }
            R.id.btn_play_wait_list->{
                context?.let { WaitListBottomSheetDialog(this,it,mBinder)}
            }
            R.id.btn_play_channel->{
//                val rssData= context?.let { playPageViewModel.getRssData(it,mBinder.getRadioItem()) }
//                if(rssData!=null){
//                    fragmentManager!!.beginTransaction()
//                        .replace(R.id.layout_play,ChannelFragment(rssData.call(),mBinder))
//                        .addToBackStack(null)
//                        .commit()
//                }

            }
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

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(hidden){timer.cancel()}
        else{
            timer.schedule(task, 0, 1000)
        }

    }

}

