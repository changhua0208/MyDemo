package com.jch.mydemo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author changhua.jiang
 * @since 2018/9/14 下午3:35
 */

public class BitmapUtils {

    public static boolean saveBmp(byte[] data,File dstFile){
        Bitmap bmp = BitmapFactory.decodeByteArray(data,0,data.length);
        return saveBmp(bmp,dstFile);
    }

    public static boolean saveBmp(Bitmap bmp,File dstFile){
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(dstFile);
            bmp.compress(Bitmap.CompressFormat.PNG,100,out);
        } catch (FileNotFoundException e) {
            return true;
        }
        return false;
    }

    public static byte[] bmp2byteArray(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap byteArray2bmp(byte[] data){
        return BitmapFactory.decodeByteArray(data,0,data.length);
    }
}
