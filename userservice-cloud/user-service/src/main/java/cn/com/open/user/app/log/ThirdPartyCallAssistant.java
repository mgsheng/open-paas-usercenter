package cn.com.open.user.app.log;



import java.util.Date;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.com.open.user.app.common.LogTypeEnum;
import cn.com.open.user.app.http.HttpClientUtil;
import cn.com.open.user.app.log.support.ThirdPartyCallLog;
import cn.com.open.user.app.tools.DateTools;

@Component
public class ThirdPartyCallAssistant {

    private final static Logger log = LoggerFactory.getLogger(ThirdPartyCallAssistant.class);

    @Autowired
    private UserServiceLogSender userServiceLogSender;
    @Value("${api.thirdparty.log.onOff}")
    private String apiThirdpartyLogOnOff;
 
    public String doOAPost(String url, Map<String, String> params) throws Exception {
        long startTime = System.currentTimeMillis(); //请求开始时间
        String responsejson = null;
        try{
            responsejson = HttpClientUtil.httpPost(url, params);
            return responsejson;
        } finally {
            if ("on".equals(apiThirdpartyLogOnOff)) {
                long endTime = System.currentTimeMillis();
                ThirdPartyCallLog thirdPartyCallLog = new ThirdPartyCallLog();
                thirdPartyCallLog.setUserName(params.get("userName"));
                thirdPartyCallLog.setIdNo(params.get("idNo"));
                thirdPartyCallLog.setExecutionTime(endTime - startTime);
                thirdPartyCallLog.setLogType(LogTypeEnum.THIRDPARTY.getCode());
                thirdPartyCallLog.setCreateTime(DateTools.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                if (responsejson != null) {
                    thirdPartyCallLog.setResponseText(responsejson);
                    thirdPartyCallLog.setResponseHeaderParam(JSONObject.valueToString(responsejson));
                }
                userServiceLogSender.sendThirdPartyCallLog(thirdPartyCallLog);
            }
        }
    }
}
