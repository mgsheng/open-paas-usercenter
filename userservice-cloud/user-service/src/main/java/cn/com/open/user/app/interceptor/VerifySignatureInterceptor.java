package cn.com.open.user.app.interceptor;

import com.alibaba.fastjson.JSONObject;

import cn.com.open.user.app.common.ExceptionEnum;
import cn.com.open.user.app.common.PlatformEnum;
import cn.com.open.user.app.common.Result;
import cn.com.open.user.app.service.AppService;
import cn.com.open.user.app.sign.MD5;
import cn.com.open.user.app.tools.HtmlUtil;
import cn.com.open.user.app.tools.ParamUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


/**
 * 验签拦截器
 *
 * Created by guxuyang on 05/07/2017.
 */
public class VerifySignatureInterceptor implements HandlerInterceptor {
	private static final Logger log = LoggerFactory.getLogger(VerifySignatureInterceptor.class);

    @Autowired
    private AppService appService;
    @Value("${app.verifySignature.onOff}")
    private String onOff;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	   response.addHeader("Access-Control-Allow-Origin", "*");
    	   response.addHeader("Access-Control-Allow-Headers"," Origin, X-Requested-With, Content-Type, Accept,appKey,timestamp,signatureNonce,platform,signature");
    	
    	   //OPTIONS 请求方式直接返回
           if(request.getMethod().equals(RequestMethod.OPTIONS.name())){
	    		return true;
	    	}
    	   
    	   if ("on".equals(onOff)) { //开关
            String appKey = request.getHeader("appKey");
            String timestamp = request.getHeader("timestamp");
            String signatureNonce = request.getHeader("signatureNonce");
            String platform = request.getHeader("platform");
            String signature = request.getHeader("signature");
            String appSecret = appService.findAppSecretByAppkey(appKey);
            
            log.info("VerifySignatureInterceptor preHandle appKey="+appKey+"&signature="+signature);
 	     	log.info("VerifySignatureInterceptor preHandle+++++++ timestamp="+timestamp+"&signatureNonce="+signatureNonce+"&platform="+platform+"&appSecret="+appSecret);

        //    HandlerMethod handlerMethod = (HandlerMethod) handler;  
        //    System.out.println(handlerMethod.getMethod().getName());  
            
            if (!ParamUtil.paramMandatoryCheck(Arrays.asList(appKey, timestamp, signature, signatureNonce, platform))) { //存在空参数
                Result result = new Result(Result.ERROR, ExceptionEnum.CommonParmeterError.getMessage(), ExceptionEnum.CommonParmeterError.getCode(), null);
                String resultJson = JSONObject.toJSONString(result);
                HtmlUtil.writeJson(response, resultJson);
                return false;
            }
            if (PlatformEnum.getByCode(platform) == null) {
                Result result = new Result(Result.ERROR, "平台参数错误", ExceptionEnum.CommonParmeterError.getCode(), null);
                String resultJson = JSONObject.toJSONString(result);
                HtmlUtil.writeJson(response, resultJson);
                return false;
            }

            if (appSecret == null) {
                Result result = new Result(Result.ERROR, ExceptionEnum.E1000001.getMessage(), ExceptionEnum.E1000001.getCode(), null);
                String resultJson = JSONObject.toJSONString(result);	
                HtmlUtil.writeJson(response, resultJson);
                return false;
            }
            if (verifySignature(timestamp, signatureNonce, platform, appSecret, signature)) {
                return true;
            } else {
                Result result = new Result(Result.ERROR, ExceptionEnum.E1000002.getMessage(), ExceptionEnum.E1000002.getCode(), null);
                String resultJson = JSONObject.toJSONString(result);
                HtmlUtil.writeJson(response, resultJson);
                return false;
            }
        }
        return true;
    }

    /**
     * 校验签名
     */
    private boolean verifySignature(String timestamp, String signatureNonce, String platform, String appSecret, String signature) {
        String str = String.format("timestamp=%s&signatureNonce=%s&platform=%s&appSecret=%s", timestamp, signatureNonce, platform, appSecret);
        signature=signature.toLowerCase();
        return signature.equals(MD5.Md5(str));
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

}

