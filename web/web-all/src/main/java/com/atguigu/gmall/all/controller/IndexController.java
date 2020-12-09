package com.atguigu.gmall.all.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.client.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @Autowired
    private ProductFeignClient productFeignClient;

    //分类级别
    @RequestMapping({"/","index.html"})
    public String index(Model model){
        Result baseCategoryList = productFeignClient.getBaseCategoryList();
        model.addAttribute("list",baseCategoryList.getData());
        return "index/index";
    }
}
