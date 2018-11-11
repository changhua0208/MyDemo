package com.houyi.notarization.utils;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import okio.BufferedSource;
import okio.Okio;
import okio.Source;

/**
 * @author changhua.jiang
 */

public class FileUtils {

    private static final String TAG = "FileUtils";

    private static String[][] MIME_MapTable = {
            //{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    public static void openFile(Context context, File file) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            //获取文件file的MIME类型
            String type = getMIMEType(file);
            //设置intent的data和Type属性。
            intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
            //跳转
            context.startActivity(intent);
//	    Intent.createChooser(intent, "请选择对应的软件打开该附件！");
        } catch (ActivityNotFoundException e) {
            // TODO: handle exception
            Toast.makeText(context, "sorry附件不能打开，请下载相关软件！", Toast.LENGTH_SHORT).show();
        }
    }

    private static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
	    /* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {

            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }


    public static void createDirIfNotExist(String path){
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public static void copyFile(File src, File dst) throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(src);
            copyFileFrom(in, dst);
        }
        finally {
            closeQuietly(in);
        }
    }

    public static void copyFileFrom(InputStream in, File dst) throws IOException {
        OutputStream out = null;
        try {
            int maxBuffSize = 10 * 1024;
            out = new FileOutputStream(dst);
            int len = 0;
            byte[] buff = new byte[maxBuffSize];//10k
            while ((len = in.read(buff)) >= 0) {
                out.write(buff, 0, len);
            }
            out.flush();
        }
        finally {
            closeQuietly(out);
        }

    }

    public static void copyFileFrom(InputStream in, String dst) throws IOException {
        File file = new File(dst);
        copyFileFrom(in, file);
    }

    public static String read(File file, String charset) throws IOException {
        if (!file.exists()) {
            return null;
        }
        BufferedSource bufferedSource = null;
        try {
            Source source = Okio.source(file);
            bufferedSource = Okio.buffer(source);
            String ret = bufferedSource.readString(Charset.forName(charset));
            //bufferedSource.close();
            return ret;
        }
        finally {
            closeQuietly(bufferedSource);
        }
    }

    public static void copyFileTo(String from,String to) throws IOException{
//        int index = from.lastIndexOf(File.separator);
//        if(index < 0){
//            throw new FileNotFoundException("unknown file" + from);
//        }
//        String fileName = from.substring(index);
//        String fullName = to + File.separator + fileName;
        File src = new File(from);
        if(!src.exists()){
            throw new FileNotFoundException(from);
        }
        String name = src.getName();
        String fullName = to + File.separator + name;
        File dst = new File(fullName);
        if(dst.exists()){
            dst.delete();
        }
        dst.createNewFile();
        copyFile(src,dst);
    }

    public static byte[] readByteArray(File file) throws IOException {
        if (!file.exists()) {
            return null;
        }
        BufferedSource bufferedSource = null;
        try {
            Source source = Okio.source(file);
            bufferedSource = Okio.buffer(source);
            byte[] ret = bufferedSource.readByteArray();
            return ret;
        }
        finally {
            closeQuietly(bufferedSource);
        }
    }

    public static String read(File file) throws IOException {
        return read(file,"utf-8");
    }

    public static String read(String filePath, String charset) throws IOException {
        File file = new File(filePath);
        return read(file, charset);
    }

    public static String read(String filePath) throws IOException {
        File file = new File(filePath);
        return read(file);
    }

    //深度优先，删除文件或文件夹，其中有一次删除失败，就认为这次删除失败
    public static boolean delete(File file) {
        boolean success = true;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : file.listFiles()) {
                    success = success & delete(f);
                }
            } else {
                success = success & file.delete();
            }
        } else if (file.isFile()) {
            success = success & file.delete();
        }
        return success;
    }

    public static boolean delete(String filePath) {
        File file = new File(filePath);
        return delete(file);
    }

    //删除suffix后缀的文件
    public static void delete(File file, String suffix) {
        if (file.isFile()) {
            if (file.getName().endsWith("." + suffix)) {
                file.delete();
            }
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : file.listFiles()) {
                    delete(f, suffix);
                }
            }
        }
    }

    public static void unzip(File zipFile, String location) throws IOException {
        if (!zipFile.isDirectory()) {
            zipFile.mkdirs();
        }
        if (!location.endsWith("/")) {
            location += "/";
        }
        ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
        try {
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                String path = location + ze.getName();

                if (ze.isDirectory()) {
                    File unzipFile = new File(path);
                    if (!unzipFile.isDirectory()) {
                        unzipFile.mkdirs();
                    }
                } else {
//                    FileOutputStream fout = new FileOutputStream(path, false);
                    File dstFile = new File(path);
                    try {
                        copyFileFrom(zin, dstFile);
                        zin.closeEntry();
                    } finally {
                        //fout.close();
                    }
                }
            }
        } finally {
            zin.close();
        }

    }

    public static void write(File file, InputStream inputStream, int offset,int length) throws IOException {
        if (file == null) {
            return;
        }
        int maxBuffSize = 10 * 1024;
        OutputStream out = null;
        try{
            out = new FileOutputStream(file);
            int len = 0;
            int count = 0;
            byte[] buff = new byte[maxBuffSize];//10k
            if (offset > 0)
                inputStream.skip(offset);
            while ((len = inputStream.read(buff)) >= 0) {
                if ((count + len) > length) {
                    out.write(buff, 0, length - count);
                    break;
                }
                out.write(buff, 0, len);
                count += len;
            }
            out.flush();
        }
        finally {
            closeQuietly(out);
        }
    }

    public static void writeAllInputStream(File file, InputStream inputStream) throws IOException {
        if (file == null) {
            return;
        }
        int maxBuffSize = 10 * 1024;
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            int len;
            byte[] buff = new byte[maxBuffSize];//10k
            while ((len = inputStream.read(buff)) >= 0) {
                out.write(buff, 0, len);
            }
            out.flush();
        }
        finally {
            closeQuietly(out);
        }
    }

    public static void write(File file,byte[] data,boolean append) throws IOException {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file, append);
            out.write(data);
            out.flush();
        }
        finally {
            closeQuietly(out);
        }

    }

    public static void unzip(String zipFile, String location) throws IOException {
        File f = new File(zipFile);
        unzip(f, location);
    }

    //判断文件是否存在
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void deleteFileSafely(@NonNull File file) {
        if (!file.delete()) {
            makeFileEmpty(file);
        }
    }

    public static void makeFileEmpty(@NonNull File file) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            closeQuietly(writer);
        }
    }

    public static void closeQuietly(Closeable... closeables) {
        if (closeables == null) {
            return;
        }

        for (Closeable closeable : closeables) {
            try {
                if (closeable != null) {
                    closeable.close();
                }
            } catch (IOException e) {
                // do nothing
            }
        }
    }

    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static File[] listFileByType(File parent,final String type){
        final String tmpType = type.toLowerCase();
        if(parent.exists() && parent.isDirectory()){
            return parent.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if(pathname.getName().toLowerCase().endsWith(tmpType)){
                        return true;
                    }
                    return false;
                }
            });
        }
        else{
            return null;
        }
    }

    public static File createNewFile(String path) throws IOException {
        File f = new File(path);
        if(f.exists())
            f.delete();
        f.createNewFile();
        return f;
    }
}
