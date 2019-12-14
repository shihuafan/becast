package com.example.becast.data.radio;

import android.content.Context;

import androidx.room.Room;

public class RadioDatabaseHelper {

    public static volatile RadioDatabase db;
    private static volatile int num=0;

    public static RadioDatabase getDb(final Context context){
        synchronized (RadioDatabaseHelper.class){
            num++;
        }
        if(db==null){
            synchronized (RadioDatabaseHelper.class){
                if(db==null){
                    db= Room.databaseBuilder(context,RadioDatabase.class, "radio_db").build();
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
