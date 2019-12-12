package com.example.becast.data.comment;

import android.content.Context;

import androidx.room.Room;

import com.example.becast.data.radioDb.RadioDatabase;

import kotlin.jvm.JvmOverloads;

public class CommentDatabaseHelper {
        public static volatile CommentDatabase db;
        private static volatile int num=0;


        public static CommentDatabase getDb(final Context context){
            synchronized (CommentDatabaseHelper.class){
                num++;
            }
            if(db==null){
                synchronized (CommentDatabaseHelper.class){
                    if(db==null){
                        db= Room.databaseBuilder(context,CommentDatabase.class, "comment_db").build();
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
