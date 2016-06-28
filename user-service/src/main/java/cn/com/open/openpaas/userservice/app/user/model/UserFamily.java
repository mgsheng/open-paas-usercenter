package cn.com.open.openpaas.userservice.app.user.model;


/**
 * 
 */
public class UserFamily {

    private Integer user_id;

    private String address;
    private String company;
    private String title;//职位
    private String description;
    private String email;
    private String name;
    private String phone;
    private String relation;
    private String tel;
    private String fax;
    private Long createTime;
    

    public UserFamily() {
    }

	public UserFamily(Integer user_id, String address, String company,
			String title, String description, String email, String name,
			String phone, String tel, String fax,
			Long createTime) {
		super();
		this.user_id = user_id;
		this.address = address;
		this.company = company;
		this.title = title;
		this.description = description;
		this.email = email;
		this.name = name;
		this.phone = phone;
		this.tel = tel;
		this.fax = fax;
		this.createTime = createTime;
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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
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
        sb.append(", user_id='").append(user_id).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", company='").append(company).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", relation='").append(relation).append('\'');
        sb.append(", tel='").append(tel).append('\'');
        sb.append(", create_time='").append(createTime).append('\'');
        sb.append(", fax='").append(fax).append('\'');
        sb.append('}');
        return sb.toString();
    }
}