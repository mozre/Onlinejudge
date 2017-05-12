/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.similar.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 查重Entity
 * @author nzjhenghao
 * @version 2017-05-04
 */
public class Similar extends DataEntity<Similar> {
	
	private static final long serialVersionUID = 1L;
	private String eid;		// 题目编号
	private String uid1;		// 学号1
	private String uid2;		// 学号2
	private String similarRate;		// 相似率
	
	public Similar() {
		super();
	}

	public Similar(String id){
		super(id);
	}

	@Length(min=1, max=64, message="题目编号长度必须介于 1 和 64 之间")
	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}
	
	@Length(min=1, max=64, message="学号1长度必须介于 1 和 64 之间")
	public String getUid1() {
		return uid1;
	}

	public void setUid1(String uid1) {
		this.uid1 = uid1;
	}
	
	@Length(min=1, max=64, message="学号2长度必须介于 1 和 64 之间")
	public String getUid2() {
		return uid2;
	}

	public void setUid2(String uid2) {
		this.uid2 = uid2;
	}
	
	public String getSimilarRate() {
		return similarRate;
	}

	public void setSimilarRate(String similarRate) {
		this.similarRate = similarRate;
	}
	
}