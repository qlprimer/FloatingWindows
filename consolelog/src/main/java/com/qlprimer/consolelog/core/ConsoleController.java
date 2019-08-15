package com.qlprimer.consolelog.core;

import android.os.Message;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;

import com.qlprimer.consolelog.utils.DateTimeUtil;

import java.util.Date;

public class ConsoleController {
    private static final long maxBuffer=16*1024;
//    private static SpannableStringBuilder builder=new SpannableStringBuilder();
    private static StringBuffer buffer=new StringBuffer();

    private static final String modelStr="%1$s/%2$s/%3$s:%4$s ";
    private static final String dateformat="HH:mm:ss.SSS";
    private static final float fontsize=1;

    private static final int  COLOR_VERBOSE = Integer.valueOf(0xff909090);
    private static final int COLOR_DEBUG = Integer.valueOf(0xffc88b48);
    private static final int COLOR_INFO =  Integer.valueOf(0xffc9c9c9);
    private static final int COLOR_WARN =  Integer.valueOf(0xffa97db6);
    private static final int COLOR_ERROR =  Integer.valueOf(0xffff534e);
    private static final int COLOR_WTF =  Integer.valueOf(0xffff5540);
    private static final int COLOR_CONSOLE=  Integer.valueOf(0xF0F8FF);

    public static String parseSpan(Message message){
        String type=message.peekData().getString("type");
        String tag=message.peekData().getString("tag");
        String msg=message.peekData().getString("msg");
        Throwable throwable= (Throwable) message.peekData().getSerializable("throwable");

        switch (type){
            case "I":
                log_I(tag,msg);
                break;
            case "V":
                log_V(tag,msg);
                break;
            case "D":
                log_D(tag,msg);
                break;
            case "W":
                log_W(tag,msg);
                break;
            case "E":
                log_E(tag,msg);
                break;
            case "ET":
                log_Et(tag,throwable);
                break;
            case "WTF":
                log_WTF(tag,msg);
                break;
                default:
                    println(msg);
                    break;
        }

        ensureOutMaxBuffer();
        return buffer.toString();
    }

    private static void log_I(String tag,String msg){
        String log=String.format(modelStr,"I", DateTimeUtil.dateToStr(new Date(),dateformat),tag,msg);
//        SpannableString style = new SpannableString (log);
//        style.setSpan(new ForegroundColorSpan(COLOR_INFO),0,log.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        style.setSpan(new RelativeSizeSpan(fontsize),0,log.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        builder.append(style);
//        builder.append("\n");

        buffer.append(log);
        buffer.append("\n");
    }

    /**
     * logger VERBOSE
     * @param tag
     * @param msg
     */
    public static void log_V(String tag,String msg){
        String log=String.format(modelStr,"V", DateTimeUtil.dateToStr(new Date(),dateformat),tag,msg);
//        SpannableString style = new SpannableString (log);
//        style.setSpan(new ForegroundColorSpan(COLOR_VERBOSE),0,log.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        style.setSpan(new RelativeSizeSpan(fontsize),0,log.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        builder.append(style);
//        builder.append("\n");

        buffer.append(log);
        buffer.append("\n");
    }

    /**
     * logger debug
     * @param tag
     * @param msg
     */
    public static void log_D(String tag,String msg){
        String log=String.format(modelStr,"D", DateTimeUtil.dateToStr(new Date(),dateformat),tag,msg);
//        SpannableString style = new SpannableString (log);
//        style.setSpan(new ForegroundColorSpan(COLOR_DEBUG),0,log.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        style.setSpan(new RelativeSizeSpan(fontsize),0,log.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        builder.append(style);
//        builder.append("\n");
        buffer.append(log);
        buffer.append("\n");
    }

    /**
     * logger warn
     * @param tag
     * @param msg
     */
    public static void log_W(String tag,String msg){
        String log=String.format(modelStr,"W", DateTimeUtil.dateToStr(new Date(),dateformat),tag,msg);
//        SpannableString style = new SpannableString (log);
//        style.setSpan(new ForegroundColorSpan(COLOR_WARN),0,log.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        style.setSpan(new RelativeSizeSpan(fontsize),0,log.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        builder.append(style);
//        builder.append("\n");
        buffer.append(log);
        buffer.append("\n");

    }

    /**
     * error
     * @param tag
     * @param msg
     */
    public static void log_E(String tag,String msg){
        String log=String.format(modelStr,"E", DateTimeUtil.dateToStr(new Date(),dateformat),tag,msg);
//        SpannableString style = new SpannableString (log);
//        style.setSpan(new ForegroundColorSpan(COLOR_ERROR),0,log.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        style.setSpan(new RelativeSizeSpan(fontsize),0,log.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        builder.append(style);
//        builder.append("\n");
        buffer.append(log);
        buffer.append("\n");
    }

    /**
     * error
     * @param tag
     * @param throwable
     */
    public static void log_Et(String tag,Throwable throwable){
        StackTraceElement[] stackTraceElements=throwable.getStackTrace();
        StringBuffer sb=new StringBuffer();
        sb.append(throwable.toString()+"("+throwable.getLocalizedMessage()+")");
        for(StackTraceElement se:stackTraceElements){
            sb.append("\n");
            sb.append("\t"+se.getClassName()+"\t");
            sb.append(se.getFileName()+"\t");
            sb.append("("+se.getLineNumber()+")\t");
            sb.append(se.getMethodName()+"\t");
        }
        String log=String.format(modelStr,"E", DateTimeUtil.dateToStr(new Date(),dateformat),tag,sb.toString());
//        SpannableString style = new SpannableString (log);
//        style.setSpan(new ForegroundColorSpan(COLOR_ERROR),0,log.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        style.setSpan(new RelativeSizeSpan(fontsize*0.8F),0,log.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        builder.append(style);
//        builder.append("\n");
        buffer.append(log);
        buffer.append("\n");

    }


    /**
     * WTF!!!
     * @param tag
     * @param msg
     */
    public static void log_WTF(String tag,String msg){
        String log=String.format(modelStr,"WTF", DateTimeUtil.dateToStr(new Date(),dateformat),tag,msg);
//        SpannableString style = new SpannableString (log);
//        style.setSpan(new ForegroundColorSpan(COLOR_ERROR),0,log.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        style.setSpan(new RelativeSizeSpan(fontsize),0,log.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        builder.append(style);
//        builder.append("\n");
        buffer.append(log);
        buffer.append("\n");

    }

    public static void println(String msg){
//        SpannableString style = new SpannableString (msg);
//        style.setSpan(new ForegroundColorSpan(COLOR_CONSOLE),0,msg.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        style.setSpan(new RelativeSizeSpan(fontsize),0,msg.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        builder.append(style);
//        builder.append("\n");
        buffer.append(msg);
        buffer.append("\n");
    }




    private static void clear(){
//        builder.delete(0,builder.length());
        buffer.delete(0,buffer.length());
    }

    private static void ensureOutMaxBuffer(){
//        if(builder.length()>maxBuffer){
//            int outSize= (int) (builder.length()-maxBuffer);
//            builder.delete(0,outSize);
//        }

        if(buffer.length()>maxBuffer){
            int outSize= (int) (buffer.length()-maxBuffer);
            buffer.delete(0,outSize);
        }

    }

}
