/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.result.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.result.entity.Result;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 结果DAO接口
 *
 * @author njzhenghao
 * @version 2017-04-23
 */
@MyBatisDao
public interface ResultDao extends CrudDao<Result> {
    /**
     * 根据题目编号和用户编号查询结果
     *
     * @param
     * @param
     * @return Result
     */
    public Result getResult(Map<String, String> queryParam);

    public List<Result> findListID(Result result);

    @Select("SELECT COUNT(1) FROM result WHERE eid = #{eid} AND answer='0'")
    public int countCorrectResult(Map<String, String> map);

}