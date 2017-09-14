package cn.com.open.usercenter.test;

import cn.com.open.openpaas.userservice.app.tools.DateTools;
import cn.com.open.openpaas.userservice.app.tools.StringTool;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.util.Date;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/spring/appCtx-disconf.xml",
        "classpath*:/spring/context.xml", "classpath*:/spring/job.xml",
        "classpath*:/spring/security.xml", "classpath*:/spring/transaction.xml" })
public class UserCenterVerifyTest {

    private MockHttpServletRequest request;
    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void userCenterVerifyTest() {
        try {
            String timestamp = DateTools.getSolrDate(new Date());
            String signatureNonce= StringTool.getRandom(100,1);
            String accessToken = Common.getAccessToken(mockMvc);
            String clientId = Common.CLIENT_ID;
            String signature = Common.getSignature(accessToken, timestamp, signatureNonce);

            MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/user/userCenterVerify")
                    .param("timestamp", timestamp)
                    .param("signatureNonce", signatureNonce)
                    .param("access_token", accessToken)
                    .param("client_id", clientId)
                    .param("signature", signature)
                    .param("accountType", "1") //登录类型，1-用户名格式  2-手机号格式  3-邮箱格式  4-奥鹏卡号
                    .param("account", Common.USERNAME)).andReturn();
            System.out.println(result.getResponse().getContentAsString());

            MvcResult result1 = mockMvc.perform(
                MockMvcRequestBuilders.post("/user/userCenterVerify")
                    .param("timestamp", timestamp)
                    .param("signatureNonce", signatureNonce)
                    .param("access_token", accessToken)
                    .param("client_id", clientId)
                    .param("signature", signature)
                    .param("accountType", "2")
                    .param("account", Common.PHONE)).andReturn();
            System.out.println(result1.getResponse().getContentAsString());

            MvcResult result2 = mockMvc.perform(
                MockMvcRequestBuilders.post("/user/userCenterVerify")
                    .param("timestamp", timestamp)
                    .param("signatureNonce", signatureNonce)
                    .param("access_token", accessToken)
                    .param("client_id", clientId)
                    .param("signature", signature)
                    .param("accountType", "3")
                    .param("account", Common.EMAIL)).andReturn();
            System.out.println(result2.getResponse().getContentAsString());

            MvcResult result3 = mockMvc.perform(
                MockMvcRequestBuilders.post("/user/userCenterVerify")
                    .param("timestamp", timestamp)
                    .param("signatureNonce", signatureNonce)
                    .param("access_token", accessToken)
                    .param("client_id", clientId)
                    .param("signature", signature)
                    .param("accountType", "4")
                    .param("account", "201202145895")).andReturn();
            System.out.println(result3.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
