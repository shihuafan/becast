package com.example.becast.playpage.comment

 import android.annotation.SuppressLint
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
 import com.example.becast.data.Becast
 import com.example.becast.data.comment.CommentData
 import com.example.becast.data.radio.RadioData
 import com.example.becast.playpage.share.ShareData
 import kotlinx.android.synthetic.main.frag_comment.view.*
 import kotlinx.android.synthetic.main.frag_share.view.layout_share


class CommentFragment(private val commentData: CommentData,private val longClick:Boolean) : Fragment(){

    lateinit var v:View
    private val commentViewModel: CommentViewModel= CommentViewModel(commentData)
    private val mHandler=Handler{
        when(it.what){
            Becast.OPEN_NOTE->{
                openNote(it.obj as Int)
            }
            Becast.DELETE_NOTE->{
                commentViewModel.delete(it.obj as Int)
            }
            Becast.SHARE_NOTE->{

            }
        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_comment, container, false)

        commentViewModel.getComment()
        if(longClick){
            v.layout_share_note.visibility=View.VISIBLE
            addNote()
            activity!!.window.setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        }
        v.btn_share_add.setOnClickListener{ addNote() }
        v.list_share.layoutManager = LinearLayoutManager(context)
        v.list_share.adapter= context?.let {
            CommentAdapter(it, commentViewModel.listLiveData.value!!, mHandler)
        }
        commentViewModel.listLiveData.observe(this, Observer{
            v.list_share.adapter?.notifyDataSetChanged()
        })

        return v
    }

    private fun openNote(position:Int){
        if(v.layout_share_note.visibility==View.VISIBLE){
            v.layout_share_note.visibility=View.GONE
        }
        v.edit_share.setText(commentViewModel.list[position].comment)
        v.layout_share_note.visibility=View.VISIBLE
        v.btn_share_finish.setOnClickListener{
            val str=v.edit_share.text.toString()
            if(str.isEmpty()){
                Toast.makeText(context,"内容不能为空",Toast.LENGTH_SHORT).show()
            }else {
                v.layout_share_note.visibility = View.GONE
                val imm = activity!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(activity!!.window.decorView.windowToken, 0)
                commentViewModel.changeNote(str, position)
            }
        }
        v.btn_share_cancel.setOnClickListener{
            val imm = activity!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity!!.window.decorView.windowToken, 0)
            v.layout_share_note.visibility=View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun addNote(){
        if(v.layout_share_note.visibility==View.VISIBLE){
            v.layout_share_note.visibility=View.GONE
        }
        if(commentData.endTime-commentData.startTime<1){
            v.text_comment_time.text=commentViewModel.timeToStr(commentData.startTime)
        }else{
            v.text_comment_time.text=
                commentViewModel.timeToStr(commentData.startTime)+ " - "+commentViewModel.timeToStr(commentData.endTime)
        }
        v.edit_share.setText("")
        v.layout_share_note.visibility=View.VISIBLE
        v.btn_share_finish.setOnClickListener{
            val str=v.edit_share.text.toString()
            if(str.isEmpty()){
                Toast.makeText(context,"内容不能为空",Toast.LENGTH_SHORT).show()
            }else{
                commentData.comment=str
                commentData.createTime=System.currentTimeMillis()
                commentViewModel.addNote(commentData)
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

