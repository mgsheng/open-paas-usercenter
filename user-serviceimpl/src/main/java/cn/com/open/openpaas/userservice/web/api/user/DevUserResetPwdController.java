package cn.com.open.openpaas.userservice.web.api.user;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.userservice.app.tools.DateTools;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.model.UserCache;
import cn.com.open.openpaas.userservice.app.user.model.UserProblem;
import cn.com.open.openpaas.userservice.app.user.service.UserActivatedService;
import cn.com.open.openpaas.userservice.app.user.service.UserCacheService;
import cn.com.open.openpaas.userservice.app.user.service.UserProblemService;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.app.useractivated.model.UserActivated;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;

/**
 * 
 */
@Controller
public class DevUserResetPwdController extends BaseDevUserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserProblemService userProblemService;
    @Autowired
    private UserActivatedService userActivatedService;
    
    @Autowired
	 private UserserviceDev userserviceDev;
	@Autowired
	private UserCacheService userCacheService;
    
    @RequestMapping("/findpwd")
    public String findpwd(HttpServletRequest request,HttpServletResponse response,Map<String,Object> model) {
    	String mobileNo=request.getParameter("mobileNo");
    	//String mobileNo="15727398579";
    	if(mobileNo!=null&&!"".equals(mobileNo)){
    		String showMobileNo=mobileNo.substring(0,3)+"****"+mobileNo.substring(7, mobileNo.length());
    		model.put("mobileNo",mobileNo);
    		model.put("showMobileNo",showMobileNo);
    		return "../../findpwdphone";
    	}
        return "../../findpwd";
    }
    @RequestMapping("/findpwdemail")
    public String findpwdemail(HttpServletRequest request,HttpServletResponse response,Map<String,Object> model,String guid) {
    	model.put("guid", guid);
        return "../../findpwdemail";
    }
    
    @RequestMapping("/activated")
    public String activated(HttpServletRequest request,HttpServletResponse response,Map<String,Object> model) {
        return "../../activated";
    }
    @RequestMapping("/findpwdphone")
    public String findpwdphone(HttpServletRequest request,HttpServletResponse response,Map<String,Object> model,String guid) {
    	model.put("guid", guid);
        return "../../findpwdphone";
    }
    @RequestMapping("/findpwdproblem")
    public String findpwdproblem(HttpServletRequest request,HttpServletResponse response,Map<String,Object> model) {
        return "../../findpwdproblem";
    }
    
    /**
     * 验证用户找回密码邮件有效性，验证通过则进入重置密码页面
     * @param request
     * @param model
     * @param bool
     * @return
     */
    @RequestMapping(value = "/dev/user/activated_reset_password_email.html")
	public String activated_reset_password_email(HttpServletRequest request,HttpServletResponse response,Map<String,Object> model,String code) {
    	//激活有效数据
    	int validTime = Integer.valueOf(userserviceDev.getEmail_verify_valid());
    	boolean flag = false;
    	String errorCode = "";
    	UserActivated userActivated = userActivatedService.findByCode(code);
    	if(null != userActivated){
    		if(DateTools.timeDiffCurr(userActivated.getCreateTime()) > validTime*60*1000){
    			errorCode = "time_out";
    		}else{
	    		User user = userService.findUserById(userActivated.getUserId());
	    		if(null != user && user.getEmail().equals(userActivated.getEmail())){
	        		userActivatedService.moveUserActivated(userActivated);
	        		model.put("code", userActivated.getCode());
	        		model.put("email", userActivated.getEmail());
	        		model.put("type", "1");
	        		return "../../findpwdreset";
	    		}else{
	    			errorCode = "invalid_email";
	    		}
    		}
    	}else{
    		errorCode = "invalid_code";
    	}
    	//跳转到发送成功后的页面，提示：当你收到邮件信息，请按照提示 进行密码重置
    	model.put("flag",flag);
    	model.put("errorCode",errorCode);
    	return "../../findpwd";
    }
    
    @RequestMapping("/activated.html")
    public String activated1(HttpServletRequest request,HttpServletResponse response,Map<String,Object> model,String guid) {
    	model.put("guid", guid);
        return "../../activated";
    } 
    /**
     * 验证用户找回密码 手机验证码有效性，验证通过则进入重置密码页面
     * @param request
     * @param model
     * @param bool
     * @return
     */
    @RequestMapping(value = "/dev/user/activated_reset_password_phone.html")
	public String activated_reset_password_phone(HttpServletRequest request,HttpServletResponse response,Map<String,Object> model,
			String code,
			String phone) {
    	boolean flag = false;
    	String errorCode = "";
    	if(StringUtils.isBlank(code) || StringUtils.isBlank(phone)){
    		errorCode = "invalid_data";
    	}
    	else{
    		//激活有效数据
        	int validTime = Integer.valueOf(userserviceDev.getEmail_verify_valid());
        	UserActivated userActivated = new UserActivated();
        	userActivated.setCode(code);
        	userActivated.setPhone(phone);
        	List<UserActivated> userActivatedList = userActivatedService.findByUserActivated(userActivated);
        	if(userActivatedList != null && userActivatedList.size()>0){
        		userActivated = userActivatedList.get(0);
    	    	if(null != userActivated){
    	    		if(DateTools.timeDiffCurr(userActivated.getCreateTime()) >(validTime*60*1000)){
    	    			errorCode = "time_out";
    	    		}else{
    		    		User user = userService.findUserById(userActivated.getUserId());
    		    		if(null != user && phone.equals(userActivated.getPhone())){
    		        		userActivatedService.moveUserActivated(userActivated);
    		        		model.put("code", userActivated.getCode());
    		        		model.put("phone", userActivated.getPhone());
    		        		model.put("type","2");
    		        		return "../../findpwdreset";
    		    		}else{
							UserCache usercache = userCacheService.findUserById(userActivated.getUserId());
							if(usercache != null && phone.equals(userActivated.getPhone())){
								userActivatedService.moveUserActivated(userActivated);
	    		        		model.put("code", userActivated.getCode());
	    		        		model.put("phone", userActivated.getPhone());
	    		        		model.put("type","2");
	    		        		return "../../findpwdreset";
						  }else{
							  errorCode = "invalid_phone";
						  }
    		    		}
    	    		}
    	    	}else{
    	    		errorCode = "invalid_data";
    	    	}
        	}else{
        		errorCode = "invalid_data";
        	}
    	}
    	model.put("flag",flag);
    	model.put("errorCode",errorCode);
    	return "../../findpwd";
    }
    
    /**
     * 验证用户找回密码 手机验证码有效性，验证通过则进入重置密码页面
     * @param request
     * @param model
     * @param bool
     * @return
     */
    @RequestMapping(value = "/dev/user/activated_reset_password_problem.html")
	public String activated_reset_password_problem(HttpServletRequest request,HttpServletResponse response,Map<String,Object> model,String userName,String problemId,String answer) {
    	boolean flag = false;
    	String errorCode = "";
    	if(StringUtils.isNotBlank(userName)){
	    	User user = userService.findByUsername(userName);
	    	if(user != null){
	    		UserProblem userProblem = userProblemService.getByUserIdAndProblemId(user.getId(),Integer.valueOf(problemId));
	        	if(null != userProblem){
	        		if(userProblem.getAnswer().equals(answer)){
	        			model.put("code", userName);
		        		model.put("type","3");
		        		return "../../findpwdreset";
	        		}else{
	        			errorCode = "answer_error";
	        		}
	        	}else{
	        		errorCode = "invalid_problem";
	        	}
	    	}else{
	    		errorCode = "invalid_username";
	    	}
    	}else{
    		errorCode = "invalid_username";
    	}
    	model.put("flag",flag);
    	model.put("errorCode",errorCode);
    	return "../../findpwd";
    }
}