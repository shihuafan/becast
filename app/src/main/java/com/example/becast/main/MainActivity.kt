package com.example.becast.main

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.becast.R
import com.example.becast.broadcastReceiver.MBroadcastReceiver
import com.example.becast.data.UserData
import com.example.becast.main.page.PageFragment
import com.example.becast.nav.follow.FollowFragment
import com.example.becast.nav.history.HistoryFragment
import com.example.becast.nav.love.LoveFragment
import com.example.becast.nav.setting.SettingFragment
import com.example.becast.nav.square.SquareFragment
import com.example.becast.login_signup.login.LoginFragment
import com.example.becast.nav.user.UserFragment
import com.example.becast.service.MediaHelper
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_nav.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var receiver : MBroadcastReceiver
    private var timer:Timer=Timer()
    private val context=this
    private var path : String?=null

    override fun onCreate(savedInstanceState: Bundle?) {

        UserData.getAll(this)
        this.setTheme(UserData.style)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startService(Intent(this,RadioService::class.java))
        MediaHelper().getBinder(this)
        //注册EventBus
        EventBus.getDefault().register(this)


        //使用opml直接进入
        val opmlIntent = intent
        val action = opmlIntent.action
        if (Intent.ACTION_VIEW == action) {
            val str = opmlIntent.data
            if (str != null) {
                path = Uri.decode(str.encodedPath).substring(6)
            }
        }

        //nav
        btn_nav_user.setOnClickListener(this)
        layout_nav.setOnClickListener(this)
        layout_nav_follow.setOnClickListener(this)
        layout_nav_collect.setOnClickListener(this)
        layout_nav_history.setOnClickListener(this)
        layout_nav_square.setOnClickListener(this)
        btn_nav_night.setOnClickListener(this)
        btn_nav_setting.setOnClickListener(this)
        btn_nav_night.setOnClickListener(this)

        //广播动态注册
        receiver=MBroadcastReceiver()
        val intentFilter=IntentFilter()
        intentFilter.addAction("android.intent.action.HEADSET_PLUG")
        registerReceiver(receiver,intentFilter)

        if(UserData.uid == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.layout_main_all, LoginFragment())
                .commit()
        }
        else {
            @SuppressLint("SetTextI18n")
            text_nav_name.text = UserData.name

            Glide.with(context)
                .load(UserData.image)
                .apply(RequestOptions().error(R.drawable.default_head).circleCrop())
                .apply(RequestOptions().circleCrop())
                .into(image_nav_show)


            supportFragmentManager.beginTransaction()
                .replace(R.id.layout_main_bottom, PlayingFragment(), "playingFragment")
                .commit()


            val bundle = Bundle()
            bundle.putString("path", path)
            val pageFragment = PageFragment()
            pageFragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                .replace(R.id.layout_main_top, pageFragment, "pageFragment")
                .commit()
        }
    }

    @SuppressLint("WrongConstant")
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_main_nav->{
                layout_drawer.openDrawer(Gravity.START)
            }
            R.id.btn_nav_user->{
                layout_drawer.closeDrawer(Gravity.START)
                layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                supportFragmentManager.findFragmentByTag("pageFragment")?.let {it1->
                    supportFragmentManager.findFragmentByTag("playingFragment")?.let { it2 ->
                        supportFragmentManager.beginTransaction()
                            .hide(it1)
                            .hide(it2)
                            .add(R.id.layout_main_all, UserFragment())
                            .addToBackStack(null)
                            .commit()
                    }
                }

            }
            R.id.layout_nav_follow->{
                layout_drawer.closeDrawer(Gravity.START)
                layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                supportFragmentManager.findFragmentByTag("pageFragment")?.let {
                    supportFragmentManager.beginTransaction()
                        .hide(it)
                        .add(R.id.layout_main_top, FollowFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
            R.id.layout_nav_collect->{
                layout_drawer.closeDrawer(Gravity.START)
                layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                supportFragmentManager.findFragmentByTag("pageFragment")?.let {
                    supportFragmentManager.beginTransaction()
                        .hide(it)
                        .add(R.id.layout_main_top, LoveFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
            R.id.layout_nav_history->{
                layout_drawer.closeDrawer(Gravity.START)
                layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                supportFragmentManager.findFragmentByTag("pageFragment")?.let {
                    supportFragmentManager.beginTransaction()
                        .hide(it)
                        .add(R.id.layout_main_top, HistoryFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
            R.id.layout_nav_square->{
                layout_drawer.closeDrawer(Gravity.START)
                layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                supportFragmentManager.findFragmentByTag("pageFragment")?.let {it1->
                    supportFragmentManager.findFragmentByTag("playingFragment")?.let { it2 ->
                        supportFragmentManager.beginTransaction()
                            .hide(it1)
                            .hide(it2)
                            .add(R.id.layout_main_top, SquareFragment())
                            .addToBackStack(null)
                            .commit()
                    }
                }
            }
            R.id.btn_nav_setting->{
                layout_drawer.closeDrawer(Gravity.START)
                layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                supportFragmentManager.findFragmentByTag("pageFragment")?.let {it1->
                    supportFragmentManager.findFragmentByTag("playingFragment")?.let {it2->
                        supportFragmentManager.beginTransaction()
                            .hide(it1)
                            .hide(it2)
                            .add(R.id.layout_main_all, SettingFragment())
                            .addToBackStack(null)
                            .commit()
                    }
                }
            }
            R.id.btn_nav_night->{
                layout_drawer.closeDrawer(Gravity.START)
                UserData.change(this,style=true)
                startActivity(Intent(this,MainActivity::class.java))
                this.finish()
            }
        }
    }

    override fun onDestroy() {
        MediaHelper().unbindService(this)
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun changeDrawer(msg:String){
        when(msg){
            "close"->{
                layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
            "open"->{
                layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
            "delay"->{
                if(UserData.delay==0){
                    timer.cancel()
                    timer= Timer()
                }else{
                    var delay=0L
                    when(UserData.delay){
                        1-> delay=10*60*1000L
                        2-> delay=30*60*1000L
                        3-> delay=60*60*1000L
                    }
                    timer.cancel()
                    timer= Timer()
                    timer.schedule(object : TimerTask() {
                        override fun run() { context.finish() }}, delay)
                }
            }
            "stop_radio"->{
                MediaHelper().getBinder()?.let {
                    if(it.isRadioPlaying()) {
                        it.pauseRadio()
                    }
                }

            }
            "change_user"->{
                UserData.uid?.let {
                    @SuppressLint("SetTextI18n")
                    text_nav_name.text = UserData.name

                    Glide.with(context)
                        .load(UserData.image)
                        .apply(RequestOptions().error(R.drawable.default_head).circleCrop())
                        .apply(RequestOptions().circleCrop())
                        .into(image_nav_show)
                }
            }
        }
    }



}
