package com.example.becast.main

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.becast.R
import com.example.becast.data.radioList.RadioListData
import com.example.becast.data.user.UserData
import com.example.becast.service.RadioService
import com.example.becast.mine.ui.addfromxml.AddFromXmlFragment
import com.example.becast.mine.ui.history.HistoryFragment
import com.example.becast.nav.love.LoveFragment
import com.example.becast.mine.ui.radioList.RadioListFragment
import com.example.becast.mine.ui.subscribe.SubscribeFragment
import com.example.becast.mine.page.PageFragment
import com.example.becast.nav.about.AboutFragment
import com.example.becast.nav.user.login.LoginFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_main.*
import kotlinx.android.synthetic.main.layout_nav.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var conn : MyConnection
    internal lateinit var mBinder: RadioService.LocalBinder
    private val mainViewModel=MainViewModel(this)

    private val mHandler : Handler = Handler{
        lateinit var fg:Fragment
        when(it.what){
            0x001->{ fg= SubscribeFragment(mBinder) }
            0x002->{ fg= LoveFragment(mBinder) }
            0x003->{ fg= HistoryFragment(mBinder) }
            0x004->{ fg= RadioListFragment(mBinder,it.obj as RadioListData) }
            0x005->{ fg= AddFromXmlFragment(it.obj.toString()) }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_main_top,fg)
            .addToBackStack(null)
            .commit()
        false
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list: MutableList<Fragment> = mutableListOf()
        list.add(PageFragment(mHandler))
        list.add(PageFragment(mHandler))
        viewpager_main.adapter= MyFragmentPagerAdapter(supportFragmentManager, list)

        UserData.getAll(this)
        Glide.with(this)
            .load(UserData.image)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(10)).circleCrop().error(R.drawable.user))
            .into(image_nav_show)
        if(UserData.uid==0){
            text_nav_name.text="点击头像登录"
        }
        else{
            text_nav_name.text="昵称:"+ UserData.name
            text_nav_uid.text="uid:"+UserData.uid.toString()
        }

        val intent = Intent(this, RadioService::class.java)
        conn=MyConnection()
        bindService(intent,conn,BIND_AUTO_CREATE)

        btn_main_more.setOnClickListener(this)
        btn_main_nav.setOnClickListener(this)
        btn_nav_night.setOnClickListener(this)
        layout_nav.setOnClickListener(this)
        layout_nav_firstpage.setOnClickListener(this)
        layout_nav_love.setOnClickListener(this)
        layout_nav_about.setOnClickListener(this)
        btn_nav_login.setOnClickListener(this)


    }

    @SuppressLint("WrongConstant")
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_main_more ->{
                FromXmlDialog(this, mHandler)
            }
            R.id.btn_main_nav->{
                layout_drawer.openDrawer(Gravity.START)
            }
            R.id.btn_nav_login->{
                layout_drawer.closeDrawer(Gravity.START)
                if(UserData.uid==0){
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.layout_main_all, LoginFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
            R.id.layout_nav_firstpage->{
                layout_drawer.setBackgroundColor(0xf2f2f2)
                layout_drawer.closeDrawer(Gravity.START)
            }
            R.id.layout_nav_love->{
                layout_drawer.closeDrawer(Gravity.START)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.layout_main_all, LoveFragment(mBinder))
                    .addToBackStack(null)
                    .commit()

            }
            R.id.btn_nav_night->{
                btn_nav_night.isChecked = !btn_nav_night.isChecked
            }
            R.id.layout_nav_about->{
                layout_drawer.closeDrawer(Gravity.START)
                if(UserData.uid==0){
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.layout_main_all, AboutFragment ())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }

    internal inner class MyConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mBinder = service as RadioService.LocalBinder
            supportFragmentManager.beginTransaction()
                .replace(R.id.layout_main_bottom, PlayingFragment(mBinder))
                .commit()
        }
        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }

    override fun onDestroy() {
        unbindService(conn)
        super.onDestroy()
    }
}
