package com.example.becast;

import java.util.Timer;
import java.util.TimerTask;

public class shf {
    public static void main(String[] args){
         Timer timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                System.out.print("timer.task");
            }
        };
        timer.schedule(task, 1000, 1000);
        timer.cancel();

    }
}
