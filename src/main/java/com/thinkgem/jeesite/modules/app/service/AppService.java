package com.thinkgem.jeesite.modules.app.service;

import com.alibaba.druid.support.json.JSONUtils;
import com.thinkgem.jeesite.modules.exam.entity.Exam;
import com.thinkgem.jeesite.modules.result.dao.ResultDao;
import com.thinkgem.jeesite.modules.result.entity.Result;
import com.thinkgem.jeesite.modules.result.service.ResultService;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by njzhenghao on 17-5-8.
 */
public class AppService {

    @Autowired
    ResultService resultService;

    @Autowired
    UserDao userDao;

    @Autowired
    ResultDao resultDao;

    /**
     * 将APP传入数据转换为Exam对象
     *
     * @param map
     * @return
     * @throws ParseException
     */
    public Exam convertToExam(Map<String, String> map) throws ParseException {
        Exam exam = new Exam();

        exam.setId(map.get("exam_id"));
        exam.setEname(map.get("exam_title"));
        exam.setLan(String.valueOf(map.get("exam_lan")));
        exam.setInArgs(map.get("exam_in_arg"));
        exam.setOutArgs(map.get("exam_out_arg"));
        String str = map.get("exam_deadline");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(str));
        Date date = calendar.getTime();
        exam.setDeadline(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期转换

//        exam.setDeadline(sdf.parse(map.get("exam_deadline")));
        exam.setDetail(map.get("exam_content"));
        exam.setRemarks(map.get("exam_remark"));

        return exam;
    }

    /**
     * 将Exam对象转换为app对象
     *
     * @param exam
     * @return
     */
    public Map<String, String> examConvertTo(Exam exam, String userId) {
        //结果Map
        Map<String, String> result = new HashMap<String, String>();

        //取对应题目的结果列表
        Result tempResult = new Result();
        tempResult.setDelFlag("0");
        tempResult.setEid(exam.getId());
        //TODO 这儿数据库查询异常
        List<Result> resultList = resultService.findList(tempResult);

        //取总学生数目
        Map<String, String> user = new HashMap<String, String>();
        user.put("del_flag", "0");
        user.put("user_type", "3");

        //取做对题目的学生数量
        Map<String, String> countCorrectResult = new HashMap<String, String>();
        countCorrectResult.put("eid", exam.getId());

        //存放结果
        result.put("exam_id", exam.getId());
        result.put("exam_title", exam.getEname());
        result.put("exam_content", exam.getDetail());
        result.put("exam_lan", exam.getLan());
        result.put("exam_in_arg", exam.getInArgs());
        result.put("exam_out_arg", exam.getOutArgs());
        result.put("exam_deadline", exam.getDeadline().toString());
        result.put("exam_update_time", exam.getUpdateDate().toString());
        result.put("exam_remark", exam.getRemarks());
        result.put("exam_tatal", String.valueOf(userDao.countUserByType(user)));//取总学生数目
        result.put("exam_commit_count", String.valueOf(resultList.size()));//取提交结果的数目
        result.put("exam_correct_count", String.valueOf(resultDao.countCorrectResult(countCorrectResult)));//取正确结果数目

        return result;
    }
}
