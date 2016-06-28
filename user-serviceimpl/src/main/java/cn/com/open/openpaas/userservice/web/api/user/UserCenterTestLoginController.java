package cn.com.open.openpaas.userservice.web.api.user;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.userservice.app.web.WebUtils;


/**
 * 测试用户登录接口(通过用户名-密码)
 * 
 */
@Controller
@RequestMapping("/user/")
public class UserCenterTestLoginController {
	
	/**
	 * 用户登录接口(通过用户名-密码)
	 * 
	 * @return Json
	 */
	@RequestMapping("/test/userCenterPassword")
	public void userCenterPasswordtest(HttpServletRequest request,
			HttpServletResponse response) {
		WebUtils.writeJson(response, "OK");
		return;
	}
}