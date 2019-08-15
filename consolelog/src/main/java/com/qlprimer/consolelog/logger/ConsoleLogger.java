package com.qlprimer.consolelog.logger;




import android.os.Bundle;
import android.os.Message;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;


import com.qlprimer.consolelog.handler.GlobalHandler;
import com.qlprimer.consolelog.utils.DateTimeUtil;

import java.util.Date;


public class ConsoleLogger {

    /**
     * logger  info
     * @param tag 标签
     * @param msg 内容
     */
    public static void i(String tag,String msg){
        writeMessage("I",tag,msg,null);
    }

    /**
     * logger VERBOSE
     * @param tag
     * @param msg
     */
    public static void v(String tag,String msg){
        writeMessage("V",tag,msg,null);
    }

    /**
     * logger debug
     * @param tag
     * @param msg
     */
    public static void d(String tag,String msg){
        writeMessage("D",tag,msg,null);

    }

    /**
     * logger warn
     * @param tag
     * @param msg
     */
    public static void w(String tag,String msg){
        writeMessage("W",tag,msg,null);
    }

    /**
     * error
     * @param tag
     * @param msg
     */
    public static void e(String tag,String msg){
        writeMessage("E",tag,msg,null);

    }

    /**
     * error
     * @param tag
     * @param throwable
     */
    public static void e(String tag,Throwable throwable){
        writeMessage("ET",tag,null,throwable);
    }

    /**
     * what the fuck!!!
     * @param tag
     * @param msg
     */
    public static void wtf(String tag,String msg){
        writeMessage("WTF",tag,msg,null);
    }

    public static void println(String msg){
        writeMessage("PRINTLN",null,msg,null);
    }



    private static void writeMessage(String type,String tag,String msg,Throwable throwable){
        SpannableStringBuilder builder=new SpannableStringBuilder();

        Bundle bundle=new Bundle();
        bundle.putString("type", type);
        bundle.putString("tag",tag);
        bundle.putString("msg",msg);
        bundle.putSerializable("throwable",throwable);

        Message message=Message.obtain();

        message.setData(bundle);
        GlobalHandler.getInstance().sendMessage(message);

    }


}
