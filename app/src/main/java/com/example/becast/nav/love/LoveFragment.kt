package com.example.becast.nav.love

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.becast.R
import com.example.becast.data.mix.MixData
import kotlinx.android.synthetic.main.frag_love.view.*
import org.greenrobot.eventbus.EventBus

class LoveFragment :Fragment(), View.OnClickListener {

    private val loveViewModel: LoveViewModel=LoveViewModel()

    private val mHandler : Handler = Handler{
        when(it.what){
            0x001 ->{
                fragmentManager!!.beginTransaction()
                    .hide(this)
                    .add(R.id.layout_main_top, MixFragment(it.obj as MixData))
                    .addToBackStack(null)
                    .commit()
            }
        }
        false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.frag_love, container, false)
        EventBus.getDefault().post("close")

        context?.let { loveViewModel.getMix(it) }

        view.list_love.layoutManager = GridLayoutManager(context,3)
        view.list_love.adapter = context?.let {
            LoveAdapter(it,loveViewModel.loveModelLiveData.value!!, mHandler)
        }

        //更新列表
        loveViewModel.loveModelLiveData.observe(this, Observer{
            view.list_love.adapter?.notifyDataSetChanged()
        })

        view.layout_love.setOnClickListener(this)
        view.btn_love_back.setOnClickListener(this)
        view.btn_love_add.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_love_back->{
                activity?.onBackPressed()
            }
            R.id.btn_love_add->{
                context?.let { AddMixDialog(it,loveViewModel) }
            }
        }
    }
}