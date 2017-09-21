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

public class UserCenterPublicLoginControllerTest {

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
    public void userCenterPublicLogin() {
        try {

            JSONObject json = new JSONObject();
            json.put("sourceId", Common.SOURCE_ID);
            json.put("time", DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
            json.put("appkey", "4194b8dbd6624131bfccbdd6f7140776");
            json.put("appId", "9999");
            json.put("platform", "1");

            String clientId = Common.CLIENT_ID;
            MvcResult result = mockMvc.perform(
                    MockMvcRequestBuilders.post("/user/userCenterPublicLogin")
                            .param("secret", DES.encrypt(json.toString(), "1d4d8c77108a4fd2a3c23feba0cfdccc".substring(0,8)))
            ).andReturn();
            String str = result.getResponse().getRedirectedUrl();
            Assert.assertNotNull(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
