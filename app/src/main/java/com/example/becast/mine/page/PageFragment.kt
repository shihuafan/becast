package com.example.becast.mine.page

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.becast.R
import kotlinx.android.synthetic.main.frag_page.view.*

class PageFragment(private val handler: Handler) : Fragment(), View.OnClickListener {

    private lateinit var pageViewModel:PageViewModel

    private val mHandler=Handler{
        val msg=Message()
        msg.what=0x004
        msg.obj=it.obj
        handler.sendMessage(msg)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.frag_page, container, false)

        pageViewModel= context?.let { PageViewModel(it) }!!

        view.list_page.layoutManager = LinearLayoutManager(context)
        view.list_page.adapter = context?.let {
            PageAdapter(it, pageViewModel.list, mHandler
            )
        }
        pageViewModel.pageModelLiveData.observe(this, Observer{
            view.list_page.adapter?.notifyDataSetChanged()
        })

        view.btn_page_subscribe.setOnClickListener(this)
        view.btn_page_love.setOnClickListener(this)
        view.btn_page_history.setOnClickListener(this)
        view.btn_page_add_radio_list.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_page_subscribe->{
                val msg=Message()
                msg.what=0x001
                handler.sendMessage(msg)
            }
            R.id.btn_page_love->{
                val msg=Message()
                msg.what=0x002
                handler.sendMessage(msg)
            }
            R.id.btn_page_history->{
                val msg=Message()
                msg.what=0x003
                handler.sendMessage(msg)
            }
            R.id.btn_page_add_radio_list->{
                val tHandler=Handler{
                    pageViewModel.addRadioList(it.obj.toString())
                    false
                }
                context?.let { AddRadioListDialog(it, tHandler) }
            }
        }
    }
}