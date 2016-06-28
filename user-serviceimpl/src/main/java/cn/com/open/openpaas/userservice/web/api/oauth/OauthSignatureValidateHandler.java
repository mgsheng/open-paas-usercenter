package cn.com.open.openpaas.userservice.web.api.oauth;


import javax.servlet.http.HttpServletRequest;

import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.tools.HMacSha1;


public class OauthSignatureValidateHandler {
	
	final static String  SEPARATOR = "&";
	/**
	 * Signature验证算法
	 * 格式：client_id&access_token&timestamp&signatureNonce
	 * 加密方法:HmacSHA1
	 * @param request
	 * @param app
	 * @return
	 */
	  public static boolean validateSignature(HttpServletRequest request,App app){
		    String clientId=request.getParameter("client_id");
		    String accessToken=request.getParameter("access_token");
		    String signature=request.getParameter("signature");
		    String timestamp=request.getParameter("timestamp");
		    String signatureNonce=request.getParameter("signatureNonce");
		 	 StringBuilder encryptText = new StringBuilder();
		 	encryptText.append(clientId);
			encryptText.append(SEPARATOR);
		 	encryptText.append(accessToken);
		 	encryptText.append(SEPARATOR);
		 	encryptText.append(timestamp);
		 	encryptText.append(SEPARATOR);
		 	encryptText.append(signatureNonce);
		 	try {
			String result=	HMacSha1.HmacSHA1Encrypt(encryptText.toString(), app.getAppsecret());
			if(signature.equals(result))
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return false;
		  
	    }  
}
