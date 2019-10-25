package com.example.becast.main.page

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.becast.R
import com.example.becast.data.radioDb.RadioData
import com.example.becast.playpage.DetailFragment
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_page.view.*


class PageFragment(private val handler: Handler,private val mBinder: RadioService.LocalBinder) : Fragment(), View.OnClickListener,
    ViewPager.OnPageChangeListener {

    private lateinit var pageViewModel:PageViewModel
    private lateinit var v:View
    private val mHandler : Handler = Handler{
        when(it.what){
            0x103 ->{
                fragmentManager!!.beginTransaction()
                    .replace(R.id.layout_main_all, DetailFragment(it.obj as RadioData,mBinder))
                    .addToBackStack(null)
                    .commit()
            }
        }
        false
    }
    override fun onResume() {
        super.onResume()
        pageViewModel.getAll()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.frag_page, container, false)
        v=view
        pageViewModel= context?.let { PageViewModel(it) }!!

//        val list: MutableList<Fragment> = mutableListOf(SubscribeFragment(mHandler,mBinder),WaitFragment(mBinder))
//        view.viewpager_page.adapter= fragmentManager?.let { MyFragmentPagerAdapter(it, list) }
//        view.viewpager_page.addOnPageChangeListener(this)


        view.list_page_subscribe.layoutManager = LinearLayoutManager(context)
        view.list_page_wait.layoutManager = LinearLayoutManager(context)
        view.list_page_subscribe.adapter = context?.let {
            RadioAdapter(it, pageViewModel.subscribeListLiveData.value!!, mHandler)
        }
        view.list_page_wait.adapter = context?.let {
            RadioAdapter(it, pageViewModel.waitListLiveData.value!!, mHandler)
        }
        view.list_page_subscribe.addOnScrollListener(object :RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val manager = recyclerView.layoutManager as LinearLayoutManager
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val lastVisibleItem = manager.findLastCompletelyVisibleItemPosition()//从0开始
                    val totalItemCount = manager.itemCount
                    Toast.makeText(context,totalItemCount.toString(),Toast.LENGTH_SHORT).show()
                    if (lastVisibleItem >= (totalItemCount - 10)) {
                        pageViewModel.getSubscribeList()
                    }else if(lastVisibleItem < (totalItemCount - 200)){
                        pageViewModel.subscribeList.removeAll(pageViewModel.subscribeList.subList(totalItemCount-100,totalItemCount))
                    }
                }
            }
        })
        pageViewModel.subscribeListLiveData.observe(this, Observer{
            view.list_page_subscribe.adapter?.notifyDataSetChanged()
        })
        pageViewModel.waitListLiveData.observe(this, Observer{
            view.list_page_wait.adapter?.notifyDataSetChanged()
        })


        val list: MutableList<RecyclerView> = mutableListOf(view.list_page_subscribe,view.list_page_wait)
        view.viewpager_page.adapter= fragmentManager?.let { ListPagerAdapter(list) }
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
