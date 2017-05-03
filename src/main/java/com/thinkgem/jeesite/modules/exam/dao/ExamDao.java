/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.exam.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.exam.entity.Exam;

/**
 * 题目DAO接口
 * @author njzhenghao
 * @version 2017-04-25
 */
@MyBatisDao
public interface ExamDao extends CrudDao<Exam> {
	
}