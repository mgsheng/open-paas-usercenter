package cn.com.open.openpaas.userservice.web.api.user;

import cn.com.open.openpaas.userservice.app.tools.MonitorInfo;
import cn.com.open.openpaas.userservice.app.tools.MonitorTools;
import cn.com.open.openpaas.userservice.app.tools.NetworkUtil;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.app.web.WebUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;


@Controller
public class ApiCommonController   {
	
    @Autowired
    private UserService userService;
    
    /**
	 * 获取服务器状态
	 * @param request
	 * @param response
	 */
    @RequestMapping("common/status")
    public void status(HttpServletRequest request,HttpServletResponse response) {
    	String ip = NetworkUtil.getIpAddress(request);
    	System.out.println(ip);
    	Map<String,Object> map = new LinkedHashMap<String, Object>();
    	//运行状态
    	map.put("running", true);
    	//连接数据库状态
    	int val = 0;
    	try {
			val = userService.connectionTest();
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
    	if(val==1){
    		map.put("database", true);
    	}
    	else{
    		map.put("database", false);
    	}
    	//系统状态
    	try {
    		MonitorInfo monitorInfo = MonitorTools.getMonitorInfo();
			map.put("cpuratio", monitorInfo.getCpuRatio());
			map.put("freememory", monitorInfo.getFreePhysicalMemorySize());
			map.put("totalmemory", monitorInfo.getTotalMemorySize());
			map.put("freespace", monitorInfo.getUsableSpace());
			map.put("totalspace", monitorInfo.getTotalSpace());
		} catch (Exception e) {
		//	e.printStackTrace();
		}
    	WebUtils.writeJson(response, JSONObject.fromObject(map));
    }
    @RequestMapping("/dnotdelet/mom.html")
    public String home() {
        return "unity/mom";
    }
}