package cn.com.open.user.app.common;

/**
 * ISP enum
 *
 * Created by guxuyang on 06/07/2017.
 */
public enum ServiceProviderEnum {

    AliyunCutout("aliyun-cutout","10000"),
    AliyunOrc("aliyun-orc", "10001"),
    BaiduyunFace("baiduyun-face", "10002")
    ;
    String name;
    String value;

    ServiceProviderEnum() {
    }

    ServiceProviderEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static String getNameByValue(String serviceProvider) {
        ServiceProviderEnum[] values = ServiceProviderEnum.values();
        for (ServiceProviderEnum value : values) {
            if (value.getValue().equals(serviceProvider)) {
                return value.getName();
            }
        }
        return null;
    }
}
