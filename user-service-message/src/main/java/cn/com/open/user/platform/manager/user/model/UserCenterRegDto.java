package cn.com.open.user.platform.manager.user.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 15-5-18
 * <p/>
 * http://localhost:8080/oauth/token?client_id=unity-client&client_secret=unity&grant_type=authorization_code&code=zLl170&redirect_uri=http%3a%2f%2flocalhost%3a8080%2funity%2fdashboard.htm
 *
 * 
 */
public class UserCenterRegDto implements Serializable {

	private String username;
	private String password;
    private String userCenterRegUri;
    private String client_id;
    private String clientSecret;
    private String access_token;
    private String grant_type = "authorization_code";
    private String scope;    
    private String source_id;
    private String nickname;    
    private String real_name;
    private String head_picture;
    private String identify_type;
    private String identify_no;
    private String phone;
    private String email;
    private String user_type;
    private String user_state;
    private String sex;
    private String nation;
    private String politics;
    private String birthday;
    private String age;
    private String marriage;
    private String education;
    private String occupation;
    private String region;
    private String province;
    private String city;
    private String hukou;
    private String create_time;
    private String qq;
    private String weixin;
    private String weibo;
    private String blog;
    private String others;
    private String address;
    private String school;
    private String major;
    private String start_date;
    private String end_date;
    private String study_description;
    private String study_level;
    private String home_address;
    private String tel;
    private String fax;
    private String begin_date;
    private String finish_date;
    private String now_address;
    private String company;
    private String description;
    private String position;
    private String responsibility;
    private String work_content;
    private String card_no;
    private String guid;
    private String emailActivation;
    private String passwordSalt;
    private String isbak;
    private boolean defaultUser = false;
    private String aesPassword;
    private String signature;
    private String timestamp;
    private String signatureNonce;
    private String isValidate;
    private String id;
    private String appUid;
    private String whetherBind;
    private String methordName;
    public String getWhetherBind() {
		return whetherBind;
	}

	public void setWhetherBind(String whetherBind) {
		this.whetherBind = whetherBind;
	}

	public String getAppUid() {
		return appUid;
	}

	public void setAppUid(String appUid) {
		this.appUid = appUid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsValidate() {
		return isValidate;
	}

	public void setIsValidate(String isValidate) {
		this.isValidate = isValidate;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSignatureNonce() {
		return signatureNonce;
	}

	public void setSignatureNonce(String signatureNonce) {
		this.signatureNonce = signatureNonce;
	}

	public String getIsbak() {
		return isbak;
	}

	public void setIsbak(String isbak) {
		this.isbak = isbak;
	}

	public UserCenterRegDto() {
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

	public String getUserCenterRegUri() {
		return userCenterRegUri;
	}

	public void setUserCenterRegUri(String userCenterRegUri) {
		this.userCenterRegUri = userCenterRegUri;
	}


	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
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

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getHukou() {
		return hukou;
	}

	public void setHukou(String hukou) {
		this.hukou = hukou;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getWeibo() {
		return weibo;
	}

	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}

	public String getBlog() {
		return blog;
	}

	public void setBlog(String blog) {
		this.blog = blog;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
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

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getResponsibility() {
		return responsibility;
	}

	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStudy_description() {
        return study_description;
    }

    public void setStudy_description(String study_description) {
        this.study_description = study_description;
    }

    public String getStudy_level() {
        return study_level;
    }

    public void setStudy_level(String study_level) {
        this.study_level = study_level;
    }

    public String getHome_address() {
        return home_address;
    }

    public void setHome_address(String home_address) {
        this.home_address = home_address;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getFinish_date() {
        return finish_date;
    }

    public void setFinish_date(String finish_date) {
        this.finish_date = finish_date;
    }

    public String getNow_address() {
        return now_address;
    }

    public void setNow_address(String now_address) {
        this.now_address = now_address;
    }

    public String getWork_content() {
        return work_content;
    }

    public void setWork_content(String work_content) {
        this.work_content = work_content;
    }

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getHead_picture() {
        return head_picture;
    }

    public void setHead_picture(String head_picture) {
        this.head_picture = head_picture;
    }

    public String getIdentify_type() {
        return identify_type;
    }

    public void setIdentify_type(String identify_type) {
        this.identify_type = identify_type;
    }

    public String getIdentify_no() {
        return identify_no;
    }

    public void setIdentify_no(String identify_no) {
        this.identify_no = identify_no;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_state() {
        return user_state;
    }

    public void setUser_state(String user_state) {
        this.user_state = user_state;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getEmail_ctivation() {
		return emailActivation;
	}

	public void setEmail_activation(String emailActivation) {
		this.emailActivation = emailActivation;
	}
	
	public String getPassword_salt() {
		return passwordSalt;
	}

	public void setPassword_salt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public boolean isDefaultUser() {
		return defaultUser;
	}

	public void setDefaultUser(boolean defaultUser) {
		this.defaultUser = defaultUser;
	}

	public String getAesPassword() {
		return aesPassword;
	}

	public void setAesPassword(String aesPassword) {
		this.aesPassword = aesPassword;
	}

	public String getMethordName() {
		return methordName;
	}

	public void setMethordName(String methordName) {
		this.methordName = methordName;
	}

    
    
    
}
