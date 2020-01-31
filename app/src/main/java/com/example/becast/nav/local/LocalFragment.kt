package com.example.becast.nav.local

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
import com.example.becast.data.Becast
import com.example.becast.data.radio.RadioData
import com.example.becast.playpage.detail.DetailFragment
import kotlinx.android.synthetic.main.frag_local.view.*
import org.greenrobot.eventbus.EventBus
import java.io.File

class LocalFragment : Fragment(), View.OnClickListener {

    private val localViewModel = LocalViewModel()

    private val mHandler : Handler = Handler{
        when(it.what){
            Becast.OPEN_DETAIL_FRAGMENT ->{
                val radioData=it.obj as RadioData
                val path=radioData.downloadPath
                val file= File(path)
                if(!file.exists()){
                    Toast.makeText(context,"文件不存在,已重新下载",Toast.LENGTH_SHORT).show()
                    context?.let { it1 -> localViewModel.delRadio(it1,radioData) }
                }else{
                    fragmentManager!!.beginTransaction()
                        .hide(this)
                        .add(
                            R.id.layout_main_all,
                            DetailFragment(radioData)
                        )
                        .addToBackStack(null)
                        .commit()
                }

            }
        }
        false
    }

    override fun onResume() {
        super.onResume()
        context?.let { localViewModel.getList(it) }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.frag_local, container, false)
        EventBus.getDefault().post("close")

        view.list_local.layoutManager = LinearLayoutManager(context)
        view.list_local.adapter = context?.let {
            LocalAdapter(it,localViewModel.localModelLiveData.value!!, mHandler)
        }

        //更新列表
        localViewModel.localModelLiveData.observe(this, Observer{
            view.list_local.adapter?.notifyDataSetChanged()
        })

        view.layout_local.setOnClickListener(this)
        view.btn_local_back.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_local_back->{
                activity?.onBackPressed()
            }
        }
    }

}