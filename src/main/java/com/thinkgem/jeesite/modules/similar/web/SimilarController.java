/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.similar.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.common.utils.Encodes;
import com.thinkgem.jeesite.modules.exam.entity.Exam;
import com.thinkgem.jeesite.modules.judge.SimilarCheck;
import com.thinkgem.jeesite.modules.result.entity.Result;
import com.thinkgem.jeesite.modules.result.service.ResultService;
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
import com.thinkgem.jeesite.modules.similar.entity.Similar;
import com.thinkgem.jeesite.modules.similar.service.SimilarService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查重Controller
 *
 * @author nzjhenghao
 * @version 2017-05-04
 */
@Controller
@RequestMapping(value = "${adminPath}/similar/similar")
public class SimilarController extends BaseController {

    @Autowired
    private SimilarService similarService;

    @Autowired
    private ResultService resultService;

    @ModelAttribute
    public Similar get(@RequestParam(required = false) String id) {
        Similar entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = similarService.get(id);
        }
        if (entity == null) {
            entity = new Similar();
        }
        return entity;
    }

    @RequestMapping(value = {"list", ""})
    public String list(Exam exam, Model model) {
        //取similar查询条件
        Similar similar = new Similar();
        similar.setEid(exam.getId());

        //取result列表
        Result result = new Result();
        result.setDelFlag("0");
        result.setEid(exam.getId());
        List<Result> results = resultService.findListID(result);

        //循环组装List<Map<String,String>>结果
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
        for (int outCount = 0; outCount < results.size() - 1; outCount++) {
            String outUid = results.get(outCount).getUid();
            String outCode = Encodes.unescapeHtml(results.get(outCount).getCode());
            for (int inCount = outCount+1; inCount < results.size(); inCount++) {
                String inUid = results.get(inCount).getUid();
                String inCode = Encodes.unescapeHtml(results.get(inCount).getCode());
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid1", inUid);
                map.put("uid2", outUid);
                map.put("code1", inCode);
                map.put("code2", outCode);
                mapList.add(map);
            }
        }

        //查重并保存结果
        SimilarCheck similarCheck = new SimilarCheck(mapList);
        List<Map<String, String>> checkList = similarCheck.startSimilar();
        similarService.delete(similar);
        for (Map<String, String> map : checkList) {
            Similar saveSimilar = new Similar();
            saveSimilar.setEid(exam.getId());
            saveSimilar.setUid1(map.get("uid1"));
            saveSimilar.setUid2(map.get("uid2"));
            saveSimilar.setSimilarRate(map.get("similarRate").substring(0,4));
            similarService.save(saveSimilar);
        }

        List<Similar> list = similarService.findList(similar);
        Page<Similar> page = similarService.findPage(new Page<Similar>(1, (list.size() / 10 + 1) * 10), similar);
        model.addAttribute("page", page);
        return "modules/similar/similarList";
    }

//	@RequestMapping(value = "form")
//	public String form(Similar similar, Model model) {
//		model.addAttribute("similar", similar);
//		return "modules/similar/similarForm";
//	}

    @RequestMapping(value = "save")
    public String save(Similar similar, Model model, RedirectAttributes redirectAttributes) {
//		if (!beanValidator(model, similar)){
//			return form(similar, model);
//		}
        similarService.save(similar);
        addMessage(redirectAttributes, "保存查重成功");
        return "redirect:" + Global.getAdminPath() + "/similar/similar/?repage";
    }

    @RequestMapping(value = "delete")
    public String delete(Similar similar, RedirectAttributes redirectAttributes) {
        similarService.delete(similar);
        addMessage(redirectAttributes, "删除查重成功");
        return "redirect:" + Global.getAdminPath() + "/similar/similar/?repage";
    }

}