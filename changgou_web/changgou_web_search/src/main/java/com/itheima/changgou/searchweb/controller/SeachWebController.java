package com.itheima.changgou.searchweb.controller;

import com.itheima.changgou.entity.Result;
import com.itheima.changgou.search.feign.SearchEsSearchClient;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/10 11:25
 */
@Controller
public class SeachWebController {

    @Autowired(required = false)
    private SearchEsSearchClient searchEsSearchClient;

    @RequestMapping("/search")
    public ModelAndView search(@RequestParam(required = false) Map<String, String> paramMap, HttpServletRequest httpServletRequest) {

        // 获取当前请求的资源地址，用于拼装前端筛选按钮的地址
        String uri = getUri(paramMap, httpServletRequest);

        // 调用search微服务
        Result<Map> result = searchEsSearchClient.search(paramMap);

        // 将结果封装到ModelAndView
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("result", result);
        modelAndView.addObject("paramMap", paramMap);
        modelAndView.addObject("uri", uri);
        modelAndView.setViewName("search");
        // 返回解析
        return modelAndView;
    }

    private String getUri(@RequestParam(required = false) Map<String, String> paramMap, HttpServletRequest httpServletRequest) {
        String uri = httpServletRequest.getRequestURI();
        if (paramMap != null) {
            uri+= "?";
            for (Map.Entry<String, String> param : paramMap.entrySet()) {
                // URI不保留分类的参数
                if (param.getKey().equalsIgnoreCase("sortField") || param.getKey().equalsIgnoreCase("sortRule")) {
                    continue;
                }
                // URI不保留分页的参数
                if (param.getKey().equalsIgnoreCase("pageNum") || param.getKey().equalsIgnoreCase("pageSize")){
                    continue;
                }
                uri+=param.getKey()+"="+param.getValue()+"&";
            }
            uri = uri.substring(0, uri.length() - 1);
        }
        return uri;
    }
}
