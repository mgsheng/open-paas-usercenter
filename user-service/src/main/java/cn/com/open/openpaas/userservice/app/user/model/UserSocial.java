package cn.com.open.openpaas.userservice.app.user.model;


/**
 * 
 */
public class UserSocial {

	private Integer app_uid;
	private Integer app_id;
    private Integer user_id;

    private String qq;
    private String weixin;	//微信
    private String weibo;	//微博
    private String blog;	//博客
    private String others;	
    private Long create_time;

    public UserSocial() {
    }

	public UserSocial(Integer app_uid,Integer app_id,Integer user_id, String qq, String weixin, String weibo,
			String blog, String others, Long create_time) {
		super();
		this.app_uid=app_uid;
		this.app_id=app_id;
		this.user_id = user_id;
		this.qq = qq;
		this.weixin = weixin;
		this.weibo = weibo;
		this.blog = blog;
		this.others = others;
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

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}

	public Integer getApp_id() {
		return app_id;
	}

	public void setApp_id(Integer app_id) {
		this.app_id = app_id;
	}

	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{app_uid='").append(app_uid).append('\'');
        sb.append(", app_id='").append(app_id).append('\'');
        sb.append(", user_id='").append(user_id).append('\'');
        sb.append(", qq='").append(qq).append('\'');
        sb.append(", weibo='").append(weibo).append('\'');
        sb.append(", weixin='").append(weixin).append('\'');
        sb.append(", blog='").append(blog).append('\'');
        sb.append(", others='").append(others).append('\'');
        sb.append(", create_time='").append(create_time).append('\'');
        sb.append('}');
        return sb.toString();
    }
}