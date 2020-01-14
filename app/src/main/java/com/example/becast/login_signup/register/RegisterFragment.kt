package com.example.becast.login_signup.register

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.becast.R
import com.example.becast.data.Becast
import kotlinx.android.synthetic.main.frag_register.*
import kotlinx.android.synthetic.main.frag_register.view.*
import java.util.*

class RegisterFragment :Fragment(), View.OnClickListener {

    private val registerViewModel= RegisterViewModel()
    private var restTime=0
    private lateinit var v:View
    val mHandler=Handler{
        when(it.what){
            Becast.SIGN_UP_SUCCESS->{
                Toast.makeText(context,"注册成功",Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            }
            Becast.SIGN_UP_FAIL->{
                Toast.makeText(context,"注册失败",Toast.LENGTH_SHORT).show()
                onResume()
            }
            Becast.NET_ERROR->{
                Toast.makeText(context,"网络错误",Toast.LENGTH_SHORT).show()
                onResume()
            }
            Becast.CHANGE_REST_TIME->{
                if(restTime>0){
                    restTime--
                    v.btn_get_captcha.text=restTime.toString()
                }
                else{
                    v.btn_get_captcha.text="获取验证码"
                }
            }
        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_register, container, false)

        view.layout_register.setOnClickListener(this)
        view.btn_get_captcha.setOnClickListener(this)
        view.btn_register_register.setOnClickListener(this)
        this.v=view
        return view
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_get_captcha->{
                if(restTime==0){
                    v.btn_get_captcha.setBackgroundResource(R.drawable.radius_concern_40_0xcfcfcf)
                    v.btn_get_captcha.setTextColor(Color.DKGRAY)
                    val phone=edit_register_id.text.toString()
                    registerViewModel.getCaptcha(phone)
                    restTime=60
                    Timer().schedule(object:TimerTask(){
                        override fun run() {
                            mHandler.sendEmptyMessage(Becast.CHANGE_REST_TIME)
                        }
                    },0,1000)
                }
            }
            R.id.btn_register_register->{

                val id=edit_register_id.text.toString()
                val code=edit_register_code.text.toString()
                val password=edit_register_password.text.toString()
                if(id=="" || password=="" || code==""){
                    Toast.makeText(context,"id和密码不能为空",Toast.LENGTH_SHORT).show()
                }
                else{
                    registerViewModel.login(id,code,password,mHandler)
                }
            }
        }
    }
}
