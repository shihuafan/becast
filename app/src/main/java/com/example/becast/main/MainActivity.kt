package com.example.becast.main

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.view.DragEvent
import android.view.Gravity
import android.view.ScaleGestureDetector
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.becast.R
import com.example.becast.data.user.UserData
import com.example.becast.main.page.PageFragment
import com.example.becast.more.MoreFragment
import com.example.becast.nav.follow.FollowFragment
import com.example.becast.nav.history.HistoryFragment
import com.example.becast.nav.love.LoveFragment
import com.example.becast.nav.setting.SettingFragment
import com.example.becast.nav.user.login.LoginFragment
import com.example.becast.nav.user.personal.InfoFragment
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_main.*
import kotlinx.android.synthetic.main.layout_nav.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var conn : MyConnection
    internal lateinit var mBinder: RadioService.LocalBinder
    private lateinit var pageFragment: PageFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EventBus.getDefault().register(this)
        UserData.getAll(this)
        Glide.with(this)
            .load(UserData.image)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(10)).circleCrop().error(R.drawable.user))
            .into(image_nav_show)
        if(UserData.uid==0){
            text_nav_name.text="点击头像登录"
        }
        else{
            @SuppressLint("SetTextI18n")
            text_nav_name.text="昵称:"+ UserData.name
        }

        val intent = Intent(this, RadioService::class.java)
        conn=MyConnection()
        bindService(intent,conn,BIND_AUTO_CREATE)

        //nav
        btn_nav_user.setOnClickListener(this)
        layout_nav.setOnClickListener(this)
        layout_nav_follow.setOnClickListener(this)
        layout_nav_collect.setOnClickListener(this)
        layout_nav_history.setOnClickListener(this)
        btn_nav_night.setOnClickListener(this)
        btn_nav_setting.setOnClickListener(this)

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
                if(UserData.uid == 0){
                    supportFragmentManager.beginTransaction()
                        .hide(pageFragment)
                        .add(R.id.layout_main_all, LoginFragment())
                        .addToBackStack(null)
                        .commit()
                }
                else{
                    supportFragmentManager.beginTransaction()
                        .hide(pageFragment)
                        .add(R.id.layout_main_all, InfoFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
            R.id.layout_nav_follow->{
                layout_drawer.closeDrawer(Gravity.START)
                layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                supportFragmentManager.beginTransaction()
                    .hide(pageFragment)
                    .add(R.id.layout_main_top, FollowFragment(mBinder))
                    .addToBackStack(null)
                    .commit()
            }
            R.id.layout_nav_collect->{
                layout_drawer.closeDrawer(Gravity.START)
                layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                supportFragmentManager.beginTransaction()
                    .hide(pageFragment)
                    .add(R.id.layout_main_top, LoveFragment(mBinder))
                    .addToBackStack(null)
                    .commit()
            }
            R.id.layout_nav_history->{
                layout_drawer.closeDrawer(Gravity.START)
                layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                supportFragmentManager.beginTransaction()
                    .hide(pageFragment)
                    .add(R.id.layout_main_top, HistoryFragment(mBinder))
                    .addToBackStack(null)
                    .commit()
            }
            R.id.btn_nav_setting->{
                layout_drawer.closeDrawer(Gravity.START)
                layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                supportFragmentManager.beginTransaction()
                    .hide(pageFragment)
                    .add(R.id.layout_main_all, SettingFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    internal inner class MyConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mBinder = service as RadioService.LocalBinder


            supportFragmentManager.beginTransaction()
                .replace(R.id.layout_main_bottom, PlayingFragment(mBinder))
                .commit()
            pageFragment=PageFragment(mBinder)
            supportFragmentManager.beginTransaction()
                .replace(R.id.layout_main_top,pageFragment)
                .commit()
        }
        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }

    override fun onDestroy() {
        unbindService(conn)
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this,supportFragmentManager.fragments[supportFragmentManager.fragments.size-1].toString(),Toast.LENGTH_SHORT).show()
        supportFragmentManager.fragments[supportFragmentManager.fragments.size-1].onResume()
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
        }
    }

}
