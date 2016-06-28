package cn.com.open.openpaas.userservice.app.user.model;

import java.util.Date;

/**
 * 
 */
public class UserDetail {

	private Integer app_uid;
    private Integer user_id;

    private Integer sex;		//性别0：男；1：女
    private String nation;		//民族
    private String politics;	//政治面貌
    private Date birthday;		//生日
    private Integer age;		//年龄
    private String marriage;	//婚姻状态
    private String education;	//学历
    private String occupation;	//职业
    private String region;		//区域
    private String province;	//省市
    private String city;		//城市
    private Long createTime;
    

    public UserDetail() {
    }

	public UserDetail(Integer app_uid,Integer user_id, Integer sex, String nation,
			String politics, Date birthday, Integer age, String marriage,
			String education, String occupation, String region, String province, String city,
			Long createTime) {
		super();
		this.app_uid=app_uid;
		this.user_id = user_id;
		this.sex = sex;
		this.nation = nation;
		this.politics = politics;
		this.birthday = birthday;
		this.age = age;
		this.marriage = marriage;
		this.education = education;
		this.occupation = occupation;
		this.region = region;
		this.province=province;
		this.city = city;
		this.createTime = createTime;
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

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getPolitics() {
		return politics;
	}

	public void setPolitics(String politics) {
		this.politics = politics;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getMarriage() {
		return marriage;
	}

	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{app_uid='").append(app_uid).append('\'');
        sb.append(", user_id='").append(user_id).append('\'');
        sb.append(", sex='").append(sex).append('\'');
        sb.append(", nation='").append(nation).append('\'');
        sb.append(", politics='").append(politics).append('\'');
        sb.append(", birthday='").append(birthday).append('\'');
        sb.append(", age='").append(age).append('\'');
        sb.append(", marriage='").append(marriage).append('\'');
        sb.append(", education='").append(education).append('\'');
        sb.append(", occupation='").append(occupation).append('\'');
        sb.append(", region='").append(region).append('\'');
        sb.append(", province='").append(province).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", create_time='").append(createTime).append('\'');
        sb.append('}');
        return sb.toString();
    }
}