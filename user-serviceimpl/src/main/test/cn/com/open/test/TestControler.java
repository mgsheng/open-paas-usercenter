package cn.com.open.test;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import cn.com.open.openpaas.userservice.web.api.user.UserCenterLoginController;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/spring/appCtx-disconf.xml",
		"classpath*:/spring/context.xml", "classpath*:/spring/job.xml",
		"classpath*:/spring/security.xml", "classpath*:/spring/transaction.xml" })
public class TestControler {
//	public static class MockSecurityContext implements SecurityContext {
//
//		private static final long serialVersionUID = -1386535243513362694L;
//
//		private Authentication authentication;
//
//		public MockSecurityContext(Authentication authentication) {
//			this.authentication = authentication;
//		}
//
//		@Override
//		public Authentication getAuthentication() {
//			return this.authentication;
//		}
//
//		@Override
//		public void setAuthentication(Authentication authentication) {
//			this.authentication = authentication;
//		}
//	}

	// 模拟request,response
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	@Autowired
	private Filter springSecurityFilterChain;

	// 注入loginController
	@Autowired
	private UserCenterLoginController loginController;
	@Autowired
	private WebApplicationContext webApplicationContext;
	// 执行测试方法之前初始化模拟request,response
	private MockMvc mockMvc;

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
	 * @Title：testLogin
	 * @Description: 测试用户登录
	 */
	@Test
	public void testLogin() {
		try {
		/*	UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(
					"credentials", "credentials-secret");
			request.setParameter("userName", "admin");
			request.setParameter("password", "2");
			loginController.userCenterPassword(request, response);
			System.out.println(response.getContentLength());

			MockHttpSession session = new MockHttpSession();
			session.setAttribute(
					HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
					new MockSecurityContext(principal));*/

			MvcResult result = mockMvc.perform(
					MockMvcRequestBuilders.get("/oauth/token")
							.param("client_id", "credentials-client")
							.param("client_secret", "credentials-secret")
							.param("grant_type", "client_credentials")
							.param("scope", "write")).andReturn();
			System.out.println(result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
