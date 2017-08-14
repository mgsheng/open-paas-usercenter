package cn.com.open.openpaas.userservice.app.user.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import cn.com.open.openpaas.userservice.app.infrastructure.model.DateUtils;
import cn.com.open.openpaas.userservice.app.shared.model.GuidGenerator;
import cn.com.open.openpaas.userservice.app.tools.AESUtil;
import cn.com.open.openpaas.userservice.app.tools.Help_Encrypt;
import cn.com.open.openpaas.userservice.app.tools.MD5;
import cn.com.open.openpaas.userservice.app.tools.StringTool;


/**
 * 
 */
public class UserCache{

	private int id;
	private int appId;
	private int userId;
    private boolean archived;
    private String guid = GuidGenerator.generate();
    private long createTime = DateUtils.now().getTime();
	
	private String username;
    private String password;
    private String passwordSalt;

    private String phone;
    private String email;
    private String cardNo;
    //Default user is initial when create database, do not delete
    private boolean defaultUser = false; //标记该用户是否有其他用户关联 true：有
    private Integer  process;  // 0:未处理，1：处理
    private String nickName;
    private String realName;
    private String headPicture;
    
    private String identifyNo;

    private Date lastLoginTime;
    
    private String userState;

    private String pid;
    
    private Integer identifyType;
    private Integer userType;
    private Integer userSercure;
    private Integer emailActivation;
    
    private String desPassword;
    private String aesPassword;
    private String sha1Password;
    private String md5Password;
    private String md5Salt;
    private Date updatePwdTime;
    
    
	public Date getUpdatePwdTime() {
		return updatePwdTime;
	}

	public void setUpdatePwdTime(Date updatePwdTime) {
		this.updatePwdTime = updatePwdTime;
	}

    
    private List<Privilege> privileges = new ArrayList<Privilege>();
    

    public String getMd5Password() {
		return md5Password;
	}

	public void setMd5Password(String md5Password) {
		this.md5Password = md5Password;
	}

	public UserCache() {
    }
    
    public UserCache(String username, String password, String phone, String email) {
        this.username = username;
        this.buildPasswordSalt();
        this.setPlanPassword(password);
        this.phone = phone;
        this.email = email;
    }
    
    public UserCache(String username, String password, String phone, String email, String nickName, String realName, String headPicture) {
        this.username = username;
        this.buildPasswordSalt();
        this.setPlanPassword(password);
        this.phone = phone;
        this.email = email;
        this.nickName = nickName;
        this.realName = realName;
        this.headPicture = headPicture;
    }
    public UserCache(String phone, String email, String realName, String identifyNo, Boolean defaultUser) {
        this.phone = phone;
        this.email = email;
        this.realName = realName;
        this.identifyNo = identifyNo;
    }
    
    /**
     * 自定义密码加密排列
     * @param password
     * @return password+passwordSalt
     */
    public String customPassword(String password){
    	if(md5Salt!=null){
    		return password+md5Salt;
    	}
    	return password;
    }
    
    /**
     * 随机生成MD5盐值
     */
    public void buildPasswordSalt() {
    	this.md5Salt = StringTool.getRandomString(16);
    }
    
    /**
     * 密码加密
     * @param planPassword
     */
    public void setPlanPassword(String planPassword) {
        this.md5Password = MD5.Md5(customPassword(planPassword));
    }
    
    /**
     * 密码AES加密
     * @param planPassword
     */
    public void setPlanPasswordByAes(String planPassword,String key) {
        //this.password = MD5.Md5(customPassword(planPassword));
        try {
			this.aesPassword = AESUtil.encrypt(planPassword, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 验证密码是否正确
     * @param password
     * @return
     */
    public boolean checkPasswod(String password){
    	if(this.password==null||"".equals(this.password)){
    		return false;
    	}
    	if(this.password.equals(MD5.Md5(customPassword(password)))){
    		return true;
    	}
    	//如果des密码符合也算正确
    	if(StringUtils.isNotBlank(this.desPassword)){
    		try {
				if(password.equals(Help_Encrypt.decrypt(this.desPassword))){
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	return false;
    }

    /**
     * 验证密码是否正确
     * @param password
     * @return
     */
    public boolean checkPasswodByAes(String password,String key,String pwdtype){
    	String aespwd="";
    	//aes密码验证
    	if(this.aesPassword!=null&&StringUtils.isNotBlank(this.aesPassword)){
    		try {
        		aespwd=AESUtil.decrypt(this.aesPassword,key.substring(0,16)).trim();
    		} catch (Exception e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
		    if(aespwd.equals(password)){
						return true;
			}
    	}else{
	    	if(StringUtils.isNotBlank(password)&&pwdtype.equals("MD5")){
	    		if(this.md5Password!=null&&!"".equals(this.md5Password)){
	    			if(this.md5Password.equals(MD5.Md5(customPassword(password)))){
		        		return true;
		        	}	
	    		}
	    	}
			// md5加密方式验证
			else if (StringUtils.isNotBlank(password)
					&& pwdtype.equals("MD5_20")) {
				if (this.md5Password != null && !"".equals(this.md5Password)) {
					if (this.md5Password.equals(MD5
							.Md5_20(customPassword(password)))) {
						return true;
					}
				}
			}
	    	//sha1_password 密码验证
	    	
	    	else	if(StringUtils.isNotBlank(password)&&pwdtype.equals("SHA1")){
				if (this.sha1Password != null && !"".equals(this.sha1Password)) {
					PasswordEncoder passwordEncoder = new ShaPasswordEncoder();
					password = passwordEncoder.encodePassword(password, null)
							.toLowerCase();
					if (password.equals(this.sha1Password.toLowerCase())) {
						return true;
					}
				}
			}
    	}
    	return false;
    }
    /*public boolean checkPasswodByAes(String password,String key){
    	if(this.aesPassword==null||"".equals(this.aesPassword)){
    		return false;
    	}
    	String aespwd="";
    	try {
    		aespwd=AESUtil.decrypt(this.aesPassword,key).trim();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	//aes密码验证
    	if(StringUtils.isNotBlank(password)){
					if(aespwd.equals(password)){
						return true;
					}
    	}
    	return false;
    }*/
    
    public boolean defaultUser() {
        return defaultUser;
    }

    public List<Privilege> privileges() {
        return privileges;
    }

	public String username() {
		return username;
	}

	public void username(String username) {
		this.username = username;
	}

	public String password() {
		return password;
	}

	public void password(String password) {
        buildPasswordSalt();
        setPlanPassword(password);
//		this.password = password;
	}

	public String phone() {
		return phone;
	}

	public void phone(String phone) {
		this.phone = phone;
	}

	public String email() {
		return email;
	}

	public void email(String email) {
		this.email = email;
	}

	public String nickName() {
		return nickName;
	}

	public void nickName(String nickName) {
		this.nickName = nickName;
	}

	public String realName() {
		return realName;
	}

	public void realName(String realName) {
		this.realName = realName;
	}

	public String headPicture() {
		return headPicture;
	}

	public void headPicture(String headPicture) {
		this.headPicture = headPicture;
	}

	public String identifyNo() {
		return identifyNo;
	}

	public void identifyNo(String identifyNo) {
		this.identifyNo = identifyNo;
	}

	public Date lastLoginTime() {
		return lastLoginTime;
	}

	public void lastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String userState() {
		return userState;
	}

	public void userState(String userState) {
		this.userState = userState;
	}

	public String pid() {
		return pid;
	}

	public void pid(String pid) {
		this.pid = pid;
	}

	public Integer identifyType() {
		return identifyType;
	}

	public void identifyType(Integer identifyType) {
		this.identifyType = identifyType;
	}

	public Integer userType() {
		return userType;
	}

	public void userType(Integer userType) {
		this.userType = userType;
	}

	public Integer userSercure() {
		return userSercure;
	}

	public void userSercure(Integer userSercure) {
		this.userSercure = userSercure;
	}

	public List<Privilege> getPrivileges() {
		return privileges;
	}
	
	public String passwordSalt(){
		return passwordSalt;
	}
	
	public void passwordSalt(String passwordSalt){
		this.passwordSalt = passwordSalt;
	}
	
	public String cardNo(){
		return cardNo;
	}
	
	public void cardNo(String cardNo){
		this.cardNo = cardNo;
	}
	
	public int id(){
		return id;
	}
	
	public void id(int id){
		this.id = id;
	}
	
	public boolean archived() {
        return archived;
    }
	
	public void archived(boolean archived) {
        this.archived = archived;
    }

    public String guid() {
        return guid;
    }

    public void guid(String guid) {
        this.guid = guid;
    }

    public long createTime() {
        return createTime;
    }
    
    public void createTime(long createTime) {
        this.createTime = createTime;
    }
    
    public Integer emailActivation() {
        return emailActivation;
    }
    
    public void emailActivation(Integer emailActivation) {
        this.emailActivation = emailActivation;
    }
    
    public int appId() {
        return appId;
    }
    
    public void appId(int appId) {
        this.appId = appId;
    }
    
    public int userId() {
        return userId;
    }
    
    public void userId(int userId) {
        this.userId = userId;
    }
    
    public String desPassword() {
        return desPassword;
    }
    
    public void desPassword(String desPassword) {
        this.desPassword = desPassword;
    }

	public String getAesPassword() {
		return aesPassword;
	}

	public void setAesPassword(String aesPassword) {
		this.aesPassword = aesPassword;
	}

	public String getSha1Password() {
		return sha1Password;
	}

	public void setSha1Password(String sha1Password) {
		this.sha1Password = sha1Password;
	}

	public int process() {
		return process;
	}

	public void process(Integer process) {
		this.process = process;
	}

//	@Override
//    public String toString() {
//        final StringBuilder sb = new StringBuilder();
//        sb.append("{username='").append(username).append('\'');
//        sb.append(", phone='").append(phone).append('\'');
//        sb.append(", id='").append(id).append('\'');
//        sb.append(", guid='").append(guid).append('\'');
//        sb.append(", defaultUser='").append(defaultUser).append('\'');
//        sb.append(", email='").append(email).append('\'');
//        sb.append(", identifyNo='").append(identifyNo).append('\'');
//        sb.append(", realName='").append(realName).append('\'');
//        sb.append(", pid='").append(pid).append('\'');
//        sb.append(", identifyType='").append(identifyType).append('\'');
//        sb.append(", userType='").append(userType).append('\'');
//        sb.append(", userSercure='").append(userSercure).append('\'');
//        sb.append('}');
//        return sb.toString();
//    }

}