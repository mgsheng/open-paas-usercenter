package cn.com.open.test;

import java.util.Date;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import cn.com.open.openpaas.userservice.app.tools.DESUtil;
import cn.com.open.openpaas.userservice.app.tools.DateTools;
import cn.com.open.openpaas.userservice.app.tools.HMacSha1;
import cn.com.open.openpaas.userservice.app.tools.PropertiesTool;
import cn.com.open.openpaas.userservice.app.tools.StringTool;
import cn.com.open.openpaas.userservice.web.api.user.AutoLoginController;
import cn.com.open.openpaas.userservice.web.api.user.UserCenterBindInfoController;
import cn.com.open.openpaas.userservice.web.api.user.VerfiyAutoLoginController;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/spring/appCtx-disconf.xml",
		"classpath*:/spring/context.xml", "classpath*:/spring/job.xml",
		"classpath*:/spring/security.xml", "classpath*:/spring/transaction.xml" })
public class TestVerifyAutoLoginControler {

	// 模拟request,response
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	@Autowired
	private Filter springSecurityFilterChain;

	// 注入verfiyAutoLoginController
	@Autowired
	private VerfiyAutoLoginController verfiyAutoLoginController;
	@Autowired
	private WebApplicationContext webApplicationContext;
	// 执行测试方法之前初始化模拟request,response
	private MockMvc mockMvc;
    final static String  SEPARATOR = "&";

	@Before
	public void setUp() {
		request = new MockHttpServletRequest();
		request.setCharacterEncoding("UTF-8");
		response = new MockHttpServletResponse();
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.addFilter(springSecurityFilterChain).build();
	}

	/**
	 * 
	 * @Title：testVerifyAutoLogin
	 * @Description: 测试自动登录
	 */
	@Test
	public void testVerifyAutoLogin() {
		try {		
			String key=PropertiesTool.getAppPropertieByKey("credentials-client");
	   	    String signature="";
	   	    String timestamp="";
	   	    String signatureNonce="";
	   	    String secret=DESUtil.encrypt("123464643546464865#20170330135700#credentials-client#123456#http://www.baidu.com", "credenti");/*Des(（guid+“#”+time+“#”+appKey+“#”+salt+“#”+desAddress）, appSecret)*/
		    if(key!=null){
	      	    timestamp=DateTools.getSolrDate(new Date());
			 	StringBuilder encryptText = new StringBuilder();
			 	signatureNonce=StringTool.getRandom(100,1);
			 	encryptText.append("credentials-client");
				encryptText.append(SEPARATOR);
			 	encryptText.append("11c6cc8f-7d93-4c29-a161-7d95e2572365");
			 	encryptText.append(SEPARATOR);
			 	encryptText.append(timestamp);
			 	encryptText.append(SEPARATOR);
			 	encryptText.append(signatureNonce);
			 	signature=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
			 	signature=HMacSha1.getNewResult(signature);
		    }
      		
			MvcResult result = mockMvc.perform(
					MockMvcRequestBuilders.get("/user/autoLogin")
							.param("client_id", "credentials-client")
							.param("access_token", "11c6cc8f-7d93-4c29-a161-7d95e2572365")
							.param("secret", secret)							
							.param("signature", signature)
							.param("timestamp", timestamp)
							.param("signatureNonce", signatureNonce)).andReturn();
			System.out.println(result.getResponse().getContentAsString());				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
