package com.andaily.springoauth.web;

import static com.andaily.springoauth.web.WebUtils.writeJson;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.andaily.springoauth.tools.MonitorInfo;
import com.andaily.springoauth.tools.MonitorTools;

@Controller
public class ApiCommonController {

	/**
	 * 获取服务器状态
	 * @param request
	 * @param response
	 */
    @RequestMapping(value = "common/status")
    public void status(HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> map = new LinkedHashMap<String, Object>();
        //运行状态
        map.put("running", true);
        //系统状态
    	try {
    		MonitorInfo monitorInfo = MonitorTools.getMonitorInfo();
			map.put("cpuratio", monitorInfo.getCpuRatio());
			map.put("freememory", monitorInfo.getFreePhysicalMemorySize());
			map.put("totalmemory", monitorInfo.getTotalMemorySize());
			map.put("freespace", monitorInfo.getUsableSpace());
			map.put("totalspace", monitorInfo.getTotalSpace());
		} catch (Exception e) {
			e.printStackTrace();
		}
        writeJson(response, JSONObject.fromObject(map));
    }

}