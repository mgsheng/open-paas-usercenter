package cn.com.open.usercenter.test;

import cn.com.open.openpaas.userservice.app.tools.AESUtil;
import cn.com.open.openpaas.userservice.app.tools.DateTools;
import cn.com.open.openpaas.userservice.app.tools.StringTool;
import com.alibaba.fastjson.JSONObject;
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
public class RedisTest {

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
    public void redisTest() {
        try {
            String redisKey = Common.CLIENT_ID + "userService" + "junitTestSonarKey";

            String timestamp = DateTools.getSolrDate(new Date());
            String signatureNonce= StringTool.getRandom(100,1);
            String accessToken = Common.getAccessToken(mockMvc);
            String clientId = Common.CLIENT_ID;
            String signature = Common.getSignature(accessToken, timestamp, signatureNonce);
            MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/redis/saveRedis")
                    .param("timestamp", timestamp)
                    .param("signatureNonce", signatureNonce)
                    .param("access_token", accessToken)
                    .param("client_id", clientId)
                    .param("signature", signature)
                    .param("redis_key", redisKey)
                    .param("service_name", "userService")
                    .param("redis_value", "呵呵哒")).andReturn();
            String json = result.getResponse().getContentAsString();
            System.out.println(json);

            MvcResult result1 = mockMvc.perform(
                MockMvcRequestBuilders.post("/redis/getRedis")
                    .param("timestamp", timestamp)
                    .param("signatureNonce", signatureNonce)
                    .param("access_token", accessToken)
                    .param("client_id", clientId)
                    .param("signature", signature)
                    .param("service_name", "userService")
                    .param("redis_key", redisKey)).andReturn();
            System.out.println(result1.getResponse().getContentAsString());

            MvcResult result2 = mockMvc.perform(
                MockMvcRequestBuilders.post("/redis/delRedis")
                    .param("timestamp", timestamp)
                    .param("signatureNonce", signatureNonce)
                    .param("access_token", accessToken)
                    .param("client_id", clientId)
                    .param("service_name", "userService")
                    .param("signature", signature)
                    .param("redis_key", redisKey)).andReturn();
            System.out.println(result2.getResponse().getContentAsString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
