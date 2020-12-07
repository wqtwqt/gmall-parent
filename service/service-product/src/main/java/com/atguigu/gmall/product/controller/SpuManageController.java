package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.product.service.ManageService;
import com.atguigu.gmall.product.service.SpuManageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "spu分页列表")
@RestController
@RequestMapping("admin/product")
public class SpuManageController {

    @Autowired
    private SpuManageService spuManageService;

    //spu分页显示
    @GetMapping("{page}/{limit}")
    public Result getSpuInfoPage(@PathVariable Long page,
                                 @PathVariable Long limit,
                                 SpuInfo spuInfo){
        Page<SpuInfo> spuInfoPage = new Page<>(page,limit);
        IPage<SpuInfo> spuInfoPageList = spuManageService.getSpuInfoPageList(spuInfoPage, spuInfo);
        return Result.ok(spuInfoPageList);
    }
    //添加spu
    @PostMapping("saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo spuInfo){
        spuManageService.saveSpuInfo(spuInfo);
        return Result.ok();
    }

    //获取销售属性集合
    @GetMapping("baseSaleAttrList")
    public Result getBaseSaleAttrList(){
        return Result.ok(spuManageService.getBaseSaleAttrList());
    }
}
