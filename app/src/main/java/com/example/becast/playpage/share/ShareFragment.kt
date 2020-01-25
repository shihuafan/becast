package com.example.becast.playpage.share

 import android.Manifest
 import android.content.*
 import android.content.pm.PackageManager
 import android.graphics.Bitmap
 import android.graphics.Canvas
 import android.net.Uri
 import android.os.Build
 import android.os.Bundle
 import android.os.Handler
 import android.provider.MediaStore
 import android.view.LayoutInflater
 import android.view.View
 import android.view.ViewGroup
 import android.widget.Toast
 import androidx.core.app.ActivityCompat
 import androidx.core.content.ContextCompat.getSystemService
 import androidx.fragment.app.Fragment
 import com.bumptech.glide.Glide
 import com.bumptech.glide.request.RequestOptions
 import com.example.becast.R
 import com.example.becast.data.Becast
 import com.example.becast.data.UserData
 import com.google.android.material.snackbar.Snackbar
 import kotlinx.android.synthetic.main.frag_share.view.*


class ShareFragment(private val shareData: ShareData) : Fragment(), View.OnClickListener {

    lateinit var v:View
    private val shareViewModel: ShareViewModel=ShareViewModel()

    private val mHandler=Handler{
        when(it.what){
            Becast.SHARE_BK->{
                v.layout_share_background.setBackgroundColor(it.obj as Int)
            }
        }

        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_share, container, false)

        shareViewModel.getBackgroundColor(mHandler,shareData.xmlImageUrl)
        shareViewModel.getShareId(shareData)

        shareData.shareLink=UserData.BaseUrl+"/share?uid=${shareData.uid}&create_time=${shareData.createTime}"
        context?.let {
            Glide.with(it)
                .load(shareViewModel.createQRCodeBitmap(shareData.shareLink.toString()))
                .apply(RequestOptions.overrideOf(500,500))
                .into(v.image_share_qrcode)

            Glide.with(it)
                .load(shareData.xmlImageUrl)
                .apply(RequestOptions.overrideOf(500,500))
                .into(v.image_share_image)
        }

        v.text_share_xmltitle.text=shareData.xmlTitle
        v.text_share_title.text=shareData.title
        v.layout_share.setOnClickListener(this)
        v.btn_share_qq.setOnClickListener(this)
        v.btn_share_wechat.setOnClickListener(this)
        v.btn_share_link.setOnClickListener(this)
        v.btn_share_time_line.setOnClickListener(this)

        return v
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.layout_share_background->{
                activity?.onBackPressed()
            }
            R.id.btn_share_qq->{

                context?.let { share(it,this.v.layout_share_background,ShareViewModel.TAG_QQ) }
            }
            R.id.btn_share_wechat->{
                context?.let { share(it,this.v.layout_share_background,ShareViewModel.TAG_WeChat) }
            }
            R.id.btn_share_time_line->{
                context?.let { share(it,this.v.layout_share_background,ShareViewModel.TAG_TimeLine) }
            }
            R.id.btn_share_link->{
                context?.let {
                    val cm = getSystemService(it,ClipboardManager::class.java)
                    val mClipData = ClipData.newPlainText("Label", shareData.shareLink)
                    cm!!.primaryClip = mClipData
                }
                Toast.makeText(context,"已复制到剪贴板",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun share(context: Context, view: View, tag:Int){
        val permissionsStorage = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            context.let {
                if (ActivityCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissionsStorage, 1)
                } else{
                    shareViewModel.shareTo(context,view,tag)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1) {
            for (grant in grantResults) {
                if(grant != PackageManager.PERMISSION_GRANTED){
                    Snackbar.make(this.v,"无法获取本地数据", Snackbar.LENGTH_SHORT).show()
                    return
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}

