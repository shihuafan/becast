package com.example.becast.main.page

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter

class ListPagerAdapter(private val mData:MutableList<RecyclerView>): PagerAdapter() {

    override fun getCount()=mData.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
       return view==`object`
    }

    override fun instantiateItem(container: ViewGroup, position:Int): Any {
        container.addView(mData[position])
        return mData[position]
    }

}