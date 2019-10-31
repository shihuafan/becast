package com.example.becast.more.from_opml

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
import com.example.becast.more.from_xml.FromXmlFragment
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_opml.view.*

class FromOpmlFragment : Fragment() {

    private lateinit var fromOpmlViewModel: FromOpmlViewModel
    private lateinit var v: View
    private lateinit var mBinder: RadioService.LocalBinder

    private val mHandler : Handler = Handler{

        when(it.what){
            0x103 ->{
                val fromXmlFragment= FromXmlFragment()
                val bundle=Bundle()
                bundle.putBinder("Binder",mBinder)
                bundle.putString("url",it.obj as String)
                fromXmlFragment.arguments=bundle
                childFragmentManager.beginTransaction()
                    .replace(R.id.layout_opml, fromXmlFragment)
                    .commit()
            }
            404->{
                Toast.makeText(context,"文件解析加载异常",Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            }
        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_opml, container, false)
        mBinder= arguments!!.getBinder("Binder") as RadioService.LocalBinder
        val path=arguments!!.getString("path") as String
        fromOpmlViewModel=FromOpmlViewModel(path, mHandler)

        v.list_opml.layoutManager=LinearLayoutManager(context)
        v.list_opml.adapter=  OpmlAdapter(fromOpmlViewModel.listLiveData.value!!,mHandler)
        fromOpmlViewModel.listLiveData.observe(this, Observer {
            v.list_opml.adapter?.notifyDataSetChanged()
        })
        return v
    }

}