package com.houyi.notarization.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.SystemClock;

import com.jch.videolib.util.RecorderUtil;
import com.houyi.notarization.mode.Notarization;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

/**
 * @author changhua.jiang
 * @since 2018/11/7 5:57 PM
 */

public class NotaFileHelper {
    private static NotaFileHelper instance = new NotaFileHelper();
    String rootDir;

    private NotaFileHelper(){

    }

    public static NotaFileHelper getInstance(){
        return instance;
    }

    public void init(Context context){
        rootDir = context.getCacheDir().getAbsolutePath();
    }

    public String createDirIfNotExist(Notarization nota){
        String strDir ="nota" + "-" + nota.getNid();
        String path = rootDir + File.separator + strDir;
        FileUtils.createDirIfNotExist(path);
        return path;
    }

    public String createTxtResDirIfNotExist(Notarization nota){
        String root = createDirIfNotExist(nota);
        String path = root + File.separator + "txt";
        FileUtils.createDirIfNotExist(path);
        return path;
    }

    public String createVideoDirIfNotExist(Notarization nota){
        String root = createDirIfNotExist(nota);
        String path = root + File.separator + "video";
        FileUtils.createDirIfNotExist(path);
        return path;
    }

    public String createAudioDirIfNotExist(Notarization nota){
        String root = createDirIfNotExist(nota);
        String path = root + File.separator + "audio";
        FileUtils.createDirIfNotExist(path);
        return path;
    }

    public String createImgDirIfNotExist(Notarization nota){
        String root = createDirIfNotExist(nota);
        String path = root + File.separator + "img";
        FileUtils.createDirIfNotExist(path);
        return path;
    }

    public void saveTextResource(Notarization nota,String src) throws IOException {
        String dir = createTxtResDirIfNotExist(nota);
        FileUtils.copyFileTo(src,dir);
    }

    public File[]listTxtResource(Notarization nota){
        String dir = createTxtResDirIfNotExist(nota);
        File dirFile = new File(dir);
        return dirFile.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        });
    }

    public void saveThumbBitmap(Notarization nota,String videoPath){
        Bitmap bmp = RecorderUtil.getThumbBitmap(videoPath);
        String path = createVideoDirIfNotExist(nota);
        File videoFile = new File(videoPath);
        String videoName = videoFile.getName();
        int index = videoName.lastIndexOf('.');
        String subName = videoName.substring(0,index);
        File bmpFile = new File(path,subName + ".png");
        BitmapUtils.saveBmp(bmp,bmpFile);
    }


    public File[] listVideoFiles(Notarization nota){
        String path = createVideoDirIfNotExist(nota);
        return FileUtils.listFileByType(new File(path),".mp4");
    }

    public void saveImage(Notarization nota,Bitmap bmp){
        String path = createImgDirIfNotExist(nota);
        long time = SystemClock.currentThreadTimeMillis();
        String name = path + File.separator + time + ".png";
        BitmapUtils.saveBmp(bmp,new File(name));
        String thumb = path +  File.separator + time + ".thumb.png";
        Bitmap newBmp = BitmapUtils.zoomImg(bmp,160,160);
        BitmapUtils.saveBmp(newBmp,new File(thumb));
        newBmp.recycle();
    }

    public File[] listAudioFiles(Notarization nota) {
        String path = createAudioDirIfNotExist(nota);
        return FileUtils.listFileByType(new File(path),".wav");
    }

    public File[] listImgFiles(Notarization nota){
        String path = createImgDirIfNotExist(nota);
        return FileUtils.listFileByType(new File(path),".png");
    }

    public String createNewVideoFile(Notarization nota) throws IOException {
        String path = createVideoDirIfNotExist(nota);
        path = path + File.separator + SystemClock.currentThreadTimeMillis() + ".mp4";
        FileUtils.createNewFile(path);
        return path;
    }

    public String createNewAudioFile(Notarization nota) throws IOException {
        String path = createAudioDirIfNotExist(nota);
        path = path + File.separator + SystemClock.currentThreadTimeMillis() + ".wav";
        FileUtils.createNewFile(path);
        return path;
    }

    public void deleteVideoFile(File file){
        String videoName = file.getName();
        int index = videoName.lastIndexOf('.');
        if(index > 0) {
            String pngName = videoName.substring(0, index) + ".png";
            FileUtils.delete(new File(file.getParent()),pngName);
        }
        FileUtils.delete(file);
    }

    public void deleteAudioFile(File file){
        FileUtils.delete(file);
    }

    public void deleteTextFile(File file){
        FileUtils.delete(file);
    }

    public void deleteImage(File file){

    }

}
