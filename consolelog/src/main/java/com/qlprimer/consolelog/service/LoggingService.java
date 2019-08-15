package com.qlprimer.consolelog.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;


import com.qlprimer.consolelog.floaty.Suspend;
import com.qlprimer.consolelog.floaty.ConsoleLogSuspend;
import com.qlprimer.consolelog.utils.SuspensionCache;

import net.danlew.android.joda.JodaTimeAndroid;




public class LoggingService extends Service {
    private Suspend ballLogSuspend;
    private ConsoleLogSuspend consoleLogSuspend;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);

        if(ballLogSuspend==null){
            ballLogSuspend=new Suspend(this);
        }
        if(consoleLogSuspend==null){
            consoleLogSuspend=new ConsoleLogSuspend(this);
        }
        ballLogSuspend.showSuspend(SuspensionCache.getSuspendSize(),false,1);
        ballLogSuspend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ballLogSuspend.hiddenSuspend();
                consoleLogSuspend.showSuspend(SuspensionCache.getSuspendSize(),false,0.6F);
            }
        });

        consoleLogSuspend.setOnClickCloseListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consoleLogSuspend.hiddenSuspend();
                ballLogSuspend.showSuspend(SuspensionCache.getSuspendSize(),false,1);

            }
        });



    }

}
