package com.qlprimer.consolelog.handler;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GlobalHandler extends Handler {
    private HandleMessageListener listener;

    public HandleMessageListener getListener() {
        return listener;
    }

    public void setListener(HandleMessageListener listener) {
        this.listener = listener;
    }

    private static class Holder{
        private static final GlobalHandler handler=new GlobalHandler();
    }
    public static GlobalHandler getInstance(){
        return Holder.handler;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (getHandleMessageListener() != null){
            getHandleMessageListener().handleMsg(msg);
        }else {
            Log.e("GlobalHandler","请传入HandleMsgListener对象");
        }


    }

    public void setHandleMessageListener(HandleMessageListener listener){
        this.listener=listener;
    }
    public HandleMessageListener getHandleMessageListener(){
        return listener;
    }

    public interface HandleMessageListener{
        void handleMsg(Message msg);
    }



}
