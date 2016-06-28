package cn.com.open.openpaas.userservice.app.user.model;


/**
 * 
 */
public class UserContect {

	private Integer app_uid;
	private Integer user_id;

    private String address;
    private String head_picture;
    private String name;
    private String phone;
    private String phone_bak;
    private String phone_bak2;
    private Long create_time;

    public UserContect() {
    }

	public UserContect(Integer app_uid,Integer user_id,String address, String head_picture, String name,String phone, Long create_time) {
		super();
		this.app_uid=app_uid;
		this.user_id=user_id;
		this.address = address;
		this.head_picture = head_picture;
		this.name = name;
		this.phone = phone;
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

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getHead_picture() {
		return head_picture;
	}



	public void setHead_picture(String head_picture) {
		this.head_picture = head_picture;
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



	public String getPhone_bak() {
		return phone_bak;
	}



	public void setPhone_bak(String phone_bak) {
		this.phone_bak = phone_bak;
	}



	public String getPhone_bak2() {
		return phone_bak2;
	}



	public void setPhone_bak2(String phone_bak2) {
		this.phone_bak2 = phone_bak2;
	}



	public Long getCreate_time() {
		return create_time;
	}



	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}



	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{app_uid='").append(app_uid).append('\'');
        sb.append(", user_id='").append(user_id).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", head_picture='").append(head_picture).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", phone_bak='").append(phone_bak).append('\'');
        sb.append(", phone_bak2='").append(phone_bak2).append('\'');
        sb.append(", create_time='").append(create_time).append('\'');
        sb.append('}');
        return sb.toString();
    }
}