package com.andaily.forLoad.util;

import java.io.File;
import java.io.FileFilter;

public class ComFileFilter implements FileFilter {
    
    public String str_ext = "";
    
    /***
     * 文件扩展名 包含‘.’(点)
     * .xls
     * @param contains
     */
    public ComFileFilter(String ext){
        str_ext = ext;
    }

    /***
     * 过滤文件扩展名
     * 
     */
    public boolean accept(File pathname) {
        String filename = pathname.getName().toLowerCase();
//        if (filename.contains(".xls")) {
//            return false;
//        } else {
//            return true;
//        }
        return filename.contains(str_ext);
    }
}
