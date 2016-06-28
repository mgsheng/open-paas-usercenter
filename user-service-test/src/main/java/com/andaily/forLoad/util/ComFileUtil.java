package com.andaily.forLoad.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ComFileUtil extends FatherCommon {
    /**
     * Logger
     * 日志
     */
    public static Logger logger = Logger.getLogger(CommonUtils.class);

	/***************************************************************************
	 * 取得文件名 在指定目录下的指定文件类型
	 * 
	 * @param path
	 *            绝对路径
	 * @param ext
	 *            扩展名（包含点），为空则取得所有文件
	 * @param isAllPath
	 *            是否返回全路径名
	 * @return List<String>
	 * @throws IOException
	 */
	public static List<String> getFileNameList(String path, String ext,
			boolean isAllPath) throws IOException {
		File file; //
		File[] fs;
		List<String> fileNames = new ArrayList<String>();
		if (ValidateUtil.isNullString(path)) {
			return null;
		}
		file = new File(path);
		if (file.isDirectory()) {
			if (!ValidateUtil.isNullString(ext)) {
				// 指定扩展名的文件
				fs = file.listFiles(new ComFileFilter(ext.toLowerCase()));
			} else {
				// 所有文件
				fs = file.listFiles();
			}
			//
			for (File f : fs) {
				if (isAllPath) {
					// 全路径文件名
					fileNames.add(f.getPath());
				} else {
					// 文件名
					fileNames.add(f.getName());
				}
			}
		}
		return fileNames;
	}

	/***************************************************************************
	 * 取得文件 在指定目录下的指定文件类型
	 * 
	 * @param path
	 *            绝对路径
	 * @param ext
	 *            扩展名（包含点），为空则取得所有文件
	 * @return List<String>
	 * @throws IOException
	 */
	public static File[] getFiles(String path, String ext) throws IOException {
		File file; //
		File[] fs = null;
		if (ValidateUtil.isNullString(path)) {
			return null;
		}
		file = new File(path);
		if (file.isDirectory()) {
			if (!ValidateUtil.isNullString(ext)) {
				// 指定扩展名的文件
				fs = file.listFiles(new ComFileFilter(ext));
			} else {
				// 所有文件
				fs = file.listFiles();
			}
		}
		return fs;
	}

    /***
     * 写文件
     * @param filePath 文件路径
     * @param filename 文件名称，如：a.txt
     * @param filecontent 文件内容
     * @param append 是否追加在文件尾
     * @return 文件全路径名
     */
    public static String write_file(String filePath, String filename, String filecontent,boolean append) {

        FileWriter fw = null;
        String filename_all = "";
        try {
            filename_all = filePath;
            File f = new File(filePath);
            if(!f.exists()){
                f.mkdirs();
            }
            filename_all += File.separator + filename;
            fw = new FileWriter(new File(filename_all),append);
            long begin = System.currentTimeMillis();
            fw.write(filecontent);
            long end = System.currentTimeMillis();
            logger.debug("CommonUtils-----write_file-"+filename_all+"--执行耗时:" + (end - begin) + "豪秒");
            return filename_all;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("CommonUtils-----write_file---异常", e);
            return null;
        } finally {
            try {
                fw.flush();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("CommonUtils-----write_file--finally-异常", e);
            }
        }

    }

    /***
     * 复制文件到指定
     * @param file 文件
     * @param filepath 备份目录
     * @param append 是否追加在文件尾
     * @return 文件全路径名
     */
    public static String bak_file(File file, String filepath,boolean append) {

        OutputStream bos = null;
        BufferedInputStream bis = null;
        String filename_all = "";
        long begin = 0;
        try {
            begin = System.currentTimeMillis();
            filename_all = filepath;
            File f = new File(filepath);
            if(!f.exists()){
                f.mkdirs();
            }
            filename_all += File.separator + file.getName();
            //
            bis = new BufferedInputStream(new FileInputStream(file));
            Long length = file.length();
            byte[] buffer = new byte[length.intValue()];
            bis.read(buffer);
            bos = new BufferedOutputStream(new FileOutputStream(new File(filename_all),append));
            bos.write(buffer);
            return filename_all;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("CommonUtils-----write_file---异常", e);
            return null;
        } finally {
            long end = System.currentTimeMillis();
            logger.debug("CommonUtils-----write_file-"+filename_all+"--执行耗时:" + (end - begin) + "豪秒");
            try {
                bos.flush();
                bos.close();
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("CommonUtils-----write_file--finally-异常", e);
            }
        }

    }
}
