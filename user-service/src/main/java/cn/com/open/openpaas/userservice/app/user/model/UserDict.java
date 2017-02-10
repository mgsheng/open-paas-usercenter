package cn.com.open.openpaas.userservice.app.user.model;


/**
 * 用户相关字典表
 */
public class UserDict {
	//字典类型_参数
	public static int TYPE_MARRIAGE = 10;	//婚姻
	public static int TYPE_PROVINCE = 20;	//省市
	public static int TYPE_NATION = 30;		//民族
	public static int TYPE_POLITICS = 40;	//政治面貌
	public static int TYPE_PROBLEM = 50;	//密保问题
	
	Integer id;
	String name;							//字典名称
	int type;								//字典类型
	long createTime;						//创建时间
	
	
	public String getName() {
		return name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	
}