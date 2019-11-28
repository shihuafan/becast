package com.example.becast.more.search

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.becast.R
import com.example.becast.more.from_xml.FromXmlFragment
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_add_from_xml.view.*
import kotlinx.android.synthetic.main.frag_search.view.*

class SearchFragment : Fragment(){

    private lateinit var searchViewModel:SearchViewModel
    private lateinit var mBinder: RadioService.LocalBinder

    private val mHandler=Handler{
        when(it.what){
            0x103->{

                Toast.makeText(context,it.obj as String,Toast.LENGTH_SHORT).show()
                val fromXmlFragment= FromXmlFragment()
                val bundle=Bundle()
                bundle.putBinder("Binder",mBinder)
                bundle.putString("url",it.obj as String)
                fromXmlFragment.arguments=bundle
                fragmentManager!!.beginTransaction()
                    .hide(this)
                    .add(R.id.layout_more, fromXmlFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
        false
    }

    private lateinit var v:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_search, container, false)
        val content=arguments!!.getString("content") as String

        Glide.with(context!!)
            .load(resources.getDrawable(R.drawable.loading_gif,null))
            .into(v.image_search_loading)

        mBinder= arguments!!.getBinder("Binder") as RadioService.LocalBinder
        searchViewModel=SearchViewModel(content,mHandler)

        v.list_search.layoutManager = LinearLayoutManager(context)
        v.list_search.adapter = context?.let { SearchAdapter(it,searchViewModel.listLiveData.value!!,mHandler) }

        searchViewModel.listLiveData.observe(this, Observer{
            if(searchViewModel.listLiveData.value!!.size>0 && v.layout_search_loading.visibility==View.VISIBLE){
                v.layout_search_loading.visibility=View.GONE
            }
            v.list_search.adapter?.notifyDataSetChanged()
        })
        return v
    }
}