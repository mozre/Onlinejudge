/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.result.entity;

import org.hibernate.validator.constraints.Length;
//import org.hibernate.validator.constraints.Length(min=1, max=1, message="编译");
//import org.hibernate.validator.constraints.Length(min=1, max=1, message="执行超时-0(不超时),1;");
//import org.hibernate.validator.constraints.Length(min=1, max=1, message="结果-0(正确),1;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 结果Entity
 * @author njzhenghao
 * @version 2017-04-23
 */
public class Result extends DataEntity<Result> {
	
	private static final long serialVersionUID = 1L;
	private String uid;		// uid
	private String eid;		// 题目编号
	private String code;		// code
	private String compile;		// 编译-0(通过),1(不通过)
	private String timeout;		// 执行超时-0(不超时),1(超时)
	private String answer;		// 结果-0(正确),1(错误)
	
	public Result() {
		super();
	}

	public Result(String id){
		super(id);
	}

	@Length(min=1, max=64, message="uid长度必须介于 1 和 64 之间")
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
	@Length(min=1, max=64, message="题目编号长度必须介于 1 和 64 之间")
	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}
	
	@Length(min=1, max=255, message="code长度必须介于 1 和 255 之间")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Length(min=1, max=1, message="编译-0(通过),1(不通过)长度必须介于 1 和 1 之间")
	public String getCompile() {
		return compile;
	}

	public void setCompile(String compile) {
		this.compile = compile;
	}
	
	@Length(min=1, max=1, message="执行超时-0(不超时),1(超时)长度必须介于 1 和 1 之间")
	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	
	@Length(min=1, max=1, message="结果-0(正确),1(错误)长度必须介于 1 和 1 之间")
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
}