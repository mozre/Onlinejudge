/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.result.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.common.utils.CookieUtils;
import com.thinkgem.jeesite.common.utils.Encodes;
import com.thinkgem.jeesite.modules.exam.entity.Exam;
import com.thinkgem.jeesite.modules.exam.service.ExamService;
import com.thinkgem.jeesite.modules.judge.JudgeC;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.result.entity.Result;
import com.thinkgem.jeesite.modules.result.service.ResultService;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 结果Controller
 *
 * @author njzhenghao
 * @version 2017-04-23
 */
@Controller
@RequestMapping(value = "${adminPath}/result/result")
public class ResultController extends BaseController {

    @Autowired
    private ResultService resultService;

    @Autowired
    private ExamService examService;

    @ModelAttribute
    public Result get(@RequestParam(required = false) String id) {
        Result entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = resultService.get(id);
        }
        if (entity == null) {
            entity = new Result();
        }
        return entity;
    }

    @RequestMapping(value = {"tResultList", ""})
    public String list(Exam exam, Model model) {
        Result result = new Result();
        result.setEid(exam.getId());
        result.setDelFlag("0");
        int size = (resultService.findList(result).size() / 10 + 1) * 10;
        Page<Result> page = resultService.findPage(new Page<Result>(1, size), result);
        model.addAttribute("page", page);
        return "modules/result/tResultList";
    }

    //跳转可以做题的结果页面--未超时
    @RequestMapping(value = "sResultForm")
    public String sResultForm(Result result, Model model) {
        model.addAttribute("result", result);
        return "modules/result/sResultForm";
    }

    //跳转不可以做题的结果页面--超时
    @RequestMapping(value = "sResultFormTimeout")
    public String sResultFormTimeout(Result result, Model model) {
        model.addAttribute("result", result);
        return "modules/result/sResultFormTimeout";
    }

    //根据id取Result对象
    @RequestMapping(value = "tGetResult")
    public String tGetResult(Result result, Model model) {
        result = resultService.get(result.getId());
        model.addAttribute("result", result);
        return "modules/result/tResultForm";
    }

    //根据eid和uid创建对应结果对象
    @RequestMapping(value = "createResult")
    public String createResult(Exam exam, Model model) {
        String eid = exam.getId();
        String uid = UserUtils.getUser().getId();
        Result result = resultService.getResult(eid, uid);
        if (result == null) {
            result = new Result();
            result.setUid(uid);
            result.setEid(eid);
            result.setCode("");
            result.setCompile("1");
            result.setTimeout("1");
            result.setAnswer("1");

            resultService.save(result);
        }

        model.addAttribute("result", result);

        Date deadline = examService.get(eid).getDeadline();
        if (deadline.before(new Date())) {
            return "modules/result/sResultFormTimeout";
        } else {
            return "modules/result/sResultForm";
        }
    }

    //做题页面
    @RequestMapping(value = "codingPage")
    public String coding(Result result, Model model) {
        model.addAttribute("result", result);
        return "modules/result/codingPage";
    }

    //判题页面
    @RequestMapping(value = "judge")
    public String judge(Result result, Model model) {
        //将题目将代码保存成文件
        String code = Encodes.unescapeHtml(result.getCode());
        String uid = result.getUid();
        String filePath = "/tmp/" + uid + ".c";
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        synchronized (file) {
            try {
                FileWriter fileWriter = new FileWriter(filePath);
                fileWriter.write(code);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        Exam exam = examService.get(result.getEid());
        Result res = new Result();
        if (exam.getLan().equals("0")) {

            Map<String, String> resMap = new JudgeC(resultService.getArgs(exam, filePath)).startJudgeC();
            res.setCompile(resMap.get("compile"));
            res.setTimeout(resMap.get("time_out"));
            res.setAnswer(resMap.get("answer"));
            res.setRemarks(resMap.get("remarks"));
            res.setUid(result.getUid());
            res.setEid(result.getEid());
            res.setCode(result.getCode());
            res.setId(result.getId());
        }

        model.addAttribute("result", res);

        return "modules/result/sResultFormSave";
    }

    @RequestMapping(value = "save")
    public String save(Result result, Model model, RedirectAttributes redirectAttributes) {
        if (result.getRemarks().length() > 254) {
            result.setRemarks(result.getRemarks().substring(0, 253));
        }
        if (result.getRemarks().equals(",")) {
            result.setRemarks(null);
        }
        if (!beanValidator(model, result)) {
            return sResultForm(result, model);
        }
        resultService.save(result);
        addMessage(redirectAttributes, "保存结果成功");
        return "redirect:" + Global.getAdminPath() + "/exam/exam/sExamList";
    }

    @RequestMapping(value = "delete")
    public String delete(Result result, RedirectAttributes redirectAttributes) {
        resultService.delete(result);
        addMessage(redirectAttributes, "删除结果成功");
        return "redirect:" + Global.getAdminPath() + "/result/result/?repage";
    }

}