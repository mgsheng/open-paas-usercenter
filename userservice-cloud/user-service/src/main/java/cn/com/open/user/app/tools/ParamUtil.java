package cn.com.open.user.app.tools;

import java.util.List;

public class ParamUtil {

    /**
     *
     * 检验参数是否为空
     * @param params
     * @return
     */
    public static boolean paramMandatoryCheck(List<String> params){
        for(String param:params){
            if(nullEmptyBlankJudge(param)){
                return false;
            }
        }
        return true;
    }
    /**
     * 检验字符串是否为空
     * @param str
     * @return
     */
    public static boolean nullEmptyBlankJudge(String str){
        return null == str || str.isEmpty() || "".equals(str.trim());
    }

    /**
     * 校验文件类型
     *
     * @param fileName 文件名
     * @param imageTypes 文件类型字符串
     * @return 是否通过校验
     */
    public static boolean checkFileType(String fileName, String imageTypes) {
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        return imageTypes.contains(suffix.trim().toLowerCase());
    }

}
