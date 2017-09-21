package cn.com.open.openpaas.userservice.app.common.model;

public class Result {

    public static final Integer SUCCESS = 1;
    public static final Integer ERROR = 0;

    private Integer status;
    private String message;
    private String errorCode;
    private Object payload;

    public Result() {
    }

    public Result(Integer status, String message, String errorCode, Object payload) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
        this.payload = payload;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

}