package cn.com.open.openpaas.userservice.web.api.user;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.userservice.app.web.WebUtils;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;


/**
 * 测试用户登录接口(通过用户名-密码)
 * 
 */
@Controller
@RequestMapping("/user/")
public class UserCenterTestLoginController {
	 @Autowired
	 private UserserviceDev userserviceDev;
	 @Value("#{properties['detect.password.time']}")
	  private String detect;
		
	/**
	 * 用户登录接口(通过用户名-密码)
	 * 
	 * @return Json
	 */
	@RequestMapping("/test/userCenterPassword")
	public void userCenterPasswordtest(HttpServletRequest request,
			HttpServletResponse response) {
		WebUtils.writeJson(response, userserviceDev.detect_password_time+":"+detect);
		return;
	}
}