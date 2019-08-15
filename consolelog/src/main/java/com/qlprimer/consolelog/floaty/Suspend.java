package com.qlprimer.consolelog.floaty;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.qlprimer.consolelog.R;
import com.qlprimer.consolelog.core.BaseSuspend;
import com.qlprimer.consolelog.entity.SizeEntity;
import com.qlprimer.consolelog.utils.SuspensionCache;

public class Suspend extends BaseSuspend {
    private ImageView imageView;
    private int width,height;
    private float mStartX, mStartY, mStopX, mStopY, touchStartX, touchStartY;
    private long touchStartTime;

    private View.OnClickListener onClickListener;
    public Suspend(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.floaty_ball_layout;
    }

    @Override
    protected void initView() {
        imageView=findView(R.id.ball_log);
        imageView.setOnTouchListener(new View.OnTouchListener() {
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

    @Override
    protected void onCreateSuspension() {
        Log.i("","");
    }

    public void setOnClickListener(View.OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }
}
