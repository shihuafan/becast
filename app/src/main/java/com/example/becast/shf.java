package com.example.becast;


import android.os.AsyncTask;

import java.io.IOException;

public class shf extends AsyncTask<String,Integer,String> {

    public static void main(String[] args) throws IOException {

        float S = 100f;
        float I = 100f;
        float R = 0f;

        float cure = 0.3f;
        float get = 0.3f;

        float N=S+I+R;

        for(int i=0;i<100;i++){
            float temp=N * get * I * S / N / N;
            float temp2=I * cure;
            S = S - temp + temp2;
            I = I + temp - temp2;
            System.out.println(S + "\t" + I + "\t" + R + "\t");
        }
//         DiskClassLoader diskClassLoader=new DiskClassLoader("D:");
//         try {
//             Class clazz=diskClassLoader.loadClass("com.example.Jobs");
//             if(clazz!=null){
//                 try{
//                     Object obj= clazz.newInstance();
//                     System.out.println(obj.getClass().getClassLoader());
//                     Method method=clazz.getDeclaredMethod("say");
//                     method.invoke(obj);
//                 }
//                 catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e){
//                     e.printStackTrace();
//                 }
//             }
//         }
//         catch (ClassNotFoundException e){
//             e.printStackTrace();
//         }

    }


    @Override
    protected String doInBackground(String... strings) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
