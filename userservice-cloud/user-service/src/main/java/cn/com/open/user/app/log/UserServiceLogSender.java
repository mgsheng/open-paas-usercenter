package cn.com.open.user.app.log;

import com.alibaba.fastjson.JSONObject;

import cn.com.open.user.app.log.support.ThirdPartyCallLog;
import cn.com.open.user.app.log.support.UserServiceLog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class UserServiceLogSender {

    private final static Logger log= LoggerFactory.getLogger(UserServiceLogSender.class);
    private static final String LOG_DB_NAME = "usercloudservicelog";

    @Autowired
    private RestTemplate restTemplate;
    @Value("${base.service.log.url}")
    private String baseServiceLogUrl;

    /**
     * 记录接口调用日志
     * @param appToolServiceLog log model
     */
    public void sendServiceLog(UserServiceLog appToolServiceLog) {
        MultiValueMap<String, Object> logMap = new LinkedMultiValueMap<>();
        logMap.add("tag", LOG_DB_NAME);
        logMap.add("logData", JSONObject.toJSONString(appToolServiceLog));
        new Thread(() -> sendLog(logMap)).start();
    }

    /**
     * 记录图片服务调用第三方接口的操作日志
     */
    public void sendThirdPartyCallLog(ThirdPartyCallLog thirdPartyCallLog) {
        MultiValueMap<String, Object> logMap = new LinkedMultiValueMap<>();
        logMap.add("tag", LOG_DB_NAME);
        logMap.add("logData", JSONObject.toJSONString(thirdPartyCallLog));
        new Thread(() -> sendLog(logMap)).start();
    }

    private void sendLog(MultiValueMap<String, Object> logMap) {
        try {
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
            headers.setContentType(type);
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(logMap, headers);
            ResponseEntity<String> result = restTemplate.exchange(baseServiceLogUrl, HttpMethod.POST, entity, String.class);
            log.info(result.getBody());
        } catch (Exception e) {
            log.error("调用基础服务日志接口异常", e);
        }
    }

}
