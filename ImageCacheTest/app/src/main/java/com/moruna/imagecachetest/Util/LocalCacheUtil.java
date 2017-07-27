package com.moruna.imagecachetest.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Author: Moruna
 * Date: 2017-07-26
 * Desc:本地缓存
 */
public class LocalCacheUtil {

    private String CACHE_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/cacheTest";

    public void setBitmapToLocal(String url, Bitmap bitmap) {
        try {
            //MD5加密
            String fileName = MD5Util.MD5Encode(url, null);
            File file = new File(CACHE_PATH, fileName);
            //确定父目录是否存在
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80,
                    new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmapFromCache(String url) {
        try {
            String fileName = MD5Util.MD5Encode(url, null);
            File file = new File(CACHE_PATH, fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(
                    new FileInputStream(file));
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
