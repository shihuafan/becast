package com.example.becast.login_signup.sign_up

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.becast.R
import com.example.becast.data.Becast
import com.example.becast.main.MainActivity
import kotlinx.android.synthetic.main.frag_sign_up.*
import kotlinx.android.synthetic.main.frag_sign_up.view.*
import java.util.*

class SignUpFragment :Fragment(), View.OnClickListener {

    private val registerViewModel= SignUpViewModel()
    private var restTime=0
    private lateinit var v:View
    @SuppressLint("SetTextI18n")
    val mHandler=Handler{
        when(it.what){
            Becast.SIGN_UP_SUCCESS->{
                Toast.makeText(context,"注册成功",Toast.LENGTH_SHORT).show()
                startActivity(Intent(activity, MainActivity::class.java))
            }
            Becast.SIGN_UP_FAIL->{
                Toast.makeText(context,"注册失败",Toast.LENGTH_SHORT).show()
                onResume()
            }
            Becast.NET_ERROR->{
                Toast.makeText(context,"网络错误",Toast.LENGTH_SHORT).show()
                onResume()
            }
            Becast.ALREADY_SIGN_UP->{
                Toast.makeText(context,"该手机号已注册",Toast.LENGTH_SHORT).show()
                onResume()
            }
            Becast.CHANGE_REST_TIME->{
                if(restTime>0){
                    restTime--
                    v.btn_sign_up_captcha.text=restTime.toString()+"秒后可获取"
                }
                else{
                    v.btn_sign_up_captcha.text="获取验证码"
                }
            }
        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_sign_up, container, false)

        view.layout_sign_up.setOnClickListener(this)
        view.btn_sign_up_captcha.setOnClickListener(this)
        view.btn_sign_up_sign_up.setOnClickListener(this)
        view.btn_sign_up_login.setOnClickListener(this)
        this.v=view
        return view
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_sign_up_captcha->{
                val phone=edit_sign_up_phone.text.toString()
                if(phone==""){
                    Toast.makeText(context,"手机号不能为空",Toast.LENGTH_SHORT).show()
                }else if(restTime==0){
                    v.btn_sign_up_captcha.setBackgroundResource(R.drawable.radius_concern_hollow_40_0xf7c325)
                    v.btn_sign_up_captcha.setTextColor(Color.DKGRAY)
                    registerViewModel.getCaptcha(phone,mHandler)
                    restTime=60
                    Timer().schedule(object:TimerTask(){
                        override fun run() {
                            mHandler.sendEmptyMessage(Becast.CHANGE_REST_TIME)
                        }
                    },0,1000)
                }
            }
            R.id.btn_sign_up_sign_up->{
                val phone=edit_sign_up_phone.text.toString()
                val code=edit_sign_up_captcha.text.toString()
                val password=edit_sign_up_password.text.toString()
                if(phone=="" || password=="" || code==""){
                    Toast.makeText(context,"手机号和密码不能为空",Toast.LENGTH_SHORT).show()
                }
                else{
                    context?.let { registerViewModel.signUp(it,phone,code,password,mHandler) }
                }
            }
            R.id.btn_sign_up_login->{
                activity?.onBackPressed()
            }
        }
    }
}
