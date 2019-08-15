package com.qlprimer.consolelog.core;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.qlprimer.consolelog.entity.SizeEntity;

public abstract class BaseSuspend {

    private Context context;
    private View view;
    private boolean isShowing=false;

    private WindowManager.LayoutParams wmParams;
    private WindowManager windowManager;
    private OnSuspendDismissListener onSuspendDismissListener;

    public BaseSuspend(Context context) {
        this.context = context;
        view= LayoutInflater.from(context).inflate(getLayoutId(),null);
        init();
        initView();
        onCreateSuspension();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void onCreateSuspension();

    public void init() {
        if (windowManager == null) {
            windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        }
        wmParams = getParams();//设置好悬浮窗的参数
        // 悬浮窗默认显示以左上角为起始坐标
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
    }
    public final <E extends View> E findView(int id) {
        try {
            return (E) view.findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }

    public final <E extends View> E findView(int id, View.OnClickListener onClickListener) {
        E e = findView(id);
        e.setOnClickListener(onClickListener);
        return e;
    }

    /**
     * 对windowManager进行设置
     *
     * @return
     */
    public WindowManager.LayoutParams getParams() {
        wmParams = new WindowManager.LayoutParams();
        //设置window type 下面变量2002是在屏幕区域显示，2003则可以显示在状态栏之上
        //wmParams.type = LayoutParams.TYPE_PHONE;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }

//        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;

        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        //wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        //设置可以显示在状态栏上
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        return wmParams;
    }

    /**
     * 全屏显示悬浮视图
     */
    public void showSuspend() {
        showSuspend(0, 0, true,1);
    }

    /**
     * 显示悬浮视图
     *
     * @param sizeEntity
     * @param isMatchParent 是否全屏显示
     * @param alpha 透明度
     */
    public void showSuspend(SizeEntity sizeEntity, boolean isMatchParent,float alpha) {
        if (sizeEntity != null) {
            showSuspend(sizeEntity.getWidth(), sizeEntity.getHeight(), isMatchParent,alpha);
        }
    }

    /**
     * 显示悬浮视图
     *
     * @param width
     * @param height
     */
    public void showSuspend(int width, int height, boolean isMatchParent,float alpha) {
        //设置悬浮窗口长宽数据
        if (isMatchParent) {
            wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            wmParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        } else {
            wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        //悬浮窗的开始位置，读取缓存
        wmParams.x = width;
        wmParams.y = height;
        wmParams.alpha=alpha;

        if (isShowing) {
            windowManager.removeView(view);
        }
        windowManager.addView(view, wmParams);
        isShowing = true;
    }

    public void hiddenSuspend(){
        if (isShowing) {
            windowManager.removeView(view);
            isShowing=false;
        }
    }

    /**
     * 更新当前视图的位置
     *
     * @param x 更新后的X轴的增量
     * @param y 更新后的Y轴的增量
     */
    public void updateSuspend(int x, int y) {
        if (view != null) {
            //必须是当前显示的视图才给更新
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) view.getLayoutParams();
            layoutParams.x += x;
            layoutParams.y += y;
            windowManager.updateViewLayout(view, layoutParams);
        }
    }


    /**
     * 移除当前悬浮窗
     */
    public void dismissSuspend() {
        if (view != null) {
            windowManager.removeView(view);
            isShowing = false;
            if (onSuspendDismissListener != null) {
                onSuspendDismissListener.onDismiss();
            }
        }
    }

    public Context getContext() {
        return context;
    }

    public View getView() {
        return view;
    }

    /**
     * 是否正在显示
     *
     * @return
     */
    public boolean isShowing() {
        return isShowing;
    }

    /**
     * 移除弹窗的时候回调
     *
     * @param onSuspendDismissListener
     */
    public void setOnSuspendDismissListener(OnSuspendDismissListener onSuspendDismissListener) {
        this.onSuspendDismissListener = onSuspendDismissListener;
    }

    public interface OnSuspendDismissListener {
        public void onDismiss();
    }


}
