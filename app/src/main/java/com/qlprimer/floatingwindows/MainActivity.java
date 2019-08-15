package com.qlprimer.floatingwindows;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.qlprimer.consolelog.logger.ConsoleLogger;
import com.qlprimer.consolelog.service.LoggingService;
import com.qlprimer.consolelog.utils.DateTimeUtil;

import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!Settings.canDrawOverlays(this)){
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
        }else{
            startService(new Intent(MainActivity.this, LoggingService.class));

        }
        button=findViewById(R.id.btn_log);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new RunableThread("thread1")).start();
//                new Thread(new RunableThread("thread2")).start();
//                new Thread(new RunableThread("thread3")).start();
//                new Thread(new RunableThread("thread4")).start();



            }
        });


    }
    class RunableThread implements Runnable{
        private String mname;
        public RunableThread(String name) {
            mname=name;
        }

        @Override
        public void run() {
            for (int i=0;i<100;i++){
                Random r = new Random(1);
                ConsoleLogger.i(TAG,"T:"+mname+" this is infomation!"+ DateTimeUtil.dateToStr(new Date()));
                ConsoleLogger.e(TAG,"T:"+mname+" this is error!"+ DateTimeUtil.dateToStr(new Date()));
                ConsoleLogger.d(TAG,"T:"+mname+" this is debug!"+DateTimeUtil.dateToStr(new Date()));
                ConsoleLogger.v(TAG,"T:"+mname+" This is VERBOSE!"+DateTimeUtil.dateToStr(new Date()));
                ConsoleLogger.w(TAG,"T:"+mname+" this is warn!"+DateTimeUtil.dateToStr(new Date()));
                ConsoleLogger.wtf(TAG,"T:"+mname+" this is WTF!"+DateTimeUtil.dateToStr(new Date()));
                try {
                    Thread.sleep(r.nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
