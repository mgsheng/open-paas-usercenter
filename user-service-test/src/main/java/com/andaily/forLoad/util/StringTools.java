package com.andaily.forLoad.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

public class StringTools {

	/**
	 * 把任何大于0的int转换成2的n次方之和，返回和的集合
	 * @param no
	 * @return
	 */
	public static List<Integer> getParsedSum(int no){
		if(no <= 0){
//			throw new RuntimeException("The argument " + no + " should be greater than zero!");
			return null;
		}
		String str = Integer.toBinaryString(no);
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0 ,len = str.length() ; i < len; i++) {
			if(str.charAt(i)=='1'){
				list.add(1 << (len - i - 1));
			}
		}
		return list;
	}
	
	public static String getRandomString(int length) { //length表示生成字符串的长度  
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 }
	
	public static String bytesSubstring(String src, int start_idx, int end_idx){   
        byte[] b = src.getBytes();   
        String tgt = "";   
        for(int i=start_idx; i<=end_idx; i++){   
            tgt +=(char)b[i];   
        }   
        return tgt;   
    }
	
	/**
	 * 隐藏Email部分名称
	 * 
	 * @param email abcdefg@qq.com
	 * @return		abxxxfg@xx.com
	 */
	public static String emailHide(String email){
		String emailname = email.substring(0,email.indexOf("@"));
		String emaildomain = email.substring(email.indexOf("@")+1,email.indexOf("."));
		String exailsuffix = email.substring(email.indexOf(".")+1);
		StringBuffer x = new StringBuffer("");
		for(int i=0;i<emailname.substring(2, emailname.length()-2).length();i++){
			x.append("x");
		}
		emailname = emailname.substring(0,2)+x+emailname.substring(emailname.length()-2);
		x = new StringBuffer("");
		for(int i=0;i<emaildomain.length();i++){
			x.append("x");
		}
		emailname = emailname+"@"+x+"."+exailsuffix;
		return emailname;
	}
	
	/**
	 * 字符隐藏
	 * 1位字符不隐藏
	 * 2位只能隐藏一个字符
	 * 3-4位前后各留1位
	 * 5位以上前后各留2位
	 * @param str
	 * @return
	 */
	public static String hideString(String str){
		if(StringUtils.isBlank(str)){
			return "";
		}
		//1位字符不隐藏
		if(str.length()<=1){
			return str;
		}
		//2位只能隐藏一个字符
		else if(str.length()==2){
			return str.substring(0,1)+"*";
		}
		//3-4位前后各留1位
		else if(str.length()==3 || str.length()==4){
			StringBuffer stringBuffer = new StringBuffer("");
			stringBuffer.append(str.substring(0,1));
			for(int i=0;i<str.length()-2;i++){
				stringBuffer.append("*");
			}
			stringBuffer.append(str.substring(str.length()-1));
			return stringBuffer.toString();
		}
		//5位以上前后各留2位
		else if(str.length()>=5){
			StringBuffer stringBuffer = new StringBuffer("");
			stringBuffer.append(str.substring(0,2));
			for(int i=0;i<str.length()-4;i++){
				stringBuffer.append("*");
			}
			stringBuffer.append(str.substring(str.length()-2));
			return stringBuffer.toString();
		}
		return "";
	}
	
	/**
	 * 平均拆分数组（目前用于显示后台首页纵坐标轴）
	 * 如：
	 * min:0
	 * max:10
	 * count:5
	 * return:[0,2,4,6,8,10]
	 * @param min 最小值
	 * @param max 最大值
	 * @param count 拆成count份
	 * @return
	 */
	public static Integer[] intSplit(int min,int max,int count){
		int increases = (max-min) / count;
		increases = subinteger(increases,2);
		Integer[] array = new Integer[count+1];
		for(int i=0;i<count+1;i++){
			if(i==count){
				array[i] = max;
			}
			else{
				array[i] = min+(i*increases);
			}
		}
		return array;
	}
	
	/**
	 * int取整（用于“平均拆分数组”调用）
	 * 如：
	 * num＝1234567
	 * count＝2
	 * return 1200000
	 * @param num
	 * @param count 截取位数
	 * @return
	 */
	private static Integer subinteger(Integer num,int count){
		String numStr = num.toString();
		StringBuffer newNumStr = new StringBuffer(numStr.substring(0,count));
		for(int i=0;i<numStr.length()-count;i++){
			newNumStr.append("0");
		}
		return Integer.parseInt(newNumStr.toString());
	}
	
	/**
	 * List<String>转成List<Integer>
	 * @param List<String>
	 * @return
	 */
	public static List<Integer> StringToIntegerLst(List<String> inList){
        List<Integer> iList =new ArrayList<Integer>(inList.size());
        try{   
           for(String i:inList){
             iList.add(Integer.parseInt(i));   
           }   
          }catch(Exception  e){}
        return iList;
    }
	
	/**
	 * 字符串截取，截取部分以“...”代替
	 * @param str
	 * @param length
	 * @return
	 */
	public static String subString(String str,int length){
		if(StringUtils.isBlank(str)){
			return "";
		}
		if(length<=0 || str.length()<=length){
			return str;
		}
		return str.substring(0,length)+"...";
	}
}
