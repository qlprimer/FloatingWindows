package com.qlprimer.consolelog.floaty;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qlprimer.consolelog.R;
import com.qlprimer.consolelog.core.BaseSuspend;
import com.qlprimer.consolelog.entity.SizeEntity;
import com.qlprimer.consolelog.handler.GlobalHandler;
import com.qlprimer.consolelog.utils.SuspensionCache;

import static com.qlprimer.consolelog.core.ConsoleController.clear;
import static com.qlprimer.consolelog.core.ConsoleController.parseSpan;

public class ConsoleLogSuspend extends BaseSuspend implements GlobalHandler.HandleMessageListener {
    private Context context;
    private GlobalHandler globalHandler;
    private RelativeLayout relativeLayout;
    private ImageView closeImageView;
    private ImageView cleanImageView;
    private int width,height;
    private float mStartX, mStartY, mStopX, mStopY, touchStartX, touchStartY;
    private long touchStartTime;
    private TextView console_textview;
    private long lastReFreshTime=0;

    private View.OnClickListener onClickListener;
    private View.OnClickListener onClickCloseListener;

    public ConsoleLogSuspend(Context context) {
        super(context);
        this.context=context;
        globalHandler=GlobalHandler.getInstance();
        globalHandler.setHandleMessageListener(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.floaty_window_layout;
    }

    @Override
    protected void initView() {
        initCloseButton();
        initCleanButton();
        relativeLayout=findView(R.id.console_layout);
        //实现textview的滚动功能
        console_textview=findView(R.id.console_content);
        console_textview.setMovementMethod(ScrollingMovementMethod.getInstance());

        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int action = event.getAction();
                mStopX = event.getRawX();
                mStopY = event.getRawY();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // 以当前父视图左上角为原点
                        mStartX = event.getRawX();
                        mStartY = event.getRawY();
                        touchStartX = event.getRawX();
                        touchStartY = event.getRawY();
                        touchStartTime = System.currentTimeMillis();//获取当前时间戳
                        break;
                    case MotionEvent.ACTION_MOVE:
                        width = (int) (mStopX - mStartX);
                        height = (int) (mStopY - mStartY);
                        mStartX = mStopX;
                        mStartY = mStopY;
                        updateSuspend(width, height);
                        break;
                    case MotionEvent.ACTION_UP:
                        width = (int) (mStopX - mStartX);
                        height = (int) (mStopY - mStartY);
                        updateSuspend(width, height);
                        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) getView().getLayoutParams();
                        SuspensionCache.setSuspendSize( new SizeEntity(layoutParams.x + width, layoutParams.y + height));//缓存一下当前位置
                        if ((mStopX - touchStartX) < 30 && (mStartY - touchStartY) < 30 && ( System.currentTimeMillis() - touchStartTime) < 300) {
                            //左右上下移动距离不超过30的，并且按下和抬起时间少于300毫秒，算是单击事件，进行回调
                            if (onClickListener != null) {
                                onClickListener.onClick(view);
                            }
                        }
                        break;
                }
                return true;
            }
        });




    }



    private void initCloseButton(){
        closeImageView=findView(R.id.console_close);
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickCloseListener!=null){
                    onClickCloseListener.onClick(v);
                }
            }
        });
    }
    private void initCleanButton(){
        cleanImageView=findView(R.id.console_clean);
        cleanImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                console_textview.setText(clear());
            }
        });
    }

    @Override
    protected void onCreateSuspension() {

    }
    public void setOnClickListener(View.OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }

    public void setOnClickCloseListener(View.OnClickListener onClickListener){
        this.onClickCloseListener=onClickListener;
    }

    @Override
    public void handleMsg(Message msg) {

//        SpannableStringBuilder builder=parseSpan(msg);
        int offset=console_textview.getLineCount()*console_textview.getLineHeight();
        synchronized (this){
            console_textview.setText(parseSpan(msg));
        }

        int downoffset=offset-console_textview.getHeight();
        if(downoffset>0){
            console_textview.scrollTo(0,downoffset);
        }

    }
}
