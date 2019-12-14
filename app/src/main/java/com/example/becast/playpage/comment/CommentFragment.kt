package com.example.becast.playpage.comment

 import android.content.Context.INPUT_METHOD_SERVICE
 import android.os.Bundle
 import android.os.Handler
 import android.view.LayoutInflater
 import android.view.View
 import android.view.ViewGroup
 import android.view.WindowManager
 import android.view.inputmethod.InputMethodManager
 import android.widget.Toast
 import androidx.fragment.app.Fragment
 import androidx.lifecycle.Observer
 import androidx.recyclerview.widget.LinearLayoutManager
 import com.example.becast.R
 import kotlinx.android.synthetic.main.frag_comment.view.*
 import kotlinx.android.synthetic.main.frag_share.view.layout_share


class CommentFragment : Fragment(), View.OnClickListener {

    lateinit var v:View
    private val shareViewModel: CommentViewModel= CommentViewModel()
    private val mHandler=Handler{
        when(it.what){
            0x001->{
                openNote(it.obj as Int)
            }
            0x002->{
                context?.let { it1 -> shareViewModel.delete(it1,it.obj as Int) }
            }
            0x003->{

            }
        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_comment, container, false)

        activity!!.window.setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        var radioUri=arguments!!.getString("radio_uri")
        var rssUri=arguments!!.getString("rss_Uri")
        val startTime=arguments!!.getLong("start_time")
        val endTime=arguments!!.getLong("end_time")
        if(radioUri==null) {
            radioUri="test"
        }
        if(rssUri==null) {
            rssUri="test"
        }

        context?.let { shareViewModel.getComment(it,radioUri) }
        v.layout_share.setOnClickListener{
            activity!!.onBackPressed()
        }
        v.btn_share_add.setOnClickListener { addNote(0,0) }
        v.list_share.layoutManager = LinearLayoutManager(context)
        v.list_share.adapter= context?.let {
            CommentAdapter(
                it,
                shareViewModel.listLiveData.value!!,
                mHandler
            )
        }
        addNote(startTime,endTime)
        shareViewModel.listLiveData.observe(this, Observer{
            v.list_share.adapter?.notifyDataSetChanged()
        })

        return v
    }

    override fun onClick(v: View?) {
        when(v?.id){

        }
    }

    private fun openNote(position:Int){
        if(v.layout_share_note.visibility==View.VISIBLE){
            v.layout_share_note.visibility=View.GONE
        }
        v.edit_share.setText(shareViewModel.list[position].comment)
        v.layout_share_note.visibility=View.VISIBLE
        v.btn_share_finish.setOnClickListener{
            val str=v.edit_share.text.toString()
            if(str.isEmpty()){
                Toast.makeText(context,"内容不能为空",Toast.LENGTH_SHORT).show()
            }else {
                v.layout_share_note.visibility = View.GONE
                val imm = activity!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(activity!!.window.decorView.windowToken, 0)
                context?.let { it1 -> shareViewModel.changeNote(it1,str, position) }
            }
        }
        v.btn_share_cancel.setOnClickListener{
            val imm = activity!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity!!.window.decorView.windowToken, 0)
            v.layout_share_note.visibility=View.GONE
        }
    }

    private fun addNote(startTime:Long,endTime:Long){
        if(v.layout_share_note.visibility==View.VISIBLE){
            v.layout_share_note.visibility=View.GONE
        }
        v.edit_share.setText("")
        v.layout_share_note.visibility=View.VISIBLE
        v.btn_share_finish.setOnClickListener{
            val str=v.edit_share.text.toString()
            if(str.isEmpty()){
                Toast.makeText(context,"内容不能为空",Toast.LENGTH_SHORT).show()
            }else{
                context?.let { it1 -> shareViewModel.addNote(it1,str,startTime,endTime,"i","b") }
                v.layout_share_note.visibility=View.GONE
                val imm = activity!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(activity!!.window.decorView.windowToken, 0)
            }
        }
        v.btn_share_cancel.setOnClickListener{
            val imm = activity!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity!!.window.decorView.windowToken, 0)
            v.layout_share_note.visibility=View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        v.blurLayout.startBlur()
    }

     override fun onStop() {
        v.blurLayout.pauseBlur()
        super.onStop()
    }
}

