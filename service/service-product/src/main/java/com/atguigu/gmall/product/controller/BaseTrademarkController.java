package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "销售属性")
@RestController
@RequestMapping("admin/product/baseTrademark")
public class BaseTrademarkController {

    @Autowired
    private BaseTrademarkService baseTrademarkService;

    //获取品牌属性,分页
    @GetMapping("{page}/{limit}")
    public Result getBaseTrademark(@PathVariable Long page, @PathVariable Long limit) {
        Page<BaseTrademark> baseTrademarkPage = new Page<>(page, limit);
        IPage<BaseTrademark> baseTrademarkPageList =  baseTrademarkService.getBaseTrademark(baseTrademarkPage);
        return Result.ok(baseTrademarkPageList);
    }

    //添加品牌属性
    @PostMapping("save")
    public Result save(@RequestBody BaseTrademark baseTrademark){
        baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }

    //修改品牌属性
    @PutMapping("update")
    public Result update(@RequestBody BaseTrademark baseTrademark){
        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }

    //删除品牌属性
    @DeleteMapping("remove/{id}")
    public Result delete(@PathVariable Long id){
        baseTrademarkService.removeById(id);
        return Result.ok();
    }

    //根据id获取修改的属性
    @GetMapping("get/{id}")
    public Result getById(@PathVariable Long id){
        BaseTrademark baseTrademark = baseTrademarkService.getById(id);
        return  Result.ok(baseTrademark);
    }

    //获取所有的品牌属性
    @GetMapping("getTrademarkList")
    public Result getTrademarkList(){
        List<BaseTrademark> list = baseTrademarkService.list(null);
        return Result.ok(list);
    }

}
