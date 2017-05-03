/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.exam.service;

import java.util.List;

import com.thinkgem.jeesite.modules.result.entity.Result;

import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.exam.entity.Exam;
import com.thinkgem.jeesite.modules.exam.dao.ExamDao;


/**
 * 题目Service
 *
 * @author njzhenghao
 * @version 2017-03-01
 */
@Service
@Transactional(readOnly = true)
public class ExamService extends CrudService<ExamDao, Exam> {

    public Exam get(String id) {
        return super.get(id);
    }

    public List<Exam> findList(Exam exam) {
        return super.findList(exam);
    }

    public Page<Exam> findPage(Page<Exam> page, Exam exam) {
        return super.findPage(page, exam);
    }

    //将Exam组装为Result
    public Result doExam(Exam exam) {
        Result result = new Result();
        result.setUid(UserUtils.getUser().getId());
        result.setEid(exam.getId());
        result.setId(result.getUid() + result.getEid());

        return result;
    }

    @Transactional(readOnly = false)
    public void save(Exam exam) {
        super.save(exam);
    }

    @Transactional(readOnly = false)
    public void delete(Exam exam) {
        super.delete(exam);
    }

}