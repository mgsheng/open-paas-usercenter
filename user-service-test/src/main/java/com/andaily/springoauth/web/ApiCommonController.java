package com.andaily.springoauth.web;

import static com.andaily.springoauth.web.WebUtils.writeJson;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ApiCommonController {
	   @Value("#{properties['SSO_DOMAIN_LIST']}")
	    private String SSO_DOMAIN_LIST;
	/**
	 * 获取服务器状态
	 * @param request
	 * @param response
	 */
    @RequestMapping(value = "common/status")
    public void status(HttpServletRequest request, HttpServletResponse response) {
      	response.setHeader("P3P","CP=\"NON DSP COR CURa ADMa DEVa TAIa PSAa PSDa IVAa IVDa CONa HISa TELa OTPa OUR UNRa IND UNI COM NAV INT DEM CNT PRE LOC\"");
        String user= request.getParameter("singleSignUser");
	     if(user!=null&&user.length()>0)
	     {
	        Cookie _USER=new Cookie("singleSignUser",user);
  	     _USER.setPath("/");
  	     _USER.setDomain(SSO_DOMAIN_LIST);
  	    _USER.setMaxAge(-1);
  	     response.addCookie(_USER);
  	     }
//        Map<String,Object> map = new LinkedHashMap<String, Object>();
//        //运行状态
//        map.put("running", true);
//        //系统状态
//    	try {
//    		MonitorInfo monitorInfo = MonitorTools.getMonitorInfo();
//			map.put("cpuratio", monitorInfo.getCpuRatio());
//			map.put("freememory", monitorInfo.getFreePhysicalMemorySize());
//			map.put("totalmemory", monitorInfo.getTotalMemorySize());
//			map.put("freespace", monitorInfo.getUsableSpace());
//			map.put("totalspace", monitorInfo.getTotalSpace());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//        writeJson(response, JSONObject.fromObject(map));
    }

}