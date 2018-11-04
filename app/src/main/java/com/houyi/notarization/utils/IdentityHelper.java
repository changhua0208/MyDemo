package com.houyi.notarization.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.houyi.notarization.mode.Identity;

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
    public boolean savePhoto(Identity identity, Bitmap image){
        File dir = checkIdentityDir(identity);
        File dstFile = new File(dir,"photo.png");
        if(dstFile.exists()){
            FileUtils.delete(dstFile);
        }
        return BitmapUtils.saveBmp(image,dstFile);
    }
    //获得现场照
    public Bitmap getPhoto(Identity identity){
        File dir = checkIdentityDir(identity);
        File dstFile = new File(dir.getAbsolutePath(),"photo.png");
        if(dstFile.exists()){
            Bitmap bmp = BitmapUtils.getBitmapFromFile(dstFile);
            return bmp;
        }
        else{
            return null;
        }
    }

    //获得已认证的人脸照片
    public Bitmap getVerifiedFace(Identity identity){
        File dir = checkIdentityDir(identity);
        File dstFile = new File(dir,"verified_face.png");
        if(dstFile.exists()) {
            return BitmapUtils.getBitmapFromFile(dstFile);
        }
        else{
            return null;
        }
    }
    //保存人脸认证照片
    public boolean saveVerfiedFace(Identity identity,Bitmap bmp){
        File dir = checkIdentityDir(identity);
        File dstFile = new File(dir,"verified_face.png");
        if(dstFile.exists())
            FileUtils.delete(dstFile);
        return BitmapUtils.saveBmp(bmp,dstFile);
    }

    //保存证件照头像
    public boolean saveHeadImage(Identity identity,Bitmap bmp){
        File dir = checkIdentityDir(identity);
        File dstFile = new File(dir,"head.png");
        return BitmapUtils.saveBmp(bmp,dstFile);
    }
    //获得证件照头像
    public Bitmap getHeadImage(Identity identity){
        File dir = checkIdentityDir(identity);
        File dstFile = new File(dir,"head.png");
        if(dstFile.exists()){
            Bitmap bmp = BitmapUtils.getBitmapFromFile(dstFile);
            return bmp;
        }
        else{
            return null;
        }
    }

    //保存身份证中的指纹特征
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

    //加载身份证中的资源【头像、指纹】
    public void loadIdentityResource(Identity identity){
        File dir = checkIdentityDir(identity);
        File dstFile = new File(dir,"head.png");
        try {
            byte[] data = FileUtils.readByteArray(dstFile);
            identity.setImage(data);
        } catch (IOException e) {
            Log.e(TAG,"loadIdentityResource error",e);
        }

        String fp1Name = identity.getFp1Name();
        if(!TextUtils.isEmpty(fp1Name)){
            dstFile = new File(dir,fp1Name + ".feature");
            if(dstFile.exists()){
                try {
                    String data = FileUtils.read(dstFile);
                    identity.setFp1(data);
                } catch (IOException e) {
                    Log.e(TAG,"loadIdentityResource error",e);
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
                    Log.e(TAG,"loadIdentityResource error",e);
                }
            }
        }
    }

    //保存已采集的指纹图片
    //约定1-5为左手大拇指到小拇指，6-10为右手大拇指到小拇指
    public boolean saveFp(Identity identity,Bitmap bmp,int finger){
        File dir = checkIdentityDir(identity);
        File dstFile = new File(dir,finger + ".png");
        if(dstFile.exists()){
            dstFile.delete();
        }
        return BitmapUtils.saveBmp(bmp,dstFile);
    }
    //保存已采集的指纹特征
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

    //获得已采集的所有指纹图片
    public void getFpImage(Identity identity,Bitmap[] bmps){
        File dir = checkIdentityDir(identity);
        for(int i = 1;i <= 10;i++) {
            File dstFile = new File(dir, i + ".png");
            if(dstFile.exists()){
                bmps[i - 1] = BitmapUtils.getBitmapFromFile(dstFile);
            }
        }
    }

    //获得已采集的指纹图片
    public Bitmap getFpImageByIndex(Identity identity,int index){
        File dir = checkIdentityDir(identity);
        File dstFile = new File(dir, index + ".png");
        if(dstFile.exists()){
            return BitmapUtils.getBitmapFromFile(dstFile);
        }
        return null;
    }

    //获得已采集的指纹特征
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

    //保存身份证图片
    public void saveIdentityView(Identity identity, View identityFront,View identityBack){
        File dir = checkIdentityDir(identity);
        Bitmap bmp = BitmapUtils.getBitmapFromView(identityFront);
        File dstFile = new File(dir,"identity_front.png");
        if(dstFile.exists())
            FileUtils.delete(dstFile);
        BitmapUtils.saveBmp(bmp,dstFile);

        bmp.recycle();
        bmp = BitmapUtils.getBitmapFromView(identityBack);
        dstFile = new File(dir,"identity_back.png");
        if(dstFile.exists())
            FileUtils.delete(dstFile);
        BitmapUtils.saveBmp(bmp,dstFile);
        bmp.recycle();

    }

    public Bitmap[] getIdentityCardImage(Identity identity){
        File dir = checkIdentityDir(identity);
        File dstFile1 = new File(dir,"identity_front.png");
        File dstFile2 = new File(dir,"identity_back.png");
        if(!dstFile1.exists() || !dstFile2.exists()){
            return null;
        }
        else{
            Bitmap[] bmps = new Bitmap[2];
            bmps[0] = BitmapUtils.getBitmapFromFile(dstFile1);
            bmps[1] = BitmapUtils.getBitmapFromFile(dstFile2);
            return bmps;
        }
    }


    private File checkIdentityDir(Identity identity){
        File dir = new File(rootDir,identity.getIdentityNo());
        if(!dir.exists()){
            dir.mkdir();
        }
        return dir;
    }

    public void deleteIdentityFiles(Identity identity){
        File dir = checkIdentityDir(identity);
        FileUtils.delete(dir);
    }

}
