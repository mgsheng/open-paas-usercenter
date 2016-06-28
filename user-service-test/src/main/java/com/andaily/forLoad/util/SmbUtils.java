package com.andaily.forLoad.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;


import org.apache.log4j.Logger;


public class SmbUtils {
 /*   *//**
     * Logger 日志
     *//*
    public static Logger logger = Logger.getLogger(CommonUtils.class);

    *//***
     * 取得SMB下的所有文件
     * 
     * @param smbdir
     * @return
     *//*
    public static SmbFile[] lisDir(String smbdir) {
        try {
            SmbFile rmifile = new SmbFile(smbdir + "/");
            return rmifile.listFiles();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.error("MalformedURLException无法访问共享目录", e);
            return null;
        } catch (SmbException e) {
            e.printStackTrace();
            logger.error("SmbException无法访问共享目录", e);
            return null;
        }
    }

    *//***
     * 读取SMB File到绝对目录下
     * 
     * @param rmifile
     *            路径
     * @param localpath
     * @return File
     *//*
    public static File readFromSmb(SmbFile rmifile, String localpath) {
        File localfile = null;
        InputStream bis = null;
        OutputStream bos = null;
        String filename = "";
        try {
            filename = rmifile.getName();
            bis = new BufferedInputStream(new SmbFileInputStream(rmifile));
            localfile = new File(localpath + File.separator + filename);
            bos = new BufferedOutputStream(new FileOutputStream(localfile));
            int length = rmifile.getContentLength();
            byte[] buffer = new byte[length];

            bis.read(buffer);
            bos.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("ReadSmb---readFromSmb---filename="+filename, e);
            return null;
        } finally {
            try {
                if (bos != null)
                    bos.close();
                if (bis != null)
                    bis.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("ReadSmb---readFromSmb---", e);
            }
        }
        return localfile;
    }

    *//***
     * 读取File到SMB
     * 
     * @param file
     * @param rmifile
     * @return 1:正常结束
     *//*
    public static int writeToSmb(File file, String smbdir) {
        BufferedInputStream bis = null;
        SmbFileOutputStream out = null;
        try {
            if (file == null || !file.exists()) {
                return -1;
            }
            SmbFile smbFileOut = new SmbFile(smbdir);
            if (!smbFileOut.exists()) {
                smbFileOut.mkdirs();
            }
            smbFileOut = new SmbFile(smbdir + File.separator + file.getName());
            if (!smbFileOut.exists()) {
                smbFileOut.createNewFile();
            }
            out = new SmbFileOutputStream(smbFileOut);
            //
            bis = new BufferedInputStream(new FileInputStream(file));
            Long length = file.length();
            byte[] buffer = new byte[length.intValue()];

            bis.read(buffer);
            out.write(buffer);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("ReadSmb---readFromSmb---", e);
            return -99;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                logger.error("ReadSmb---readFromSmb--out.close()-", e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        try {
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("ReadSmb---readFromSmb---", e);
        }
    }*/
}
