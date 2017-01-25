package cn.com.open.openpaas.userservice.web.api.user;

import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * 基于cookie跨域的单点登录
 */
@Controller
@RequestMapping("/cross/")
public class CookieController   extends BaseController {
	@Autowired
	protected RedisClientTemplate redisClient;
    /**
     * cookie获取接口
     * @return Json
     */
    @RequestMapping(value = "getCookie",method = RequestMethod.POST)
    public void getCookie(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		String username=request.getParameter("username");
		if(!paraMandatoryCheck(Arrays.asList(username))){
			paraMandaChkAndReturn(5, response,"必传参数中有空值");
			return;
		}
		String[] cookieValues;
        if(redisClient.existKey(RedisConstant.USER_SERVICE_COOKIENAME)){
        	String cookieNewRedis = "";
        	String cookieValue = redisClient.getObject(RedisConstant.USER_SERVICE_COOKIENAME).toString();
        	 cookieValues = cookieValue.split(",");
        	for (String cookie : cookieValues){
        	 	/*判断是否过期*/
        	 	String initDate = cookie.split("_")[1];
				long second = diffDate(initDate,(new Date()).toString(),"second");
				/*cookie未过期，second>0， 0标示多久cookie过期，需要设置时间,过期数据将被删除*/
				if(second>0 && cookie.split("_")[0].equals(username)){
					Cookie redisCookie = new Cookie(cookie.split("_")[0],cookie.split("_")[1]);
					redisCookie.setPath("/");
					response.addCookie(redisCookie);
					cookieNewRedis += cookie.split("_")[0]+"_"+cookie.split("_")[1]+"_"+new Date()+",";
				}
				if(cookieNewRedis.length()>0){
					cookieNewRedis = cookieNewRedis.substring(0,cookieNewRedis.length()-1);
				}
				redisClient.setObject(RedisConstant.USER_SERVICE_COOKIENAME,cookieNewRedis);
			}
		}
    }

	/**
	 * 计算时间差
	 * @param startDate 截止日止
	 * @param endDate 开始日期
	 * @param dateType 返回类型 天：day 小时：hour 分钟：minute 秒：second
	 * @return
	 * @throws ParseException
	 */
    private long diffDate(String startDate,String endDate,String dateType) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		java.util.Date now = df.parse(endDate);

		java.util.Date date=df.parse(startDate);

		long l=now.getTime()-date.getTime();

		long day=l/(24*60*60*1000);

		long hour=(l/(60*60*1000)-day*24);

		long min=((l/(60*1000))-day*24*60-hour*60);

		long s=(l/1000-day*24*60*60-hour*60*60-min*60);

		if(dateType.equals("day")){
			return day;
		}else if(dateType.equals("hour")){
			return day*24+hour;
		}else if(dateType.equals("minute")){
			return day*24*60+min;
		}else if(dateType.equals("second")){
			return day*24*60*60+s;
		}else {
			return 0;
		}
	}
}
