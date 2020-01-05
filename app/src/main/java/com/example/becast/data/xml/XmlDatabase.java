package com.example.becast.data.xml;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities=XmlData.class, version = 1)
public abstract class XmlDatabase extends RoomDatabase{

    public static volatile XmlDatabase db;
    private static volatile int num=0;

    public static XmlDatabase getDb(final Context context){
        synchronized (XmlDatabase.class){
            num++;
        }
        if(db==null){
            synchronized (XmlDatabase.class){
                if(db==null){
                    db= Room.databaseBuilder(context, XmlDatabase.class, "xml_db").build();
                }
            }
        }
        return db;
    }

    public abstract XmlDao xmlDao();

    public synchronized static void closeDb(){
        num--;
        if(num==0){
            db.close();
            db=null;
        }
    }

}
