package cn.com.open.user.app.log.support;

public class ThirdPartyCallLog {

	private String idNo;
	private String userName; 
    private String createTime;
    private double executionTime;
    private String logType;
    private String responseText;
    private String responseHeaderParam;
    
    
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public double getExecutionTime() {
		return executionTime;
	}
	public void setExecutionTime(double executionTime) {
		this.executionTime = executionTime;
	}
	public String getResponseText() {
		return responseText;
	}
	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}
	public String getResponseHeaderParam() {
		return responseHeaderParam;
	}
	public void setResponseHeaderParam(String responseHeaderParam) {
		this.responseHeaderParam = responseHeaderParam;
	}
     
}
