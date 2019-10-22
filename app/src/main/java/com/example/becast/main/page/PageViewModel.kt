package com.example.becast.main.page

import android.content.Context
import android.graphics.*
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView

class PageViewModel(private val context: Context) {

    fun roundedCorner(position: Int, positionOffset: Float): Bitmap {

        val left: Int
        val right: Int
        val radius=15F
        val height=2*radius.toInt()
        val colorL:Int
        val colorR:Int

        if(position==1){
             left=2*radius.toInt()
             right=100+2*radius.toInt()
             colorL = Color.argb(255,216,216,216)
             colorR = Color.argb(255,247,195,37)
        }
        else {
            left=(2*radius+100-positionOffset*100).toInt()
            right=(2*radius+positionOffset*100).toInt()
            colorR = Color.argb(255,
                (247*positionOffset+216*(1-positionOffset)).toInt(),
                (195*positionOffset+216*(1-positionOffset)).toInt(),
                (37*positionOffset+216*(1-positionOffset)).toInt())
            colorL = Color.argb(255,
                (216*positionOffset+247*(1-positionOffset)).toInt(),
                (216*positionOffset+195*(1-positionOffset)).toInt(),
                (216*positionOffset+37*(1-positionOffset)).toInt())
        }

        val result = Bitmap.createBitmap(left+20+right, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)

        val paint = Paint()
        val rectL = Rect(0, 0, left, height)
        val rectR = Rect(left+20, 0, left+20+right, height)
        val rectLF = RectF(rectL)
        val rectRF = RectF(rectR)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = colorL
        canvas.drawRoundRect(rectLF, radius, radius, paint)
        paint.color = colorR
        canvas.drawRoundRect(rectRF, radius, radius, paint)

        return result
    }

    fun textChange(position: Int, positionOffset: Float, textView1:TextView, textView2:TextView){

        if(position==0 && positionOffset==0F){
            textView1.visibility=View.VISIBLE
            textView2.visibility=View.GONE
            textView1.setTextColor(Color.argb(255, 0, 0, 0))
        }
        else if(position==0){
            textView1.visibility=View.VISIBLE
            textView2.visibility=View.VISIBLE
            textView1.setTextColor(Color.argb((255-255*positionOffset).toInt(), 0, 0, 0))
            textView1.textSize=26F-10*positionOffset
            textView2.setTextColor(Color.argb((255*positionOffset).toInt(), 0, 0, 0))
            textView2.textSize=16F+10*positionOffset
            val layoutParams = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            layoutParams.setMargins((500-500*positionOffset).toInt(), 0, 0, 0)
            textView2.layoutParams = layoutParams
        }
        else{
            textView1.visibility= View.GONE
            textView2.visibility=View.VISIBLE
            textView2.setTextColor(Color.argb(255, 0, 0, 0))
        }

    }

}