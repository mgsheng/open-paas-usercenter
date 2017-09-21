package cn.com.open.usercenter.test;

import cn.com.open.openpaas.userservice.app.tools.DES;
import cn.com.open.openpaas.userservice.app.tools.DateTools;
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
import java.util.Date;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/spring/appCtx-disconf.xml",
        "classpath*:/spring/context.xml", "classpath*:/spring/job.xml",
        "classpath*:/spring/security.xml", "classpath*:/spring/transaction.xml" })
public class VerifyNoTokenTest {


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
    public void verifyNoTokenAutoLoginTest() {
        try {
            String clientId = Common.CLIENT_ID;
            MvcResult result = mockMvc.perform(
                    MockMvcRequestBuilders.post("/user/noToken/autoLogin")
                            .param("client_id", clientId)
                            .param("secret", DES.encrypt(Common.GUID + "#" + DateTools.dateToString(new Date(),"yyyyMMddHHmmss") + "#" + Common.CLIENT_ID + "#123456", Common.CLIENT_SECRET))
            ).andReturn();
            String str = result.getResponse().getContentAsString();
            JSONObject jsonObject = JSONObject.parseObject(str);
            String status = jsonObject.getString("status");
            Assert.assertEquals("1", status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
