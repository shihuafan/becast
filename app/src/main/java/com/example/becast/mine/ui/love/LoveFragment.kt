package com.example.becast.mine.ui.love

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.becast.R
import com.example.becast.service.RadioService
import com.example.becast.unit.data.radioDb.RadioData
import com.example.becast.mine.ui.RadioAdapter
import com.example.becast.playpage.PlayPageFragment
import kotlinx.android.synthetic.main.frag_love.view.*

class LoveFragment() :Fragment(), View.OnClickListener {
    private lateinit var loveViewModel:LoveViewModel
    private lateinit var mBinder: RadioService.LocalBinder
    private val mHandler : Handler = Handler{
        when(it.what){
            0x101->{
                val flag=  loveViewModel.addToRadioList(it.obj as RadioData)
                if(flag){
                    Toast.makeText(context,"添加成功", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context,"添加失败,请确定列表中是否已添加该节目", Toast.LENGTH_SHORT).show()
                }
            }
            0x102->{
                loveViewModel.changeLove(it.obj as RadioData)
                Toast.makeText(context,"已取消收藏", Toast.LENGTH_SHORT).show()
            }
            0x103 ->{
                mBinder.playRadio(it.obj as RadioData)
                fragmentManager!!.beginTransaction().replace(R.id.layout_main_all, PlayPageFragment(mBinder))
                    .addToBackStack(null)
                    .commit()
            }
        }
        false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.frag_love, container, false)
        mBinder= this.arguments!!.getBinder("service") as RadioService.LocalBinder
        loveViewModel= context?.let { LoveViewModel(it) }!!

        view.list_love.layoutManager = LinearLayoutManager(context)
        view.list_love.adapter = context?.let {
            RadioAdapter(it,loveViewModel.loveModelLiveData.value!!.list, mHandler)
        }

        //更新列表
        loveViewModel.loveModelLiveData.observe(this, Observer{
            view.list_love.adapter?.notifyDataSetChanged()
        })

        view.btn_love_back.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_love_back->{
            }
        }
    }
}