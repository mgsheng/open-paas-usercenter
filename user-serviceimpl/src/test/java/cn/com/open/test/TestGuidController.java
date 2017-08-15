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

import cn.com.open.openpaas.userservice.app.tools.AESUtil;
import cn.com.open.openpaas.userservice.app.tools.DateTools;
import cn.com.open.openpaas.userservice.app.tools.HMacSha1;
import cn.com.open.openpaas.userservice.app.tools.PropertiesTool;
import cn.com.open.openpaas.userservice.app.tools.StringTool;
import cn.com.open.openpaas.userservice.web.api.user.ModiPwdController;
import cn.com.open.openpaas.userservice.web.api.user.UserCenterGuidController;
import cn.com.open.openpaas.userservice.web.api.user.UserCenterRegController;
import cn.com.open.openpaas.userservice.web.api.user.VerifyController;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/spring/appCtx-disconf.xml",
		"classpath*:/spring/context.xml", "classpath*:/spring/job.xml",
		"classpath*:/spring/security.xml", "classpath*:/spring/transaction.xml" })
public class TestGuidController {

	// 模拟request,response
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	@Autowired
	private Filter springSecurityFilterChain;

	// 注入guidController
	@Autowired
	private UserCenterGuidController guidController;
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
	 * @Title：testGuid
	 * @Description: 测试用户账号验证
	 */
	@Test
	public void testGuid() {
		try {		
			String key=PropertiesTool.getAppPropertieByKey("credentials-client");
	   	    String signature="";
	   	    String timestamp="";
	   	    String signatureNonce="";
		    if(key!=null){
	      	    timestamp=DateTools.getSolrDate(new Date());
			 	StringBuilder encryptText = new StringBuilder();
			 	signatureNonce=StringTool.getRandom(100,1);
			 	encryptText.append("credentials-client");
				encryptText.append(SEPARATOR);
			 	encryptText.append("57571cc1-e71f-48a8-b98d-21f073a28481");
			 	encryptText.append(SEPARATOR);
			 	encryptText.append(timestamp);
			 	encryptText.append(SEPARATOR);
			 	encryptText.append(signatureNonce);
			 	signature=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
			 	signature=HMacSha1.getNewResult(signature);
		    }
      		
			MvcResult result = mockMvc.perform(
					MockMvcRequestBuilders.get("/user/userGuidInfo")
							.param("client_id", "credentials-client")
							.param("access_token", "57571cc1-e71f-48a8-b98d-21f073a28481")
							.param("account", "xiaoli123")
					    	.param("accountType", "1")/*登录类型，1-用户名格式  2-手机号格式  3-邮箱格式  4-奥鹏卡号*/
							.param("signature", signature)
							.param("timestamp", timestamp)
							.param("signatureNonce", signatureNonce)).andReturn();
			System.out.println(result.getResponse().getContentAsString());	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
