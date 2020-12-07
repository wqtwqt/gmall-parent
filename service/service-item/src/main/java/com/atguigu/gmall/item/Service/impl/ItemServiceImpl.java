package com.atguigu.gmall.item.Service.impl;

import com.alibaba.fastjson.JSON;

import com.atguigu.gmall.item.Service.ItemService;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.client.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Override
    public Map<String, Object> getSkuInfoById(Long skuId) {
        Map<String,Object> result = new HashMap<>();
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        BaseCategoryView categoryView = productFeignClient.getCategoryView(skuId);
        BigDecimal skuPrice = productFeignClient.getSkuPrice(skuId);
        Map skuValueIdsMap = productFeignClient.getSkuValueIdsMap(skuInfo.getSpuId());
        System.out.println(skuValueIdsMap);
        String s = JSON.toJSONString(skuValueIdsMap);
        List<SpuSaleAttr> spuSaleAttrListCheckBySku = productFeignClient.getSpuSaleAttrListCheckBySku(skuInfo.getId(), skuInfo.getSpuId());
        result.put("categoryView",categoryView);
        result.put("price",skuPrice);
        result.put("valuesSkuJson",s);
        result.put("spuSaleAttrList",spuSaleAttrListCheckBySku);
        result.put("skuInfo",skuInfo);

        return result;

    }
}
