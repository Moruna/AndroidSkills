package com.moruna.imagecachetest.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Author: Moruna
 * Date: 2017-07-26
 * Copyright (c) 2017,dudu Co.,Ltd. All rights reserved.
 */
public class NetCacheUtil {

    private LocalCacheUtil localCacheUtil;
    private MemoryCacheUtil memoryCacheUtil;

    public NetCacheUtil(LocalCacheUtil localCache,
                        MemoryCacheUtil memoryCache) {
        localCacheUtil = localCache;
        memoryCacheUtil = memoryCache;
    }

    public void getBitmapFromNet(ImageView imageView, String url) {
        new Task().execute(imageView, url);
    }

    private class Task extends AsyncTask<Object, Void, Bitmap> {
        ImageView imageView;
        String url;

        @Override
        protected Bitmap doInBackground(Object... params) {
            imageView = (ImageView) params[0];
            url = (String) params[1];
            return downLoadBitmap(url);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                memoryCacheUtil.setBitmapToMemory(url, bitmap);
                localCacheUtil.setBitmapToLocal(url, bitmap);
            }
        }
    }

    /**
     * 网络下载图片
     *
     * @param url
     * @return
     */
    private Bitmap downLoadBitmap(String url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                //图片压缩
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;//宽高压缩为原来的1/2
                options.inPreferredConfig = Bitmap.Config.ARGB_4444;
                Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream(),
                        null, options);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return null;
    }
}
