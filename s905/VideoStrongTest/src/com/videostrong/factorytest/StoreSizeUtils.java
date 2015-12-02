package com.videostrong.factorytest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

public class StoreSizeUtils {

    private static final int ERROR = -1;

    /**
     * SDCARDÊÇ·ñ´æ
     */
    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * »ñÈ¡ÊÖ»úÄÚ²¿Ê£Óà´æ´¢¿Õ¼ä
     * 
     * @return
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * »ñÈ¡ÊÖ»úÄÚ²¿×ÜµÄ´æ´¢¿Õ¼ä
     * 
     * @return
     */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * »ñÈ¡SDCARDÊ£Óà´æ´¢¿Õ¼ä
     * 
     * @return
     */
    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    /**
     * »ñÈ¡SDCARD×ÜµÄ´æ´¢¿Õ¼ä
     * 
     * @return
     */
    public static long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    /**
     * »ñÈ¡ÏµÍ³×ÜÄÚ´æ
     * 
     * @param context ¿É´«ÈëÓ¦ÓÃ³ÌÐòÉÏÏÂÎÄ¡£
     * @return ×ÜÄÚ´æ´óµ¥Î»ÎªB¡£
     */
    public static long getTotalMemorySize(Context context) {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            return Integer.parseInt(subMemoryLine.replaceAll("\\D+", "")) * 1024l;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
     public static long getSize(String str) {
        File file = null;
        if (str != null) {
            file = new File(str);
            StatFs stat = new StatFs(file.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        }else {
            return ERROR;
        }

    }
    /**
     * »ñÈ¡µ±Ç°¿ÉÓÃÄÚ´æ£¬·µ»ØÊý¾ÝÒÔ×Ö½ÚÎªµ¥Î»¡£
     * 
     * @param context ¿É´«ÈëÓ¦ÓÃ³ÌÐòÉÏÏÂÎÄ¡£
     * @return µ±Ç°¿ÉÓÃÄÚ´æµ¥Î»ÎªB¡£
     */
    public static long getAvailableMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    private static DecimalFormat fileIntegerFormat = new DecimalFormat("#0");
    private static DecimalFormat fileDecimalFormat = new DecimalFormat("#0.#");

    /**
     * µ¥Î»»»Ëã
     * 
     * @param size µ¥Î»ÎªB
     * @param isInteger ÊÇ·ñ·µ»ØÈ¡ÕûµÄµ¥Î»
     * @return ×ª»»ºóµÄµ¥Î»
     */
    public static String formatFileSize(long size, boolean isInteger) {
        DecimalFormat df = isInteger ? fileIntegerFormat : fileDecimalFormat;
        String fileSizeString = "0M";
        if (size < 1024 && size > 0) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1024 * 1024) {
            fileSizeString = df.format((double) size / 1024) + "K";
        } else if (size < 1024 * 1024 * 1024) {
            fileSizeString = df.format((double) size / (1024 * 1024)) + "M";
        } else {
            fileSizeString = df.format((double) size / (1024 * 1024 * 1024)) + "G";
        }
        return fileSizeString;
    }
}