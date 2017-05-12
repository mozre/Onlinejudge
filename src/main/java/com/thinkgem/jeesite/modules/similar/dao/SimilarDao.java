/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.similar.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.similar.entity.Similar;

/**
 * 查重DAO接口
 * @author nzjhenghao
 * @version 2017-05-04
 */
@MyBatisDao
public interface SimilarDao extends CrudDao<Similar> {
	
}