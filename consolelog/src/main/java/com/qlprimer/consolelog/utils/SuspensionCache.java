package com.qlprimer.consolelog.utils;

import android.content.Context;

import com.qlprimer.consolelog.entity.SizeEntity;

public class SuspensionCache {
    private static SizeEntity entity=new SizeEntity(0,200);//初始化悬浮球位置

    public static SizeEntity getSuspendSize() {
        return entity;
    }

    public static void setSuspendSize(SizeEntity sizeEntity) {
        entity=sizeEntity;
    }
}
