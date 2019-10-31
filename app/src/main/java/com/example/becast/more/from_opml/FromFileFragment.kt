package com.example.becast.more.from_opml

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.becast.R
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_file.view.*
import kotlinx.android.synthetic.main.frag_opml.view.*

class FromFileFragment : Fragment() {

    private lateinit var fromFileViewModel: FromFileViewModel
    private lateinit var v: View
    private lateinit var mBinder: RadioService.LocalBinder
    private val mHandler : Handler = Handler{
        when(it.what){
            0x103 ->{
                val bundle=Bundle()
                bundle.putBinder("Binder",mBinder)
                bundle.putString("path",it.obj as String)
                val fromOpmlFragment=FromOpmlFragment()
                fromOpmlFragment.arguments=bundle
                fragmentManager!!.beginTransaction()
                    .replace(R.id.layout_file, fromOpmlFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_file, container, false)
        mBinder= arguments!!.getBinder("Binder") as RadioService.LocalBinder
        fromFileViewModel= context?.let { FromFileViewModel(it) }!!

        v.list_file.layoutManager=LinearLayoutManager(context)
        v.list_file.adapter=  FileAdapter(fromFileViewModel.listLiveData.value!!,mHandler)
        fromFileViewModel.listLiveData.observe(this, Observer {
            v.list_file.adapter?.notifyDataSetChanged()
        })
        return v
    }

}