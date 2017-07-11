package cn.com.open.user.app.tools;

import javax.servlet.http.HttpServletResponse;

public class HtmlUtil {

	public static void writeJson(HttpServletResponse response, String jsonString) {
		try {
			response.setHeader("Content-type", "application/json;charset=UTF-8");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonString);
		} catch (Exception e){
			e.printStackTrace();
		}

	}

}
