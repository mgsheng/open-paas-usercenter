package cn.com.open.openpaas.userservice.app.user.model;


/**
 * 
 */
public class UserWork {

	private Integer app_uid;
    private Integer user_id;
    private Integer app_id;
    private Integer beginDate;		//开始日期
    private Integer endDate;		//结束日期
    private String address;			//公司地址
    private String company;			//公司
    private String description;		//工作描述
    private String position;		//职位
    private String responsibility;	//岗位职责
    private String workContent;		//简历
    private Long create_time;

    public UserWork() {
    }

	public UserWork(Integer app_uid,Integer user_id, Integer beginDate, Integer endDate,
			String address, String company, String description,String position,
			String responsibility, String workContent, Long create_time) {
		super();
		this.app_uid=app_uid;
		this.user_id = user_id;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.address = address;
		this.company = company;
		this.description = description;
		this.position=position;
		this.responsibility = responsibility;
		this.workContent = workContent;
		this.create_time = create_time;
	}
	
	public Integer getApp_uid() {
		return app_uid;
	}

	public void setApp_uid(Integer app_uid) {
		this.app_uid = app_uid;
	}

	public Integer getUser_id() {
		return user_id;
	}
	
	public Integer getApp_id() {
		return app_id;
	}

	public void setApp_id(Integer appId) {
		app_id = appId;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Integer beginDate) {
		this.beginDate = beginDate;
	}

	public Integer getEndDate() {
		return endDate;
	}

	public void setEndDate(Integer endDate) {
		this.endDate = endDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getResponsibility() {
		return responsibility;
	}

	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}

	public String getWorkContent() {
		return workContent;
	}

	public void setWorkContent(String workContent) {
		this.workContent = workContent;
	}

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{app_uid='").append(app_uid).append('\'');
        sb.append(", user_id='").append(user_id).append('\'');
        sb.append(", begin_date='").append(beginDate).append('\'');
        sb.append(", end_date='").append(endDate).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", company='").append(company).append('\'');
        sb.append(", decription='").append(description).append('\'');
        sb.append(", position='").append(position).append('\'');
        sb.append(", responsibility='").append(responsibility).append('\'');
        sb.append(", work_content='").append(workContent).append('\'');
        sb.append(", create_time='").append(create_time).append('\'');
        sb.append('}');
        return sb.toString();
    }
}