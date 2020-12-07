package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import com.atguigu.gmall.product.service.SkuManageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/product")
public class SkuManageController {

    @Autowired
    private SkuManageService skuManageService;

    //根据spuId获取spuImage
    @GetMapping("spuImageList/{spuId}")
    public Result getSpuImageList(@PathVariable Long spuId){
        List<SpuImage> spuImageList = skuManageService.getSpuImageList(spuId);
        //回显到前端添加Sku页面中
        return Result.ok(spuImageList);
    }

    //根据spuId获取销售属性
    @GetMapping("spuSaleAttrList/{spuId}")
    public Result getSpuSaleAttrList(@PathVariable Long spuId){
        List<SpuSaleAttr> spuSaleAttrList = skuManageService.getSpuSaleAttrList(spuId);
        //回显到添加Sku页面中
        return Result.ok(spuSaleAttrList);
    }

    //保存SkuInfo
    @PostMapping("saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo){
        skuManageService.saveSkuInfo(skuInfo);
        return Result.ok();
    }
    //获取带分页的skuInfo
    @GetMapping("list/{page}/{limit}")
    public Result getSkuInfoList(@PathVariable Long page,@PathVariable Long limit){
        Page<SkuInfo> skuInfoPage = new Page<>(page, limit);
        IPage<SkuInfo> skuInfoIPage= skuManageService.getSkuInfoList(skuInfoPage);
        return Result.ok(skuInfoIPage);
    }
    //修改skuInfo中的is_sale=1,表示上架
    @GetMapping("onSale/{skuId}")
    public Result onSale(@PathVariable Long skuId){
        skuManageService.onSale(skuId);
        return Result.ok();
    }

    //修改skuInfo中的is_sale=0,表示下架
    @GetMapping("cancelSale/{skuId}")
    public Result cancelSale(@PathVariable Long skuId){
        skuManageService.cancelSale(skuId);
        return Result.ok();
    }


}
