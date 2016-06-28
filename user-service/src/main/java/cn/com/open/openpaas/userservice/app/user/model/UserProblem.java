package cn.com.open.openpaas.userservice.app.user.model;

import java.util.Date;

/**
 * 用户密保问题
 */
public class UserProblem {

	private Integer id;
    private Integer userId;
    private Integer problemId;
    private String answer;
    private Long createTime = new Date().getTime();

    public UserProblem() {
    }
    
    public UserProblem(Integer user_id,Integer problem_id,String answer) {
    	this.setUserId(user_id);
    	this.setProblemId(problem_id);
    	this.setAnswer(answer);
    	this.setCreateTime(new Date().getTime());
    }
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getProblemId() {
		return problemId;
	}

	public void setProblemId(Integer problemId) {
		this.problemId = problemId;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}


}