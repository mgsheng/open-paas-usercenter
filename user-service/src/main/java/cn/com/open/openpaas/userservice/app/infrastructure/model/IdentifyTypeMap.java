package cn.com.open.openpaas.userservice.app.infrastructure.model;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.com.open.openpaas.userservice.app.user.model.User;



/**
 * 证件类型
 * @author dongminghao
 *
 */
public class IdentifyTypeMap {
	
	private static Map<Integer,String> map;
	
	/**
	 * 根据IdentifyType查询Name
	 * @param status
	 * @return
	 */
	public static String getName(int value){
		return getMap().get(value);
	}
	
	/**
	 * 获取整个IdentifyType的Map
	 * @return
	 */
	public static Map<Integer,String> getMap(){
		if(map==null){
			map = new LinkedHashMap<Integer, String>();
			map.put(User.IDENTIFYTYPE_ID, "身份证");
			map.put(User.IDENTIFYTYPE_MILITARYID, "军官证");
			map.put(User.IDENTIFYTYPE_HKID, "港澳台证");
			map.put(User.IDENTIFYTYPE_PASSPORT, "护照");
			map.put(User.IDENTIFYTYPE_OTHER, "其他");
		}
		return map;
	}
	
}
