package cn.com.open.usercenter.test;

import cn.com.open.openpaas.userservice.app.tools.AESUtil;
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
public class ChangeStatusTest {

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
    public void changeStatusTest() {
        try {
            String timestamp = DateTools.getSolrDate(new Date());
            String signatureNonce= StringTool.getRandom(100,1);
            String accessToken = Common.getAccessToken(mockMvc);
            String clientId = Common.CLIENT_ID;
            String signature = Common.getSignature(accessToken, timestamp, signatureNonce);

            MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/user/change/status")
                    .param("timestamp", timestamp)
                    .param("signatureNonce", signatureNonce)
                    .param("access_token", accessToken)
                    .param("client_id", clientId)
                    .param("signature", signature)
                    .param("status", "2")
                    .param("guid", Common.GUID)).andReturn();
            System.out.println(result.getResponse().getContentAsString());

            MvcResult result1 = mockMvc.perform(
                    MockMvcRequestBuilders.post("/user/change/status")
                            .param("timestamp", timestamp)
                            .param("signatureNonce", signatureNonce)
                            .param("access_token", accessToken)
                            .param("client_id", clientId)
                            .param("signature", signature)
                            .param("status", "1")
                            .param("guid", Common.GUID)).andReturn();
            System.out.println(result1.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
