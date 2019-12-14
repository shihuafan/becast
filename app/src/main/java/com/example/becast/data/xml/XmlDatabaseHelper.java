package com.example.becast.data.xml;

import android.content.Context;

import androidx.room.Room;

public class XmlDatabaseHelper {

    public static volatile XmlDatabase db;
    private static volatile int num=0;

    public static XmlDatabase getDb(final Context context){
        synchronized (XmlDatabaseHelper.class){
            num++;
        }
        if(db==null){
            synchronized (XmlDatabaseHelper.class){
                if(db==null){
                    db= Room.databaseBuilder(context, XmlDatabase.class, "xml_db").build();
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
