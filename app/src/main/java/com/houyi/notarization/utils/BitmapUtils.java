package com.houyi.notarization.utils;

import android.content.Context;
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
            return false;
        }
        return true;
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

    public static Bitmap getBitmapFromFile(File file){
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public static Bitmap getBitmapFromResource(Context context,int resId){
        return BitmapFactory.decodeResource(context.getResources(),resId);
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

    public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

}
