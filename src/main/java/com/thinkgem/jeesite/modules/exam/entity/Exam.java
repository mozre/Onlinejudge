/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.exam.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 题目Entity
 * @author njzhenghao
 * @version 2017-04-25
 */
public class Exam extends DataEntity<Exam> {
	
	private static final long serialVersionUID = 1L;
	private String ename;		// 题目名称
	private String detail;		// 题目内容
	private String lan;		// 题目语言(0-c,1-c++,2-python)
	private String inArgsType;		// 输入参数类型
	private String outArgsType;		// 输出参数类型
	private String inArgs;		// 输入参数
	private String outArgs;		// 输出参数
	private Date deadline;		// 提交截止时间
	
	public Exam() {
		super();
	}

	public Exam(String id){
		super(id);
	}

	@Length(min=1, max=100, message="题目名称长度必须介于 1 和 100 之间")
	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}
	
	@Length(min=1, max=1024, message="题目内容长度必须介于 1 和 1024 之间")
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	@Length(min=1, max=1, message="题目语言(0-c,1-c++,2-python)长度必须介于 1 和 1 之间")
	public String getLan() {
		return lan;
	}

	public void setLan(String lan) {
		this.lan = lan;
	}
	
	@Length(min=1, max=64, message="输入参数类型长度必须介于 1 和 64 之间")
	public String getInArgsType() {
		return inArgsType;
	}

	public void setInArgsType(String inArgsType) {
		this.inArgsType = inArgsType;
	}
	
	@Length(min=1, max=64, message="输出参数类型长度必须介于 1 和 64 之间")
	public String getOutArgsType() {
		return outArgsType;
	}

	public void setOutArgsType(String outArgsType) {
		this.outArgsType = outArgsType;
	}
	
	@Length(min=1, max=64, message="输入参数长度必须介于 1 和 64 之间")
	public String getInArgs() {
		return inArgs;
	}

	public void setInArgs(String inArgs) {
		this.inArgs = inArgs;
	}
	
	@Length(min=1, max=64, message="输出参数长度必须介于 1 和 64 之间")
	public String getOutArgs() {
		return outArgs;
	}

	public void setOutArgs(String outArgs) {
		this.outArgs = outArgs;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="提交截止时间不能为空")
	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	
}