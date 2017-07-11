package cn.com.open.user.app.common;

/**
 * Created by guxuyang on 07/07/2017.
 */
public enum PlatformEnum {

    PC("1","pc端"),
    PHONE("2", "phone端"),
    APP("3","app端")
    ;
    String code;
    String message;

    PlatformEnum() {
    }

    PlatformEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static PlatformEnum getByCode(String code) {
        PlatformEnum[] values = PlatformEnum.values();
        for (PlatformEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
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
