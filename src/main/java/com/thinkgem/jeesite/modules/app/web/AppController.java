/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.web;

import com.alibaba.druid.support.json.JSONUtils;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.modules.app.service.AppService;
import com.thinkgem.jeesite.modules.exam.entity.Exam;
import com.thinkgem.jeesite.modules.exam.service.ExamService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.activiti.engine.impl.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.*;

/**
 * 题目Controller
 *
 * @author njzhenghao
 * @version 2017-05-08
 */
@RestController
@RequestMapping(value = "/app")
public class AppController {

    @Autowired
    ExamService examService;

    /**
     * 处理登录请求
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        //声明返回结果Map
        Map<String, String> result = new HashMap<String, String>();

        //从request获取相关信息
        String no = request.getParameter("username");//登录账户名(不是姓名)
        String password = request.getParameter("password");//密码

        //取得当前user
        User user = UserUtils.getByLoginName(no);

        if (user.getUserType().equals("2")) {        //验证是否为老师身份
            if (SystemService.validatePassword(password, user.getPassword())) {//验证账号密码
                result.put("resultCode", "200");
                result.put("username", user.getName());//返回教师姓名
                result.put("token", no);//token为教师no
            } else {
                result.put("resultCode", "400");
            }
        } else {
            result.put("resultCode", "300");
        }

        return JSONUtils.toJSONString(result);
    }

    /**
     * 保存或者更新Exam
     *
     * @param request
     * @param response
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/exam", method = RequestMethod.POST)
    public String updateOrNewExam(HttpServletRequest request, HttpServletResponse response) {
        //声明返回结果Map
        Map<String, String> result = new HashMap<String, String>();

        //从request获取相关信息
        String no = request.getParameter("token");//登录账户名(不是姓名)

        Exam exam = null;
        try {

            String strData = request.getParameter("data");
            JSONObject object = (JSONObject) JSONUtils.parse(strData);
            strData = object.getString("data");

            //取app的exam对象,强转为map
            Map<String, String> data = (Map<String, String>) JSONUtils.parse(strData);




            //将app的exam对象转为系统中的exam对象
            exam = new AppService().convertToExam(data);


            //获取当前用户
            User user = UserUtils.getByLoginName(no);

            //保存题目
            examService.saveWithUser(exam, user);

            //写入返回值200
            result.put("resultCode", "200");
        } catch (ParseException e) {
            e.printStackTrace();
            result.put("resultCode", "300");
        }

        return JSONUtils.toJSONString(result);

    }

    @RequestMapping(value = "/exam", method = RequestMethod.GET)
    public String getExam(HttpServletRequest request, HttpServletResponse response) {
        //声明结果List
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

        //声明返回结果Map
        Map<String, Object> result = new HashMap<String, Object>();

        //从request获取相关信息
        String no = request.getParameter("token");
        String action = request.getParameter("action");

        //获取当前用户
        User user = UserUtils.getByLoginName(no);

        //声明一个题目,用于查询题目List
        Exam exam = new Exam();
        exam.setDelFlag("0");//设置删除标识符
        List<Exam> examList = examService.findList(exam);//查询所有题目List

        //声明APPService,后面调用
        AppService appService = new AppService();

        if (action.equals("1")) {//请求40个题目
            if (examList.size() < 40) {
                for (int count = 0; count < examList.size(); count++) {
                    resultList.add(appService.examConvertTo(examList.get(count), user.getId()));
                }
            } else {
                for (int count = 0; count < 40; count++) {
                    resultList.add(appService.examConvertTo(examList.get(count), user.getId()));
                }
            }
        } else if (action.equals("2")) {//请求所有题目
            for (int count = 0; count < examList.size(); count++) {
                resultList.add(appService.examConvertTo(examList.get(count), user.getId()));
            }
        }

        if (resultList.size() != 0 || resultList != null) {
            result.put("resultCode", "200");//设置返回数值
            result.put("data", resultList);//结果
        }

        return JSONUtils.toJSONString(result);
    }
}