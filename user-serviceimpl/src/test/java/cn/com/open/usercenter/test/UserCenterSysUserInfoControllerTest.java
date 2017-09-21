package cn.com.open.usercenter.test;

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
import org.testng.Assert;

import javax.servlet.Filter;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/spring/appCtx-disconf.xml",
        "classpath*:/spring/context.xml", "classpath*:/spring/job.xml",
        "classpath*:/spring/security.xml", "classpath*:/spring/transaction.xml" })
public class UserCenterSysUserInfoControllerTest {

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
    public void userCenterSysUserInfoTest() {
        try {
            String accessToken = Common.getAccessToken(mockMvc);
            MvcResult result = mockMvc.perform(
                    MockMvcRequestBuilders.post("/user/info/synUserInfo")
                            .param("grant_type", Common.GRANT_TYPE)
                            .param("client_id", Common.CLIENT_ID)
                            .param("access_token", accessToken)
                            .param("scope", Common.SCOPE)
                            .param("source_id", Common.SOURCE_ID)
                            .param("nickname", "extrigger")
                            .param("read_name", "谷旭阳")
                            .param("phone", Common.PHONE)
                            .param("email", Common.EMAIL)).andReturn();
            String str = result.getResponse().getContentAsString();
            JSONObject jsonObject = JSONObject.parseObject(str);
            String status = jsonObject.getString("status");
            Assert.assertEquals("1", status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
