package com.example.becast;

import com.example.becast.nav.setting.download.DownLoadFromNet;

public class shf  {
    public static void main(String[] args){
        double sum=0;
        for(int i=0;i<1000;i++){
            sum=sum-3.1;
            int lucknum=(int)(Math.random()*6)+1;
            int num=(int)(Math.random()*6)+1;
            if(lucknum==1){
                sum+=0.88;
            }else if(lucknum==2 && num<6){
                sum+=1.88;
            }else if(lucknum==3 && num<5){
                sum+=3.88;
            }else if(lucknum==4 && num<4){
                sum+=8.88;
            }else if(lucknum==5 && num<3){
                sum+=12.88;
            }else if(lucknum==6 && num<2){
                sum+=28.88;
            }
            System.out.println(lucknum+"--"+num+"--"+sum);
        }


    }
}
