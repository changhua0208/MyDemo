package com.jch.mydemo.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
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
}
