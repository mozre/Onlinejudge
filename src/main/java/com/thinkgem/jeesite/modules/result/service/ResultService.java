/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.result.service;

import java.util.*;

import com.sun.org.apache.regexp.internal.RE;
import com.thinkgem.jeesite.modules.exam.entity.Exam;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.result.entity.Result;
import com.thinkgem.jeesite.modules.result.dao.ResultDao;

/**
 * 结果Service
 *
 * @author njzhenghao
 * @version 2017-04-23
 */
@Service
@Transactional(readOnly = true)
public class ResultService extends CrudService<ResultDao, Result> {

    @Autowired
    ResultDao resultDao;

    public Result get(String id) {
        return super.get(id);
    }

    public Result getResult(String eid, String uid) {
        Map<String, String> queryParam = new HashMap<String, String>();
        queryParam.put("eid", eid);
        queryParam.put("uid", uid);
        return resultDao.getResult(queryParam);
    }

    public List<Result> findList(Result result) {
        return super.findList(result);
    }

    public List<Result> findListID(Result result) {
        return resultDao.findListID(result);
    }

    public Page<Result> findPage(Page<Result> page, Result result) {
        return super.findPage(page, result);
    }

    @Transactional(readOnly = false)
    public void save(Result result) {
        super.save(result);
    }

    @Transactional(readOnly = false)
    public void delete(Result result) {
        super.delete(result);
    }

    /**
     * 获取输入输出参数
     *
     * @param exam
     * @return
     */
    public Map<String, Object> getArgs(Exam exam, String filePath) {
        Map<String, Object> args = new HashMap<String, Object>();
        String[] inArgs = exam.getInArgs().split(",");
        String outArgs = exam.getOutArgs();
        args.put("inArgs", inArgs);
        args.put("outArgs", outArgs);
        args.put("filePath", filePath);
        return args;
    }
}