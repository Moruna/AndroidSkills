package com.moruna.imagecachetest.Util;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * Author: Moruna
 * Date: 2017-07-26
 * Desc:内存缓存
 */
public class MemoryCacheUtil {
    private static final String TAG = "MemoryCacheUtil";
    //官方推荐使用LruCache，之前一般使用HashMap+SoftReference
    private LruCache<String, Bitmap> memoryCache;

    public MemoryCacheUtil() {
        //得到手机最大允许内存的1/8,即超过指定内存,则开始回收
        long maxMemory = Runtime.getRuntime().maxMemory() / 8;
        memoryCache = new LruCache<String, Bitmap>((int) maxMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                Log.e(TAG, "sizeOf: " + value.getByteCount());
                return value.getByteCount();
            }
        };
    }

    public void setBitmapToMemory(String url, Bitmap bitmap) {
        memoryCache.put(url, bitmap);
    }

    public Bitmap getBitmapFromMemory(String url) {
        Bitmap bitmap = memoryCache.get(url);
        return bitmap;
    }
}
