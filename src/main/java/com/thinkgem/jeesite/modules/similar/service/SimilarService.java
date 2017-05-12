/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.similar.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.similar.entity.Similar;
import com.thinkgem.jeesite.modules.similar.dao.SimilarDao;

/**
 * 查重Service
 * @author nzjhenghao
 * @version 2017-05-04
 */
@Service
@Transactional(readOnly = true)
public class SimilarService extends CrudService<SimilarDao, Similar> {

	public Similar get(String id) {
		return super.get(id);
	}
	
	public List<Similar> findList(Similar similar) {
		return super.findList(similar);
	}
	
	public Page<Similar> findPage(Page<Similar> page, Similar similar) {
		return super.findPage(page, similar);
	}
	
	@Transactional(readOnly = false)
	public void save(Similar similar) {
		super.save(similar);
	}
	
	@Transactional(readOnly = false)
	public void delete(Similar similar) {
		super.delete(similar);
	}
	
}