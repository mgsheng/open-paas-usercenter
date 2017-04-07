package cn.com.open.openpaas.userservice.web;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
/**
 * TODO .
 * 项目名称 : ${CLASS_NAME}.
 * 创建日期 : 2017/4/7.
 * 创建时间 : 9:41.
 * 版本 : 1.0.
 * 修改历史 :
 * 1. [2017/4/7][9:41]创建文件 by jh.
 */
public class MessageGZIP {
    private static String encode = "utf-8";//"ISO-8859-1"

    public String getEncode() {
        return encode;
    }

    /*
     * 设置 编码，默认编码：UTF-8
     */
    public void setEncode(String encode) {
        MessageGZIP.encode = encode;
    }

    /*
     * 字符串压缩为字节数组
     */
    public static byte[] compressToByte(String str){
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(encode));
            gzip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    /*
     * 字符串压缩为字节数组
     */
    public static byte[] compressToByte(String str,String encoding){
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(encoding));
            gzip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    /*
     * 字节数组解压缩后返回字符串
     */
    public static String uncompressToString(byte[] b) {
        if (b == null || b.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(b);

        try {
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toString();
    }

    /*
     * 字节数组解压缩后返回字符串
     */
    public static String uncompressToString(byte[] b, String encoding) {
        if (b == null || b.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(b);

        try {
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString(encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String returnGzipString(String value){
       return value;//uncompressToString(compressToByte(value));
    }
}
/*
class test{
    public static void main(String[] args){
        String str = "{\"guid\":\"56739\",\"info\":\"有效张三李四王五\",\"sessiontime\":\"30\",\"status\":1,\"user\":\"{\\\"aesPassword\\\":\\\"xPOkwaY9Zng3Pj7W1WeVIQ==\\\",\\\"appId\\\":8,\\\"defaultUser\\\":false,\\\"id\\\":80004698,\\\"lastLoginTime\\\":1491525063000,\\\"md5Password\\\":\\\"64a62d49999fb141855f51ed3d58b5d0\\\",\\\"newly\\\":true,\\\"pid\\\":\\\"0\\\",\\\"privileges\\\":[],\\\"userState\\\":\\\"1\\\",\\\"username\\\":\\\"Admin81\\\"}\"}";
        System.out.println(MessageGZIP.uncompressToString(MessageGZIP.compressToByte(str)));
    }
}
*/
