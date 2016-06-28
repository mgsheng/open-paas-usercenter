package cn.com.open.openpaas.userservice.app.user.model;

import java.util.Date;

/**
 * 
 */
public class UserEducation {

	private Integer app_uid;
    private Integer user_id;
    private Integer app_id;
    private String address;			//学校地址
    private String school;			//学校
    private String major;			//专业
    private Date startDate;			//开始时间
    private Date endDate;			//毕业时间
    private String description;		//学习信息描述
    private String studyLevel;		//学历
    private Long createTime;
    

    public UserEducation() {
    }

	public UserEducation(Integer app_uid,Integer user_id, String address, String school,
			String major, Date startDate, Date endDate, String description,
			String studyLevel, Long createTime) {
		super();
		this.app_uid=app_uid;
		this.user_id = user_id;
		this.address = address;
		this.school = school;
		this.major = major;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
		this.studyLevel = studyLevel;
		this.createTime = createTime;
	}
	
	public UserEducation(Integer app_uid,Integer user_id,Integer app_id, String address, String school,
			String major, Date startDate, Date endDate, String description,
			String studyLevel, Long createTime) {
		super();
		this.app_uid=app_uid;
		this.user_id = user_id;
		this.app_id = app_id;
		this.address = address;
		this.school = school;
		this.major = major;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
		this.studyLevel = studyLevel;
		this.createTime = createTime;
	}

	public Integer getApp_uid() {
		return app_uid;
	}

	public void setApp_uid(Integer appUid) {
		app_uid = appUid;
	}

	public Integer getApp_id() {
		return app_id;
	}

	public void setApp_id(Integer appId) {
		app_id = appId;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStudyLevel() {
		return studyLevel;
	}

	public void setStudyLevel(String studyLevel) {
		this.studyLevel = studyLevel;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{app_uid='").append(app_uid).append('\'');
        sb.append(", user_id='").append(user_id).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", school='").append(school).append('\'');
        sb.append(", major='").append(major).append('\'');
        sb.append(", start_date='").append(startDate).append('\'');
        sb.append(", end_date='").append(endDate).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", study_level='").append(studyLevel).append('\'');
        sb.append(", create_time='").append(createTime).append('\'');
        sb.append('}');
        return sb.toString();
    }
}