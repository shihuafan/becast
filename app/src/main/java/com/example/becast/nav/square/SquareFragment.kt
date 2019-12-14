package com.example.becast.nav.square

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.becast.R
import com.example.becast.nav.square.recommend.RecommendFragment
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_square.view.*


class SquareFragment(private var mBinder: RadioService.LocalBinder): Fragment(){

    private val squareViewModel:SquareViewModel=SquareViewModel()
    private val mHandler=Handler{
        when(it.what){
            0x001->{
                var url=it.obj as String
                url="https://raw.githubusercontent.com/shihuafan/cubing/master/shf1211.json"
                fragmentManager!!.beginTransaction()
                    .hide(this)
                    .add(R.id.layout_main_all,
                        RecommendFragment(url, mBinder)
                    )
                    .addToBackStack(null)
                    .commit()
            }
            0x002->{
                var url=it.obj as String
                url="https://raw.githubusercontent.com/shihuafan/cubing/master/shf1211.json"
                fragmentManager!!.beginTransaction()
                    .hide(this)
                    .add(R.id.layout_main_all,
                        RecommendFragment(url, mBinder)
                    )
                    .addToBackStack(null)
                    .commit()

            }
            0x003->{
                var url=it.obj as String
                url="https://raw.githubusercontent.com/shihuafan/cubing/master/shf1211.json"
                fragmentManager!!.beginTransaction()
                    .hide(this)
                    .add(R.id.layout_main_all,
                        RecommendFragment(url, mBinder)
                    )
                    .addToBackStack(null)
                    .commit()

            }
        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_square, container, false)

        val layoutManager=LinearLayoutManager(context)
        layoutManager.orientation=LinearLayoutManager.HORIZONTAL
        view.list_square_mix.layoutManager=layoutManager
        view.list_square_mix.adapter=
            context?.let { SquareMixAdapter(it, MutableList(10) { index->index.toString()},mHandler) }

        view.list_square.layoutManager=LinearLayoutManager(context)
        view.list_square.adapter=
            context?.let { SquareAdapter(it, MutableList(20) { index->index.toString()},mHandler) }

        return view
    }
}