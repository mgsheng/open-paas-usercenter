package com.andaily.springoauth.service.dto;

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
    private String synUserInfoUri;
    private String saveUserInfoUri;
    private String clientId;
    private String clientSecret;
    private String access_token;
    private String grantType = "authorization_code";    
    private String scope;    
    private String sourceId;    
    private String nickname;    
    private String realName;    
    private String headPicture;
    private String identifyType;
    private String identifyNo;
    private String phone;
    private String email;
    private String userType;
    private String userState;
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
    private String createTime;
    private String qq;
    private String weixin;
    private String weibo;
    private String blog;
    private String others;
    private String address;
    private String school;
    private String major;
    private String startDate;
    private String endDate;
    private String studyDescription;
    private String studyLevel;
    private String homeAddress;
    private String tel;
    private String fax;
    private String beginDate;
    private String finishDate;
    private String nowAddress;
    private String company;
    private String description;
    private String position;
    private String responsibility;
    private String workContent;
    private String cardNo;
    private String isbak;
    private String synUserNameUri;
    private String unBindUserInfoUri;
    private String aesPassword;
    private String isValidate;
    private String id;
    private String appUid;
    private String guid;
    private String whetherBind;
    
    
    
    public String getWhetherBind() {
		return whetherBind;
	}

	public void setWhetherBind(String whetherBind) {
		this.whetherBind = whetherBind;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppUid() {
		return appUid;
	}

	public void setAppUid(String appUid) {
		this.appUid = appUid;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getIsValidate() {
		return isValidate;
	}

	public void setIsValidate(String isValidate) {
		this.isValidate = isValidate;
	}

	public String getUnBindUserInfoUri() {
		return unBindUserInfoUri;
	}

	public void setUnBindUserInfoUri(String unBindUserInfoUri) {
		this.unBindUserInfoUri = unBindUserInfoUri;
	}

	public String getIsbak() {
		return isbak;
	}

	public void setIsbak(String isbak) {
		this.isbak = isbak;
	}

	public UserCenterRegDto() {
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

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getHeadPicture() {
		return headPicture;
	}

	public void setHeadPicture(String headPicture) {
		this.headPicture = headPicture;
	}

	public String getIdentifyType() {
		return identifyType;
	}

	public void setIdentifyType(String identifyType) {
		this.identifyType = identifyType;
	}

	public String getIdentifyNo() {
		return identifyNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public void setIdentifyNo(String identifyNo) {
		this.identifyNo = identifyNo;
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

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStudyDescription() {
		return studyDescription;
	}

	public void setStudyDescription(String studyDescription) {
		this.studyDescription = studyDescription;
	}

	public String getStudyLevel() {
		return studyLevel;
	}

	public void setStudyLevel(String studyLevel) {
		this.studyLevel = studyLevel;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
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

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}

	public String getNowAddress() {
		return nowAddress;
	}

	public void setNowAddress(String nowAddress) {
		this.nowAddress = nowAddress;
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

	public String getWorkContent() {
		return workContent;
	}

	public void setWorkContent(String workContent) {
		this.workContent = workContent;
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

	public String getSynUserInfoUri() {
		return synUserInfoUri;
	}

	public void setSynUserInfoUri(String synUserInfoUri) {
		this.synUserInfoUri = synUserInfoUri;
	}

	public String getSaveUserInfoUri() {
		return saveUserInfoUri;
	}

	public void setSaveUserInfoUri(String saveUserInfoUri) {
		this.saveUserInfoUri = saveUserInfoUri;
	}

	public String getSynUserNameUri() {
		return synUserNameUri;
	}

	public void setSynUserNameUri(String synUserNameUri) {
		this.synUserNameUri = synUserNameUri;
	}

	public String getAesPassword() {
		return aesPassword;
	}

	public void setAesPassword(String aesPassword) {
		this.aesPassword = aesPassword;
	}

	public String getFullUri() throws UnsupportedEncodingException {
        return String.format("%s?username=%s&isbak=%s&password=%s&client_id=%s&access_token=%s&grant_type=%s&scope=%s&source_id=%s&nickname=%s&real_name=%s&head_picture=%s&card_no=%s&identify_type=%s&identify_no=%s&phone=%s&email=%s&user_type=%s&user_state=%s&sex=%s&nation=%s&politics=%s&birthday=%s&age=%s&marriage=%s&education=%s&occupation=%s&region=%s&province=%s&city=%s&hukou=%s&create_time=%s&qq=%s&weixin=%s&weibo=%s&blog=%s&others=%s&address=%s&school=%s&major=%s&start_date=%s&end_date=%s&study_description=%s&study_level=%s&home_address=%s&tel=%s&fax=%s&begin_date=%s&finish_date=%s&now_address=%s&company=%s&description=%s&position=%s&responsibility=%s&work_content=%s",
        		userCenterRegUri,username,isbak,password, clientId, access_token, grantType, scope, sourceId,nickname,realName,headPicture,cardNo,identifyType,identifyNo,phone,email,userType,userState,sex,nation,politics,birthday,age,marriage,education,occupation,region,province,city,hukou,createTime,qq,weixin,weibo,blog,others,address,school,major,startDate,endDate,studyDescription,studyLevel,homeAddress,tel,fax,beginDate,finishDate,nowAddress,company,description,position,responsibility,workContent);  
    }
	public String getAesFullUri(String signature,String timestamp,String signatureNonce) throws UnsupportedEncodingException {
        return String.format("%s?username=%s&password=%s&client_id=%s&access_token=%s&grant_type=%s&scope=%s&source_id=%s&phone=%s&isValidate=%s&email=%s&signature=%s&timestamp=%s&signatureNonce=%s&card_no=%s",
        		userCenterRegUri,username,aesPassword, clientId, access_token, grantType, scope, sourceId,phone,isValidate,email,signature,timestamp,signatureNonce,cardNo);  
    }
	public String getSaveUri(String signature,String timestamp,String signatureNonce) throws UnsupportedEncodingException {
        return String.format("%s?username=%s&password=%s&client_id=%s&access_token=%s&grant_type=%s&scope=%s&source_id=%s&phone=%s&isValidate=%s&email=%s&signature=%s&timestamp=%s&signatureNonce=%s&card_no=%s&id=%s&guid=%s&appUid=%s",
        		saveUserInfoUri,username,aesPassword, clientId, access_token, grantType, scope, sourceId,phone,isValidate,email,signature,timestamp,signatureNonce,cardNo,id,guid,appUid);  
    }
	public String getsynUserInfoUri() throws UnsupportedEncodingException {
        return String.format("%s?username=%s&password=%s&client_id=%s&access_token=%s&grant_type=%s&scope=%s&source_id=%s&nickname=%s&real_name=%s&head_picture=%s&identify_type=%s&identify_no=%s&phone=%s&email=%s&user_type=%s&user_state=%s&sex=%s&nation=%s&politics=%s&birthday=%s&age=%s&marriage=%s&education=%s&occupation=%s&region=%s&province=%s&city=%s&hukou=%s&create_time=%s&qq=%s&weixin=%s&weibo=%s&blog=%s&others=%s&address=%s&school=%s&major=%s&start_date=%s&end_date=%s&study_description=%s&study_level=%s&home_address=%s&tel=%s&fax=%s&begin_date=%s&finish_date=%s&now_address=%s&company=%s&description=%s&position=%s&responsibility=%s&work_content=%s",
        		synUserInfoUri,username,password, clientId, access_token, grantType, scope, sourceId,nickname,realName,headPicture,identifyType,identifyNo,phone,email,userType,userState,sex,nation,politics,birthday,age,marriage,education,occupation,region,province,city,hukou,createTime,qq,weixin,weibo,blog,others,address,school,major,startDate,endDate,studyDescription,studyLevel,homeAddress,tel,fax,beginDate,finishDate,nowAddress,company,description,position,responsibility,workContent);  
    }
	public String getsynUserNameUri() throws UnsupportedEncodingException {
        return String.format("%s?username=%s&client_id=%s&access_token=%s&grant_type=%s&scope=%s&source_id=%s",
        		synUserNameUri,username,clientId, access_token, grantType, scope, sourceId);  
    }
	public String getUnBindUserUri(String signature,String timestamp,String signatureNonce) throws UnsupportedEncodingException {
        return String.format("%s?username=%s&client_id=%s&access_token=%s&grant_type=%s&scope=%s&source_id=%s&signature=%s&timestamp=%s&signatureNonce=%s",
        		unBindUserInfoUri,username,clientId, access_token, grantType, scope, sourceId,signature,timestamp,signatureNonce);  
    }
	public String getsaveUserInfoUri() throws UnsupportedEncodingException {
        return String.format("%s?username=%s&password=%s&client_id=%s&access_token=%s&grant_type=%s&scope=%s&source_id=%s&nickname=%s&real_name=%s&head_picture=%s&identify_type=%s&identify_no=%s&phone=%s&email=%s&user_type=%s&user_state=%s&sex=%s&nation=%s&politics=%s&birthday=%s&age=%s&marriage=%s&education=%s&occupation=%s&region=%s&province=%s&city=%s&hukou=%s&create_time=%s&qq=%s&weixin=%s&weibo=%s&blog=%s&others=%s&address=%s&school=%s&major=%s&start_date=%s&end_date=%s&study_description=%s&study_level=%s&home_address=%s&tel=%s&fax=%s&begin_date=%s&finish_date=%s&now_address=%s&company=%s&description=%s&position=%s&responsibility=%s&work_content=%s",
        		saveUserInfoUri,username,password, clientId, access_token, grantType, scope, sourceId,nickname,realName,headPicture,identifyType,identifyNo,phone,email,userType,userState,sex,nation,politics,birthday,age,marriage,education,occupation,region,province,city,hukou,createTime,qq,weixin,weibo,blog,others,address,school,major,startDate,endDate,studyDescription,studyLevel,homeAddress,tel,fax,beginDate,finishDate,nowAddress,company,description,position,responsibility,workContent);  
    }

	public String getSynInfoUri(String signature, String timestamp,String signatureNonce) {
		 return String.format("%s?client_id=%s&access_token=%s&grant_type=%s&scope=%s&source_id=%s&phone=%s&email=%s&signature=%s&timestamp=%s&signatureNonce=%s&whetherBind=%s",
	        		synUserInfoUri,clientId, access_token, grantType, scope, sourceId, phone, email,signature,timestamp,signatureNonce,whetherBind);  
	}
	
	/*public String getFullUri() throws UnsupportedEncodingException {
        return String.format("%s?username=%s&password=%s&client_id=%s&client_secret=%s&grant_type=%s&scope=%s&source_id=%s&nickname=%s&real_name=%s&head_picture=%s&identify_type=%s&identify_no=%s&phone=%s&email=%s&user_type=%s&suer_state=%s&sex=%s&nation=%s&politics=%s&birthday=%s&age=%s&marriage=%s&education=%s&occupation=%s&region=%s&province=%s&city=%s&hukou=%s&create_time=%s&qq=%s&weixin=%s&weibo=%s&blog=%s&others=%s&address=%s&school=%s&major=%s&start_date=%s&end_date=%s&study_description=%s&study_level=%s&home_address=%s&tel=%s&fax=%s&begin_date=%s&finish_date=%s&now_address=%s&company=%s&description=%s&position=%s&responsibility=%s&work_content=%s",
        		userCenterRegUri,username,password, clientId, clientSecret, grantType, scope, sourceId,nickname,realName,headPicture,identifyType,identifyNo,phone,email,userType,userState,sex,nation,politics,birthday,age,marriage,education,occupation,region,province,city,hukou,createTime,qq,weixin,weibo,blog,others,address,school,major,startDate,endDate,studyDescription,studyLevel,homeAddress,tel,fax,beginDate,finishDate,nowAddress,company,description,position,responsibility,workContent);  
    }*/
}
