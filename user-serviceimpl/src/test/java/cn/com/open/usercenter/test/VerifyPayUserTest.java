package cn.com.open.usercenter.test;

import cn.com.open.openpaas.userservice.app.tools.DateTools;
import cn.com.open.openpaas.userservice.app.tools.HMacSha1;
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
import org.testng.Assert;

import javax.servlet.Filter;
import java.util.*;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/spring/appCtx-disconf.xml",
        "classpath*:/spring/context.xml", "classpath*:/spring/job.xml",
        "classpath*:/spring/security.xml", "classpath*:/spring/transaction.xml" })
public class VerifyPayUserTest {

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
    public void verifyPayUserTest() {
        try {
            String appId = Common.CLIENT_ID;
            String appSecret = Common.CLIENT_SECRET;
            String timestamp = DateTools.getSolrDate(new Date());
            String signatureNonce= StringTool.getRandom(100,1);
            String sourceId = Common.SOURCE_ID;
            SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
            sParaTemp.put("app_id",appId);
            sParaTemp.put("timestamp", timestamp);
            sParaTemp.put("signatureNonce", signatureNonce);
            sParaTemp.put("source_id", sourceId);
            String params=createSign(sParaTemp);
            String signature = HMacSha1.HmacSHA1Encrypt(params, appSecret);
            MvcResult result = mockMvc.perform(
                    MockMvcRequestBuilders.post("/user/verify/payUser")
                            .param("app_id", appId)
                            .param("timestamp", timestamp)
                            .param("signatureNonce", signatureNonce)
                            .param("signature", signature)
                            .param("source_id", sourceId)
            ).andReturn();
            String str = result.getResponse().getContentAsString();
            JSONObject jsonObject = JSONObject.parseObject(str);
            String status = jsonObject.getString("status");
            Assert.assertEquals("1", status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成加密串
     * @param parameters
     * @return signature
     */
    private static String createSign(SortedMap<Object, Object> parameters){
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            if(null != v && !"".equals(v)&& !"null".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        String temp_params = sb.toString();
        return sb.toString().substring(0, temp_params.length()-1);
    }
}
