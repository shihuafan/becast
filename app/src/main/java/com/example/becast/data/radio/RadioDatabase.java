package com.example.becast.data.radio;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = RadioData.class, version = 1)
public abstract class RadioDatabase extends RoomDatabase {

    public static volatile RadioDatabase db;
    private static volatile int num=0;

    public abstract RadioDao radioDao();
    public static RadioDatabase getDb(final Context context){
        synchronized (RadioDatabase.class){
            num++;
        }
        if(db==null){
            synchronized (RadioDatabase.class){
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
