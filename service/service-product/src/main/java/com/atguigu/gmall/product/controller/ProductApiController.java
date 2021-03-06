package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.ManageService;
import com.atguigu.gmall.product.service.SkuManageService;
import com.atguigu.gmall.product.service.SpuManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/product")
public class ProductApiController {

    @Autowired
    private SkuManageService skuManageService;

    @Autowired
    private ManageService manageService;

    @Autowired
    private SpuManageService spuManageService;

    //根据skuId回显sku基本信息
    @GetMapping("inner/getSkuInfo/{skuId}")
    public SkuInfo getSkuInfo(@PathVariable Long skuId){
        SkuInfo skuInfo = skuManageService.getSkuInfo(skuId);
        return skuInfo;
    }

    //根据三级分类id回显分类信息
    @GetMapping("inner/getCategoryView/{category3Id}")
    public BaseCategoryView getCategoryView(@PathVariable Long category3Id){
        BaseCategoryView baseCategoryView = manageService.getBaseCategoryViewByCategory3Id(category3Id);
        return baseCategoryView;
    }

    //根据SkuId回显sku图片信息
    @GetMapping("inner/getSkuPrice/{skuId}")
    public BigDecimal getSkuPrice(@PathVariable Long skuId){
        BigDecimal price = skuManageService.getSkuPrice(skuId);
        return price;
    }

    //根据skuId,spuId回显销售属性和销售属性值以及锁定skuId对应的销售属性值
    @GetMapping("inner/getSpuSaleAttrListCheckBySku/{skuId}/{spuId}")
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(@PathVariable Long skuId,@PathVariable Long spuId){
        List<SpuSaleAttr> spuSaleAttr = spuManageService.getSpuSaleAttr(skuId, spuId);
        return spuSaleAttr;
    }

    //根据spuId查询属性值组合
    @GetMapping("inner/getSkuValueIdsMap/{spuId}")
    public Map getSkuValueIdsMap(@PathVariable Long spuId){
        Map skuValueIdsMap = skuManageService.getSkuValueIdsMap(spuId);

        return skuValueIdsMap;
    }
}
