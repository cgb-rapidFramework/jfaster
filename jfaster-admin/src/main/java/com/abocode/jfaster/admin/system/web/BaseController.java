package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.core.common.util.StrUtils;
import com.abocode.jfaster.core.common.util.SystemJsonUtils;
import com.abocode.jfaster.core.platform.view.interactions.easyui.Autocomplete;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 基础控制器，其他控制器需集成此控制器获得initBinder自动转换的功能
 */
@Controller
@RequestMapping("/commonController")
public class BaseController {
    @Autowired
    private SystemRepository systemService;

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        System.out.printf("ssssssssssssssbindersssssssssssssssssss");
//        binder.registerCustomEditor(Date.class, new DateConvertEditor());
    }


    /**
     * 自动完成请求返回数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "getAutoList")
    public void getAutoList(HttpServletRequest request, HttpServletResponse response, Autocomplete autocomplete) {
        String trem = StrUtils.getEncodePra(request.getParameter("trem"));// 重新解析参数
        autocomplete.setTrem(trem);
        List autoList = systemService.findAutoList(autocomplete);
        String labelFields = autocomplete.getLabelField();
        String[] fieldArr = labelFields.split(",");
        String valueField = autocomplete.getValueField();
        if (!StrUtils.isEmpty(valueField)) {
            String[] allFieldArr = new String[fieldArr.length + 1];
            for (int i = 0; i < fieldArr.length; i++) {
                allFieldArr[i] = fieldArr[i];
            }
            allFieldArr[fieldArr.length] = valueField;
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.setHeader("Pragma", "No-cache");
                response.setHeader("Cache-Control", "no-cache");
                response.setDateHeader("Expires", 0);
                response.getWriter().write(SystemJsonUtils.listToJson(allFieldArr, allFieldArr.length, autoList));
                response.getWriter().flush();
                response.getWriter().close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 通用列表页面跳转
     */
    @Deprecated
    @RequestMapping(params = "listTurn")
    public ModelAndView listTurn(HttpServletRequest request) {
        String turn = request.getParameter("turn");// 跳转的目标页面
        return new ModelAndView(turn);
    }

    /***
     * 导入文件
     * @return
     */
    @Deprecated
    @RequestMapping(params = "importdata")
    public ModelAndView importdata() {
        return new ModelAndView("system/upload");
    }
}
