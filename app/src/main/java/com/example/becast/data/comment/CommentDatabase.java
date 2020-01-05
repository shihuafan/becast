package com.example.becast.data.comment;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = CommentData.class, version = 1)
public abstract class CommentDatabase extends RoomDatabase {

        public static volatile CommentDatabase db;
        private static volatile int num=0;
        public abstract CommentDao commentDao();

        public static CommentDatabase getDb(final Context context){
            synchronized (CommentDatabase.class){
                num++;
            }
            if(db==null){
                synchronized (CommentDatabase.class){
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