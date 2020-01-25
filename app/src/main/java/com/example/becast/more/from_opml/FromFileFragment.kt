package com.example.becast.more.from_opml

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.becast.R
import com.example.becast.data.Becast
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_file.view.*

class FromFileFragment : Fragment() {

    private lateinit var fromFileViewModel: FromFileViewModel
    private lateinit var v: View
    private val mHandler : Handler = Handler{
        when(it.what){
            Becast.OPEN_OPML_FRAGMENT ->{
                val bundle=Bundle()
                bundle.putString("path",it.obj as String)
                val fromOpmlFragment=FromOpmlFragment()
                fromOpmlFragment.arguments=bundle
                fragmentManager!!.beginTransaction()
                    .hide(this)
                    .add(R.id.layout_more, fromOpmlFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_file, container, false)


        fromFileViewModel= context?.let { FromFileViewModel(it) }!!

        Glide.with(context!!)
            .load(resources.getDrawable(R.drawable.loading_gif,null))
            .into(v.image_file_loading)
        v.list_file.layoutManager=LinearLayoutManager(context)
        v.list_file.adapter=  FileAdapter(fromFileViewModel.listLiveData.value!!,mHandler)
        fromFileViewModel.listLiveData.observe(this, Observer {
            if(fromFileViewModel.listLiveData.value!!.size>0){
                v.layout_file_loading.visibility=View.GONE
            }
            v.list_file.adapter?.notifyDataSetChanged()

        })


        return v
    }


}