package com.example.becast.data.mix;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.becast.data.radio.RadioData;

@Database(entities = MixData.class, version = 1)
public abstract class MixDatabase extends RoomDatabase {

        public static volatile MixDatabase db;
        private static volatile int num=0;
        public abstract MixDao mixDao();

        public static MixDatabase getDb(final Context context){
            synchronized (MixDatabase.class){
                num++;
            }
            if(db==null){
                synchronized (MixDatabase.class){
                    if(db==null){
                        db= Room.databaseBuilder(context,MixDatabase.class, "mix_db").build();
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
