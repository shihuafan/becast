package com.example.becast.mine.ui.subscribe

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.becast.R
import com.example.becast.main.MainActivity
import com.example.becast.service.RadioService
import com.example.becast.unit.data.radioDb.RadioData
import com.example.becast.mine.ui.unit.RadioAdapter
import com.example.becast.playpage.play.PlayPageFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.frag_subscribe.view.*

@SuppressLint("NewApi")
class SubscribeFragment(private var mBinder: RadioService.LocalBinder) : Fragment(),
    View.OnClickListener{

    private lateinit var subscribeViewModel: SubscribeViewModel
    private val mHandler : Handler = Handler{
        when(it.what){
            0x101->{
               val flag= subscribeViewModel.addToRadioList(it.obj as RadioData)
                if(flag){
                    Toast.makeText(context,"添加成功",Toast.LENGTH_SHORT).show()
                }
                else{
                   Toast.makeText(context,"添加失败,请确定列表中是否已添加该节目",Toast.LENGTH_SHORT).show()
                }
            }
            0x102->{
                subscribeViewModel.changeLove(it.obj as RadioData)
            }
            0x103 ->{
                mBinder.playRadio(it.obj as RadioData)
                fragmentManager!!.beginTransaction().replace(R.id.layout_main_all,
                    PlayPageFragment(mBinder)
                )
                    .addToBackStack(null)
                    .commit()
            }
        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.frag_subscribe, container, false)

        subscribeViewModel= context?.let { SubscribeViewModel(it) }!!

        view.list_subscribe.layoutManager = LinearLayoutManager(context)
        view.list_subscribe.adapter = context?.let {
            RadioAdapter(it,subscribeViewModel.subscribeModelLiveData.value!!, mHandler)
        }

        //更新列表
        subscribeViewModel.subscribeModelLiveData.observe(this, Observer{
            view.list_subscribe.adapter?.notifyDataSetChanged()
        })

        view.layout_subscribe.setOnClickListener(this)
        view.btn_subscribe_back.setOnClickListener(this)
        return view
    }


    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_subscribe_back->{
                activity?.onBackPressed()
            }

        }
    }
}