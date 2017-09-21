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
public class JsonDevUserResetPwdControllerTest {

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
    public void devUserSendResetPasswordEmail() {
        try {
            MvcResult result = mockMvc.perform(
                    MockMvcRequestBuilders.post("/dev/user/send_reset_password_email")
                            .param("email", Common.EMAIL)).andReturn();
            String str = result.getResponse().getContentAsString();

            JSONObject jsonObject = JSONObject.parseObject(str);
            boolean flag = jsonObject.getBoolean("flag");
            Assert.assertEquals(true, flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void devUserSendResetPasswordPhone() {
        try {
            MvcResult result = mockMvc.perform(
                    MockMvcRequestBuilders.post("/dev/user/send_reset_password_phone")
                            .param("phone", Common.PHONE)).andReturn();
            String str = result.getResponse().getContentAsString();

            JSONObject jsonObject = JSONObject.parseObject(str);
            boolean flag = jsonObject.getBoolean("flag");
            Assert.assertEquals(true, flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void devUserActivatedResetPasswordPhone() {
        try {
            MvcResult result = mockMvc.perform(
                    MockMvcRequestBuilders.post("/dev/user/activated_reset_password_phone")
                            .param("phone", Common.PHONE)
                            .param("code", "9999")).andReturn();
            String str = result.getResponse().getContentAsString();

            JSONObject jsonObject = JSONObject.parseObject(str);
            boolean flag = jsonObject.getBoolean("flag");
            Assert.assertEquals(false, flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void devUserResetPassword1() {
        try {
            MvcResult result = mockMvc.perform(
                    MockMvcRequestBuilders.post("/dev/user/reset_password")
                            .param("phone", Common.PHONE)
                            .param("code", "9999")
                            .param("type", "2")
                            .param("password", Common.PASSWORD)).andReturn();
            String str = result.getResponse().getContentAsString();
            JSONObject jsonObject = JSONObject.parseObject(str);
            boolean flag = jsonObject.getBoolean("flag");
            Assert.assertEquals(false, flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void devUserResetPassword2() {
        try {
            MvcResult result = mockMvc.perform(
                    MockMvcRequestBuilders.post("/dev/user/reset_password")
                            .param("email", Common.EMAIL)
                            .param("type", "1")
                            .param("code", "9999")
                            .param("password", Common.PASSWORD)).andReturn();
            String str = result.getResponse().getContentAsString();
            JSONObject jsonObject = JSONObject.parseObject(str);
            boolean flag = jsonObject.getBoolean("flag");
            Assert.assertEquals(false, flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
