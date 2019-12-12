package com.example.becast.data.rss;

import android.content.Context;

import androidx.room.Room;

public class RssDatabaseHelper {

    public static volatile RssDatabase db;
    private static volatile int num=0;

    public static RssDatabase getDb(final Context context){
        synchronized (RssDatabaseHelper.class){
            num++;
        }
        if(db==null){
            synchronized (RssDatabaseHelper.class){
                if(db==null){
                    db= Room.databaseBuilder(context,RssDatabase.class, "rss").build();
                }
            }
        }
        return db;
    }

    public synchronized static void closeDb(){
        num--;
        if(num==0){
            db.close();
            db=null;
        }
    }

}
