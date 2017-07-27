package com.moruna.imagecachetest.Util;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

/**
 * Author: Moruna
 * Date: 2017-07-26
 * Copyright (c) 2017,dudu Co.,Ltd. All rights reserved.
 */
public class BitmapUtil {
    private static final String TAG = "BitmapUtil";
    private MemoryCacheUtil memoryCacheUtil;
    private LocalCacheUtil localCacheUtil;
    private NetCacheUtil netCacheUtil;

    public BitmapUtil() {
        memoryCacheUtil = new MemoryCacheUtil();
        localCacheUtil = new LocalCacheUtil();
        netCacheUtil = new NetCacheUtil(localCacheUtil,
                memoryCacheUtil);
    }

    public void display(ImageView imageView, String url) {
        Bitmap bitmap;
        bitmap = memoryCacheUtil.getBitmapFromMemory(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            Log.e(TAG, "display: memory display");
            return;
        }

        bitmap = localCacheUtil.getBitmapFromCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            memoryCacheUtil.setBitmapToMemory(url, bitmap);
            Log.e(TAG, "display: local display");
            return;
        }

        Log.e(TAG, "display: net display");
        netCacheUtil.getBitmapFromNet(imageView, url);
    }
}
