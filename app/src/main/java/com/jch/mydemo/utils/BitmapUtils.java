package com.jch.mydemo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.View;

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

    public static Bitmap getBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        // Draw background
        Drawable bgDrawable = v.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(c);
        else
            c.drawColor(Color.WHITE);
        // Draw view to canvas
        v.draw(c);
        return b;
    }

    public static Bitmap  rotate(Bitmap bm, final int orientationDegree) {

        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);

            return bm1;

        } catch (OutOfMemoryError ex) {
        }
        return null;

    }
}
