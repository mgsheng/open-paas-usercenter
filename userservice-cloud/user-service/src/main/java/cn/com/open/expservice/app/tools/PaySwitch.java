package cn.com.open.expservice.app.tools;

/**
 * 开关
 * @author admin
 */
public enum PaySwitch{
    ALI("alibaba", 10001),EXP100("express100", 10002);  
   // 成员变量  
   private String value;  
   private int channel;  
   // 构造方法  
   private PaySwitch(String value, int channel) {  
       this.value = value;  
       this.channel = channel;  
   }
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getChannel() {
		return channel;
	}
	public void setChannel(int channel) {
		this.channel = channel;
	}  
}