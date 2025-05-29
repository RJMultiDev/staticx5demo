package com.tencent.tbs.demo.utils;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    public static void writeBytesToFile(InputStream is, File file) throws IOException {
        if (file.exists()) {
            return;
        }
        FileOutputStream fos = null;
        try {
            byte[] data = new byte[2048];
            int nbread = 0;
            fos = new FileOutputStream(file);
            while ((nbread = is.read(data)) > -1) {
                fos.write(data, 0, nbread);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    public static String getFileType(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return null;
    }


    /**
     * 复制Assets目录到sdcard中
     *
     * @param context Application Context
     * @param assetsFilePath 源文件assets中的路径
     * @param sdcardDir sdcard目录名
     * @param targetFileName 目标文件名
     * @return 目标文件的File对象
     */
    public static File copyAssetsToSDCard(Context context, String assetsFilePath, String sdcardDir,
            String targetFileName) {
        File target = new File(context.getExternalFilesDir(sdcardDir), targetFileName);
        return copyAssetsToTargetFile(context, assetsFilePath, target);
    }

    /**
     * 复制Assets目录到sdcard中
     *
     * @param context Application Context
     * @param assetsFilePath 源文件assets中的路径
     * @param dataDir sdcard目录名
     * @param targetFileName 目标文件名
     * @return 目标文件的File对象
     */
    public static File copyAssetsToDataDir(Context context, String assetsFilePath, String dataDir,
            String targetFileName) {
        File target = new File(context.getDir(dataDir, Context.MODE_PRIVATE), targetFileName);
        return copyAssetsToTargetFile(context, assetsFilePath, target);
    }

    /**
     * 复制Assets目录到sdcard中
     *
     * @param context Application Context
     * @param assetsFilePath 源文件assets中的路径
     * @param dataDir sdcard目录名
     * @param targetFileName 目标文件名
     * @return 目标文件的File对象
     */
    public static File copyAssetsToOtherContextDataDir(Context myContext, Context context, String assetsFilePath, String dataDir,
            String targetFileName) {
        File target = new File(context.getDir(dataDir, Context.MODE_PRIVATE), targetFileName);
        return copyAssetsToTargetFile(myContext, assetsFilePath, target);
    }

    /**
     * 复制Assets目录到sdcard中
     *
     * @param context Application Context
     * @param assetsFilePath 源文件assets中的路径
     * @param target 目标文件
     * @return 目标文件的File对象
     */
    public static File copyAssetsToTargetFile(Context context, String assetsFilePath, File target) {
        AssetManager assetManager = context.getAssets();
        InputStream is = null;
        if (target.exists()) {
            return target;
        }

        try {
            String[] lists = assetManager.list("");
            is = assetManager.open(assetsFilePath);
            FileUtil.writeBytesToFile(is, target);
        } catch (Exception e) {
            target = null;
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return target;
    }
}
