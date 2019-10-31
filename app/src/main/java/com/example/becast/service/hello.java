package com.example.becast.service;

import com.example.becast.more.from_xml.FromXmlViewModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class hello {
    public static void main(String[] arg){
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        //第二步构建Request对象
        Request request = new Request.Builder()
                .url("https://getpodcast.xyz/data/ximalaya/9723091.xml")
                .get()
                .build();
        //第三步构建Call对象
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                System.out.println(response.body().string());
                InputStream input= response.body().byteStream();

                XmlPullParserFactory factory = null;
                try {
                    factory = XmlPullParserFactory.newInstance();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                // 获得xml解析类的引用
                XmlPullParser parser = null;
                try {
                    parser = factory.newPullParser();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                assert parser != null;
                try {
                    parser.setInput(input, "UTF-8");
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                // 获得事件的类型
                int eventType = 0;
                try {
                    eventType = parser.getEventType();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if(eventType==XmlPullParser.START_TAG){
                        System.out.println(parser.getName());
                    }
                    try {
                        eventType = parser.next();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }


}
