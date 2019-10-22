package com.example.becast.main.page

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.becast.R
import com.example.becast.main.FromXmlDialog
import com.example.becast.home.subscribe.SubscribeFragment
import com.example.becast.home.wait.WaitFragment
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_page.view.*


class PageFragment(private val handler: Handler,private val mBinder: RadioService.LocalBinder) : Fragment(), View.OnClickListener,
    ViewPager.OnPageChangeListener {

    private lateinit var pageViewModel:PageViewModel
    private lateinit var v:View
    private val mHandler=Handler{
        val msg=Message()
        msg.what=0x004
        msg.obj=it.obj
        handler.sendMessage(msg)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.frag_page, container, false)
        v=view
        pageViewModel= context?.let { PageViewModel(it) }!!

        val list: MutableList<Fragment> = mutableListOf(SubscribeFragment(mBinder),WaitFragment(mBinder))
        view.viewpager_page.adapter= fragmentManager?.let { MyFragmentPagerAdapter(it, list) }
        view.viewpager_page.addOnPageChangeListener(this)
        view.btn_page_more.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_page_more->{
                handler.sendEmptyMessage(0x003)
            }
        }
    }

    override fun onPageSelected(position: Int) {
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    @SuppressLint("SetTextI18n")
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        pageViewModel.textChange(position,positionOffset,v.text_page_subscribe,v.text_page_wait)
        v.image_page_bar.setImageBitmap(pageViewModel.roundedCorner(position,positionOffset))
    }

}
