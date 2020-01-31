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
import com.example.becast.data.Becast
import com.example.becast.data.radio.RadioData
import com.example.becast.more.MoreFragment
import com.example.becast.more.from_opml.FromOpmlFragment
import com.example.becast.playpage.detail.DetailFragment
import com.example.becast.service.MediaHelper
import com.example.becast.service.player.RadioIPlayer
import kotlinx.android.synthetic.main.frag_page.view.*
import org.greenrobot.eventbus.EventBus


class PageFragment : Fragment(), View.OnClickListener,
    ViewPager.OnPageChangeListener {

    private lateinit var pageViewModel:PageViewModel
    private lateinit var v:View
    private var mPlayer: RadioIPlayer?= null
    private val mHandler : Handler = Handler{
        when(it.what){
            Becast.OPEN_DETAIL_FRAGMENT ->{
                fragmentManager!!.findFragmentByTag("playingFragment")?.let {it_->
                    fragmentManager!!.beginTransaction()
                        .hide(this)
                        .hide(it_)
                        .add(R.id.layout_main_all, DetailFragment(it.obj as RadioData))
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
        false
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if(!hidden){
            EventBus.getDefault().post("open")
            pageViewModel.getAll()
        }
        super.onHiddenChanged(hidden)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.frag_page, container, false)
        v=view
        mPlayer=MediaHelper().getPlayer()
        val path=arguments!!.getString("path")
        path?.let{
            val index= path.lastIndexOf('.')+1
            //这里检验一下文件格式
            if(index>=0 && "opml"== path.substring(index)){
                val bundle=Bundle()
                bundle.putString("path",path)
                val fromOpmlFragment= FromOpmlFragment(false)
                fromOpmlFragment.arguments=bundle
                fragmentManager?.findFragmentByTag("playingFragment")?.let { it1 ->
                    fragmentManager!!.beginTransaction()
                        .replace(R.id.layout_main_all, fromOpmlFragment)
                        .addToBackStack(null)
                        .hide(this)
                        .hide(it1)
                        .commit()
                }
            }
            else{
                Toast.makeText(context, "文件格式错误",Toast.LENGTH_SHORT).show()
            }
        }

        pageViewModel= context?.let { PageViewModel(it) }!!

        view.list_page_subscribe.layoutManager = LinearLayoutManager(context)
        view.list_page_wait.layoutManager = LinearLayoutManager(context)
        view.list_page_subscribe.adapter = context?.let {
            RadioAdapter(it, pageViewModel.subscribeListLiveData.value!!, mHandler)
        }
        view.list_page_wait.adapter = context?.let {
            mPlayer?.let {it_->
                WaitAdapter(it, it_.getLiveData().value!!, mHandler,it_)
            }

        }
        view.list_page_subscribe.addOnScrollListener(object :RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val manager = recyclerView.layoutManager as LinearLayoutManager
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val lastVisibleItem = manager.findLastCompletelyVisibleItemPosition()//从0开始
                    val totalItemCount = manager.itemCount
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
        mPlayer?.getLiveData()?.observe(this, Observer{
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
                fragmentManager!!.findFragmentByTag("playingFragment")?.let {
                    fragmentManager!!.beginTransaction()
                        .hide(this)
                        .hide(it)
                        .replace(R.id.layout_main_all,MoreFragment())
                        .addToBackStack(null)
                        .commit()
                }
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
