package com.jch.mydemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.jch.mydemo.mode.Identity;

import java.io.File;
import java.io.IOException;

/**
 * @author changhua.jiang
 * @since 2018/9/13 上午11:50
 */

public class IdentityHelper {
    private static final String TAG = "IdentityHelper";

    private static IdentityHelper instance = new IdentityHelper();

    private Context mContext;
    private String rootDir;

    private IdentityHelper(){

    }

    public static IdentityHelper getInstance(){
        return instance;
    }

    public void init(Context context){
        this.mContext = context;
        rootDir = this.mContext.getCacheDir().getAbsolutePath();
    }

    //现场照保存一张
    public boolean savePhoto(Identity identity, File image){
        File dir = checkIdentityDir(identity);
        File dstFile = new File(dir.getAbsolutePath(),"photo.jpg");
        try {
            if(dstFile.exists())
                FileUtils.delete(dstFile);
            FileUtils.copyFile(image,dstFile);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public Bitmap getPhoto(Identity identity){
        File dir = checkIdentityDir(identity);
        File dstFile = new File(dir.getAbsolutePath(),"photo.jpg");
        if(dstFile.exists()){
            Bitmap bmp = BitmapFactory.decodeFile(dstFile.getAbsolutePath());
            return bmp;
        }
        else{
            return null;
        }
    }

    public boolean saveHeadImage(Identity identity,Bitmap bmp){
        File dir = checkIdentityDir(identity);
        File dstFile = new File(dir,"head.png");
        return BitmapUtils.saveBmp(bmp,dstFile);
    }

    public Bitmap getHeadImage(Identity identity){
        File dir = checkIdentityDir(identity);
        File dstFile = new File(dir,"head.png");
        if(dstFile.exists()){
            Bitmap bmp = BitmapFactory.decodeFile(dstFile.getAbsolutePath());
            return bmp;
        }
        else{
            return null;
        }
    }

    public void saveIdentityFpFeature(Identity identity){
        File dir = checkIdentityDir(identity);
        if(!TextUtils.isEmpty(identity.getFp1())){
            File dstFile = new File(dir,identity.getFp1Name() +  ".feature");
            try {
                FileUtils.write(dstFile,identity.getFp1().getBytes(),false);
            } catch (IOException e) {
                Log.e(TAG,"saveIdentityFpFeature",e);
                return;
            }
        }

        if(!TextUtils.isEmpty(identity.getFp2())){
            //int index = getIndexByFpName(identity.getFp2Name());
            File dstFile = new File(dir,identity.getFp2Name() + ".feature");
            try {
                FileUtils.write(dstFile,identity.getFp2().getBytes(),false);
            } catch (IOException e) {
                Log.e(TAG,"saveIdentityFpFeature",e);
                return;
            }
        }
    }

    public void loadIdentityImages(Identity identity){
        File dir = checkIdentityDir(identity);
        File dstFile = new File(dir,"head.png");
        try {
            byte[] data = FileUtils.readByteArray(dstFile);
            identity.setImage(data);
        } catch (IOException e) {
            Log.e(TAG,"loadIdentityImages error",e);
        }

        String fp1Name = identity.getFp1Name();
        if(!TextUtils.isEmpty(fp1Name)){
            dstFile = new File(dir,fp1Name + ".feature");
            if(dstFile.exists()){
                try {
                    String data = FileUtils.read(dstFile);
                    identity.setFp1(data);
                } catch (IOException e) {
                    Log.e(TAG,"loadIdentityImages error",e);
                }
            }
        }

        String fp2Name = identity.getFp2Name();
        if(!TextUtils.isEmpty(fp2Name)){
            dstFile = new File(dir,fp2Name + ".feature");
            if(dstFile.exists()){
                try {
                    String data = FileUtils.read(dstFile);
                    identity.setFp2(data);
                } catch (IOException e) {
                    Log.e(TAG,"loadIdentityImages error",e);
                }
            }
        }
    }


    //约定1-5为左手大拇指到小拇指，6-10为右手大拇指到小拇指
    public boolean saveFp(Identity identity,Bitmap bmp,int finger){
        File dir = checkIdentityDir(identity);
        File dstFile = new File(dir,finger + ".png");
        if(dstFile.exists()){
            dstFile.delete();
        }
        return BitmapUtils.saveBmp(bmp,dstFile);
    }

    public boolean saveFpFeature(Identity identity,String feature,int finger){
        File dir = checkIdentityDir(identity);
        File dstFile = new File(dir,finger + ".feature");
        if(dstFile.exists()){
            dstFile.delete();
        }
        try {
            FileUtils.write(dstFile,feature.getBytes(),false);
        } catch (IOException e) {
           Log.e(TAG,"saveFpFeature",e);
           return false;
        }
        return true;
    }

    public void getFpImage(Identity identity,Bitmap[] bmps){
        File dir = checkIdentityDir(identity);
        for(int i = 1;i <= 10;i++) {
            File dstFile = new File(dir, i + ".png");
            if(dstFile.exists()){
                bmps[i - 1] = BitmapFactory.decodeFile(dstFile.getAbsolutePath());
            }
        }
    }

    public Bitmap getFpImageByIndex(Identity identity,int index){
        File dir = checkIdentityDir(identity);
        File dstFile = new File(dir, index + ".png");
        if(dstFile.exists()){
            return BitmapFactory.decodeFile(dstFile.getAbsolutePath());
        }
        return null;
    }

    public String getFpFeatureByIndex(Identity identity, int index){
        File dir = checkIdentityDir(identity);
        File dstFile = new File(dir, index + ".feature");
        if(dstFile.exists()){
            try {
                return FileUtils.read(dstFile);
            } catch (IOException e) {
                Log.e(TAG,"getFpFeatureByIndex",e);
            }
        }
        return null;
    }


    private File checkIdentityDir(Identity identity){
        File dir = new File(rootDir,identity.getIdentityNo());
        if(!dir.exists()){
            dir.mkdir();
        }
        return dir;
    }

}
