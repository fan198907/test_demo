/**
 * 
 */
package com.android.cache;

/**
 * @author Administrator
 *
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.text.TextUtils;

public class MD5Utils {

    private static final String TAG = MD5Utils.class.getSimpleName();

    public static final int BUFFER_SIZE = 4 * 1024;

    public static String md5Bytes(byte[] data) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(data, 0, data.length);
            return bytesToString(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * md5File ：取文件md5值
     * 
     * @param FILEPATH
     *            文件全路径
     * @return md5串
     */
    public static String md5File(File file) {
        FileInputStream fis = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            return bytesToString(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                    fis = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String bytesToString(byte[] data) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        char[] temp = new char[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            byte b = data[i];
            temp[i * 2] = hexDigits[b >>> 4 & 0x0f];
            temp[i * 2 + 1] = hexDigits[b & 0x0f];
        }
        return new String(temp);
    }

    /**
     * md5String ：计算md5，是按照和clock mod recovery的 rom manager同样的做法计算的
     * 如果首位是0，会被直接忽略
     * 
     * @param str
     * @return
     */
    public static String md5String(String str) {
        try {
            byte[] strBytes = str.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(strBytes);
            BigInteger bigInt = new BigInteger(1, md5.digest());
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * md5String32 ：计算md5，即使首位是0，不被忽略掉
     * 
     * @param str
     * @return
     */
    public static String md5String32(String str) {
        try {
            byte[] strBytes = str.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(strBytes);
            return bytesToString(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 校验文件的MD5是否一致 */
    public static boolean isFileMd5Ok(String filePath, String fileMd5) {
        boolean isOk = false;

        // 校验md5
        if (!TextUtils.isEmpty(filePath) && !TextUtils.isEmpty(fileMd5)) {
            File file = new File(filePath);
            if (file.exists()) {
                long startTime = System.currentTimeMillis();
                String md5 = MD5Utils.md5File(file);
//                HjLog.i(TAG, "isFileMd5Ok, filePath:" + filePath + ";costTime:" + (System.currentTimeMillis() - startTime));
                if (!fileMd5.equalsIgnoreCase(md5)) {
                    isOk = false;
                    file.delete(); // 删除文件
                    // md5校验
//                    HjLog.i(TAG, "isFileMd5Ok not same, filePath:" + filePath + ";fileMd5:" + fileMd5 + ";md5:" + md5);
                } else {
//                    HjLog.i(TAG, "isFileMd5Ok same, filePath:" + filePath + ";fileMd5:" + fileMd5 + ";md5:" + md5);
                    isOk = true;
                }
            }
        }

        return isOk;
    }
}

