package cn.com.open.openpaas.userservice.app.filter;


import java.io 

.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import cn.com.open.openpaas.userservice.app.common.model.ExceptionEnum;
import cn.com.open.openpaas.userservice.app.common.model.Result;
import cn.com.open.openpaas.userservice.app.log.UserLog;
import cn.com.open.openpaas.userservice.app.tools.DateTools;
import cn.com.open.openpaas.userservice.app.tools.HttpTools;

/**
 * 日志服务
 * @author admin
 *
 */
public class LogFilter implements Filter {

    private FilterConfig filterConfig;
    private String user_base_request_url;
    private String user_api_log_onOff;
    private String kong_log_url;

    public void setKong_log_url(String kong_log_url) {
		this.kong_log_url = kong_log_url;
	}

	public String getUser_base_request_url() {
		return user_base_request_url;
	}

	public void setUser_base_request_url(String user_base_request_url) {
		this.user_base_request_url = user_base_request_url;
	}

	public String getUser_api_log_onOff() {
		return user_api_log_onOff;
	}

	public void setUser_api_log_onOff(String user_api_log_onOff) {
		this.user_api_log_onOff = user_api_log_onOff;
	}


	@Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

    	
    	if ("on".equals(user_api_log_onOff)) {
		String[] notCheckUrl = null;
		if (filterConfig.getInitParameter("notFilter") != null) {
			notCheckUrl = filterConfig.getInitParameter("notFilter").split(",");
		}
		// 不过滤的uri
		///filterChain.doFilter(request, response);
		// 请求的uri
		String uri = ((HttpServletRequest) request).getRequestURI();
		// 是否过滤
		boolean doFilter = true;
		if (uri.equals(this.filterConfig.getInitParameter("serverUri"))) {
			doFilter = false;
		} else {
			for (String s : notCheckUrl) {
				if (uri.indexOf(s) != -1) {
					// 如果uri中包含不过滤的uri，则不进行过滤
					doFilter = false;
					break;
				}
			}
		}
		if (doFilter) {
			//如果是添加日志的接口则添加日志

            if (response.getCharacterEncoding() == null) {
                response.setCharacterEncoding("UTF-8");
            }
            long startTime = System.currentTimeMillis(); //请求开始时间
            UserLog userLog=getUserLog(request);
            System.out.println(userLog.getGrantType());
            HttpServletResponseCopier responseCopier = new HttpServletResponseCopier((HttpServletResponse) response);

            filterChain.doFilter(request, responseCopier);
            responseCopier.flushBuffer();
            byte[] copy = responseCopier.getCopy();
            long endTime = System.currentTimeMillis(); //请求结束时间
          
            if (copy.length > 0) { //出现异常则 copy数组长度为0
                String result = new String(copy, response.getCharacterEncoding());
                JSONObject jsonObject = JSONObject.parseObject(result);
                userLog.setStatus(jsonObject.getString("status"));
                userLog.setErrorCode(jsonObject.getString("errorCode"));
                userLog.setErrorMsg(jsonObject.getString("errorMsg"));
                userLog.setExecTime(String.valueOf(endTime - startTime));
            } else { //处理出现异常
            	userLog.setStatus(String.valueOf(Result.ERROR));
            	userLog.setErrorCode(ExceptionEnum.SysException.getCode());
            	userLog.setErrorMsg(ExceptionEnum.SysException.getMessage());
            }
            Map <String,String>logMap=new HashMap<String,String>();
			logMap.put("tag", "usercenter");
			logMap.put("logData", JSONObject.toJSONString(userLog));
			HttpTools.URLPost(kong_log_url, logMap,"UTF-8");
		} else {
			filterChain.doFilter(request, response);
		}
		}else{
			filterChain.doFilter(request, response);
		}
	
    }

    @Override
    public void destroy() {

    }
    /**
     * 获取用户日志
     * @return
     */
    public UserLog getUserLog(ServletRequest request){
    	UserLog userLog=new UserLog();
    	userLog.setAccessToken(request.getParameter("access_token")==null?"":request.getParameter("access_token"));
    	userLog.setClientId(request.getParameter("client_id")==null?"":request.getParameter("client_id"));
    	userLog.setGrantType(request.getParameter("grant_type")==null?"":request.getParameter("grant_type"));
    	userLog.setClientSecret(request.getParameter("client_secert")==null?"":request.getParameter("client_secert"));
    	userLog.setScope(request.getParameter("scope")==null?"":request.getParameter("scope"));
    	userLog.setAccount(request.getParameter("account")==null?"":request.getParameter("account"));
    	userLog.setAccountType(request.getParameter("account_type")==null?"":request.getParameter("account_type"));
    	userLog.setAppId(request.getParameter("app_id")==null?"":request.getParameter("app_id"));
    	userLog.setCardNo(request.getParameter("card_no")==null?"":request.getParameter("card_no"));
    	userLog.setCode(request.getParameter("code")==null?"":request.getParameter("code"));
    	userLog.setCreateTime(DateTools.dateToString(new Date(), DateTools.FORMAT_ONE));
    	userLog.setDesAddress(request.getParameter("desAddress")==null?"":request.getParameter("desAddress"));
    	userLog.setDesAppId(request.getParameter("desApp_id")==null?"":request.getParameter("desApp_id"));
    	userLog.setEmail(request.getParameter("email")==null?"":request.getParameter("email"));
    	userLog.setGuid(request.getParameter("guid")==null?"":request.getParameter("guid"));
    	userLog.setIp(request.getParameter("ip")==null?"":request.getParameter("ip"));
    	userLog.setIsValidate(request.getParameter("isValidate")==null?"":request.getParameter("isValidate "));
    	userLog.setJsessionId(request.getParameter("jsessionId")==null?"":request.getParameter("jsessionId"));
    	userLog.setName(((HttpServletRequest) request).getRequestURI().replaceFirst(user_base_request_url, ""));
    	userLog.setNewPwd(request.getParameter("new_pwd")==null?"":request.getParameter("new_pwd"));
    	userLog.setOldPwd(request.getParameter("old_pwd")==null?"":request.getParameter("old_pwd"));
    	userLog.setPassword(request.getParameter("password")==null?"":request.getParameter("password"));
    	userLog.setPhone(request.getParameter("phone")==null?"":request.getParameter("phone"));
    	userLog.setPwdType(request.getParameter("pwdtype")==null?"":request.getParameter("pwdtype"));
    	userLog.setRedisKey(request.getParameter("redis_key")==null?"":request.getParameter("redis_key"));
    	userLog.setRedisValue(request.getParameter("redis_value")==null?"":request.getParameter("redis_value"));
    	userLog.setRefreshToken(request.getParameter("refresh_token")==null?"":request.getParameter("refresh_token"));
    	userLog.setSecret(request.getParameter("secert")==null?"":request.getParameter("secert"));
    	userLog.setSignature(request.getParameter("signature")==null?"":request.getParameter("signature"));
    	userLog.setSignatureNonce(request.getParameter("signatureNonce")==null?"":request.getParameter("signatureNonce"));
    	userLog.setSourceId(request.getParameter("source_id")==null?"":request.getParameter("source_id"));
    	userLog.setTimestamp(request.getParameter("timestamp")==null?"":request.getParameter("timestamp"));
    	userLog.setType(request.getParameter("type")==null?"":request.getParameter("type"));
    	userLog.setUsername(request.getParameter("username")==null?"":request.getParameter("username"));
    	userLog.setWhetherBind(request.getParameter("whetherBind")==null?"":request.getParameter("whetherBind"));
    	return userLog;
    	
    }

  
}
