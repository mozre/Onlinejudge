/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.exam.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.exam.entity.Exam;
import com.thinkgem.jeesite.modules.exam.service.ExamService;
import com.thinkgem.jeesite.modules.result.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 题目Controller
 *
 * @author njzhenghao
 * @version 2017-03-01
 */
@Controller
@RequestMapping(value = "${adminPath}/exam/exam")
public class ExamController extends BaseController {

    @Autowired
    private ExamService examService;

    @ModelAttribute
    public Exam get(@RequestParam(required = false) String id) {
        Exam entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = examService.get(id);
        }
        if (entity == null) {
            entity = new Exam();
        }
        return entity;
    }

    //教师列表-题目列表
    @RequestMapping(value = {"tExamList", ""})
    public String tExamList(Exam exam, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Exam> page = examService.findPage(new Page<Exam>(request, response), exam);
        model.addAttribute("page", page);
        return "modules/exam/tExamList";
    }

    //教师列表详情
    @RequestMapping(value = "tExamForm")
    public String tExamForm(Exam exam, Model model) {
        model.addAttribute("exam", exam);
        return "modules/exam/tExamForm";
    }

    //学生列表-我的题目
    @RequestMapping(value = {"sExamList", ""})
    public String sExamList(Exam exam, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Exam> page = examService.findPage(new Page<Exam>(request, response), exam);
        model.addAttribute("page", page);
        return "modules/exam/sExamList";
    }

    //学生题目详情
    @RequestMapping(value = "sExamForm")
    public String sExamForm(Exam exam, Model model) {
        model.addAttribute("exam", exam);
        return "modules/exam/sExamForm";
    }

    //跳转到做题页面
    @RequestMapping(value = "doExam")
    public String doExam(Exam exam, Model model) {
	    Result result = examService.doExam(exam);
        model.addAttribute(result);
        return "modules/result/codingPage";
    }

    @RequestMapping(value = "save")
    public String save(Exam exam, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, exam)) {
            return tExamForm(exam, model);
        }
        examService.save(exam);
        addMessage(redirectAttributes, "保存题目成功");
        return "redirect:" + Global.getAdminPath() + "/exam/exam/tExamList?repage";
    }

    @RequestMapping(value = "delete")
    public String delete(Exam exam, RedirectAttributes redirectAttributes) {
        examService.delete(exam);
        addMessage(redirectAttributes, "删除题目成功");
        return "redirect:" + Global.getAdminPath() + "/exam/exam/?repage";
    }

}