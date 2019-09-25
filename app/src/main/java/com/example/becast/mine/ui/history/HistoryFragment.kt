package com.example.becast.mine.ui.history

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
import com.example.becast.mine.ui.unit.RadioAdapter
import com.example.becast.playpage.play.PlayPageFragment
import kotlinx.android.synthetic.main.frag_history.view.*

class HistoryFragment(private var mBinder: RadioService.LocalBinder) :Fragment(), View.OnClickListener {
    private lateinit var historyViewModel:HistoryViewModel

    private val mHandler : Handler = Handler{
        when(it.what){
            0x101->{
                val flag=  historyViewModel.addToRadioList(it.obj as RadioData)
                if(flag){
                    Toast.makeText(context,"添加成功", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context,"添加失败,请确定列表中是否已添加该节目", Toast.LENGTH_SHORT).show()
                }
            }
            0x102->{
                historyViewModel.changeLove(it.obj as RadioData)
                Toast.makeText(context,"已取消收藏", Toast.LENGTH_SHORT).show()
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
        val view= inflater.inflate(R.layout.frag_history, container, false)

        historyViewModel= context?.let { HistoryViewModel(it) }!!

        view.list_history.layoutManager = LinearLayoutManager(context)
        view.list_history.adapter = context?.let {
            RadioAdapter(it,historyViewModel.historyModelLiveData.value!!, mHandler)
        }

        //更新列表
        historyViewModel.historyModelLiveData.observe(this, Observer{
            view.list_history.adapter?.notifyDataSetChanged()
        })

        view.layout_history.setOnClickListener(this)
        view.btn_history_back.setOnClickListener(this)
        view.btn_history_clear.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_history_back->{
                activity?.onBackPressed()
            }
            R.id.btn_history_clear->{
                historyViewModel.clearList()
            }
        }
    }
}