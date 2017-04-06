package cn.com.open.openpaas.userservice.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP压缩字符串.
 * 1. [2017/4/6][16:03]创建文件 by jh.
 */
public class GzipUtil {
    // 压缩
    public static String compress(String str){
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try{
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes());
            gzip.close();
            return out.toString("UTF-8");
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    // 解压缩
    public static String uncompress(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("UTF-8"));
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            // toString()使用平台默认编码，也可以显式的指定如toString(&quot;GBK&quot;)
            return out.toString();
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
