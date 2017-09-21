package cn.com.open.usercenter.test;

import cn.com.open.openpaas.userservice.app.tools.HMacSha1;
import com.alibaba.fastjson.JSONObject;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

public class Common {

    static final String APP_ID = "23";
    static final String GRANT_TYPE = "client_credentials";
    static final String CLIENT_ID = "4194b8dbd6624131bfccbdd6f71407760";
    static final String CLIENT_SECRET = "0f8ddd15fa544991b5ec23af210d7091";
    static final String SCOPE = "read,write";

    //测试账号
    static final String GUID = "53431461b79c4361a540d783dd73576e";
    static final String SOURCE_ID = "10000";
    static final String USERNAME = "gxytest1111";
    static final String PHONE = "13699246974";
    static final String PASSWORD = "123456abc";
    static final String EMAIL = "it_gxy@163.com";

    private Common(){}

    static Map<String, String> getAccessMap(MockMvc mockMvc) {
        try {
            MvcResult result = mockMvc.perform(
                    MockMvcRequestBuilders.post("/oauth/token")
                            .param("grant_type", GRANT_TYPE)
                            .param("client_id", CLIENT_ID)
                            .param("client_secret", CLIENT_SECRET)
                            .param("scope", SCOPE)).andReturn();
            String json = result.getResponse().getContentAsString();


            JSONObject jsonObject = JSONObject.parseObject(json);
            HashMap<String, String> map = new HashMap<>();
            map.put("access_token",  jsonObject.getString("access_token"));
            map.put("refresh_token", jsonObject.getString("refresh_token"));
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    static String getAccessToken(MockMvc mockMvc) {
        return getAccessMap(mockMvc).get("access_token");
    }

    static String getSignature(String accessToken, String timestamp, String signatureNonce) throws Exception {
        String encryptText = String.format("%s&%s&%s&%s", CLIENT_ID, accessToken, timestamp, signatureNonce);
        return HMacSha1.HmacSHA1Encrypt(encryptText, CLIENT_SECRET);
    }
}