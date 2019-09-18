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
import com.example.becast.R
import com.example.becast.data.radioListDb.RadioListData
import com.example.becast.service.RadioService
import com.example.becast.mine.ui.addfromxml.AddFromXmlFragment
import com.example.becast.mine.ui.history.HistoryFragment
import com.example.becast.mine.ui.love.LoveFragment
import com.example.becast.mine.ui.radioList.RadioListFragment
import com.example.becast.mine.ui.subscribe.SubscribeFragment
import com.example.becast.mine.page.PageFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_main.*
import kotlinx.android.synthetic.main.layout_nav.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var conn : MyConnection
    internal lateinit var mBinder: RadioService.LocalBinder

    private val mHandler : Handler = Handler{
        lateinit var fg:Fragment
        when(it.what){
            0x001->{ fg= SubscribeFragment() }
            0x002->{ fg= LoveFragment() }
            0x003->{ fg= HistoryFragment() }
            0x004->{ fg= RadioListFragment(it.obj as RadioListData) }
            0x005->{ fg= AddFromXmlFragment(it.obj.toString()) }
        }
        val bundle=Bundle()
        bundle.putBinder("service",mBinder)
        fg.arguments=bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_main_top,fg)
            .addToBackStack(null)
            .commit()
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list: MutableList<Fragment> = mutableListOf()
        list.add(PageFragment(mHandler))
        list.add(PageFragment(mHandler))
        viewpager_main.adapter= MyFragmentPagerAdapter(supportFragmentManager, list)

        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_main_bottom, PlayingFragment())
            .commit()

        val intent = Intent(this, RadioService::class.java)
        conn=MyConnection()
        bindService(intent,conn,BIND_AUTO_CREATE)

        btn_main_more.setOnClickListener(this)
        btn_main_nav.setOnClickListener(this)
        layout_nav.setOnClickListener(this)
        btn_nav_firstpage.setOnClickListener(this)
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
            R.id.btn_nav_firstpage->{
                layout_drawer.closeDrawer(Gravity.START)
            }
        }
    }

    internal inner class MyConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mBinder = service as RadioService.LocalBinder
        }
        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }

    override fun onDestroy() {
        unbindService(conn)
        super.onDestroy()
    }
}
