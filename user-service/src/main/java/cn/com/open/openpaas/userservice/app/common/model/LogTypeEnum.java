package cn.com.open.openpaas.userservice.app.common.model;

/**
 * Created by guxuyang on 07/07/2017.
 */
public enum LogTypeEnum {

    SERVICE("1","service"),   //自身被业务方调用的日志
    ThIRDPARTY("2", "thirdparty")     //调用第三方(百度，阿里)的日志
    ;
    String code;
    String message;

    LogTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
