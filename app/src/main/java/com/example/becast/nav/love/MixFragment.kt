package com.example.becast.nav.love

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.becast.R
import com.example.becast.data.mix.MixData
import com.example.becast.data.radioDb.RadioData
import com.example.becast.playpage.detail.DetailFragment
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_love.view.*
import kotlinx.android.synthetic.main.frag_mix.view.*
import org.greenrobot.eventbus.EventBus

class MixFragment(private val mixData: MixData,private val mBinder: RadioService.LocalBinder) :Fragment(), View.OnClickListener {
    private lateinit var mixViewModel: MixViewModel
    private val mHandler : Handler = Handler{
        when(it.what){
            0x001 ->{
                mBinder.playRadio(it.obj as RadioData)
                fragmentManager!!.beginTransaction()
                    .hide(this)
                    .add(R.id.layout_main_all,
                        DetailFragment(
                            it.obj as RadioData,
                            mBinder
                        )
                    )
                    .addToBackStack(null)
                    .commit()
            }
        }
        false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.frag_mix, container, false)
        EventBus.getDefault().post("close")

        mixViewModel= context?.let { MixViewModel(mixData,it) }!!

        view.list_mix.layoutManager = LinearLayoutManager(context)
        view.list_mix.adapter = context?.let {
            MixAdapter(it,mixViewModel.mixModelLiveData.value!!, mHandler,mixViewModel)
        }

        //更新列表
        mixViewModel.mixModelLiveData.observe(this, Observer{
            view.list_mix.adapter?.notifyDataSetChanged()
        })


        view.layout_mix.setOnClickListener(this)
        view.btn_mix_back.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_mix_back->{
                activity?.onBackPressed()
            }
        }
    }
}