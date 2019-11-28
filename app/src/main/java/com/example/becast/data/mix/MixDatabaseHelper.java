package com.example.becast.data.mix;

import android.content.Context;

import androidx.room.Room;

import com.example.becast.data.comment.CommentDatabase;

public class MixDatabaseHelper {
        public static volatile MixDatabase db;
        private static volatile int num=0;

        public static MixDatabase getDb(final Context context){
            synchronized (MixDatabaseHelper.class){
                num++;
            }
            if(db==null){
                synchronized (MixDatabaseHelper.class){
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
