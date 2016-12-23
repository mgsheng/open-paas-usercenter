package cn.com.open.openpaas.userservice.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

/**
 * 
 */
public abstract class WebUtils {


    //private
    private WebUtils() {
    }


    public static void writeJson(HttpServletResponse response, JSON json) {
        response.setContentType("application/json;charset=UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            json.write(writer);
            writer.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Write json to response error", e);
        }

    }
    
    public static void writeJson(HttpServletResponse response,String str) {
    	try {
			response.getWriter().write(str);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static void writeJsonToMap(HttpServletResponse response,Map<String,Object> map) {
    	try {
			JsonConfig jsonConfig = new JsonConfig();
			// 排除,避免循环引用 There is a cycle in the hierarchy!
			jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
			jsonConfig.setIgnoreDefaultExcludes(true);
			jsonConfig.setAllowNonStringKeys(true);
			writeJson(response, JSONObject.fromObject(map,jsonConfig));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}