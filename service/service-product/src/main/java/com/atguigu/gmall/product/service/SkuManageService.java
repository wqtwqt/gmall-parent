package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SkuManageService {
    /**
     * 获取spuImage
     * @param spuId
     * @return
     */
    List<SpuImage> getSpuImageList(Long spuId);

    /**
     * 根据SpuId获取销售属性
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrList(Long spuId);

    /**
     * 保存SkuInfo
     * @param skuInfo
     */
    void saveSkuInfo(SkuInfo skuInfo);

    /**
     * 获取分页的skuInfo
     * @param skuInfoPage
     * @return
     */
    IPage<SkuInfo> getSkuInfoList(Page<SkuInfo> skuInfoPage);

    /**
     * 上架skuInfo
     * @param skuId
     */
    void onSale(Long skuId);

    /**
     * 下架skuInfo
     * @param skuId
     */
    void cancelSale(Long skuId);

    /**
     * 根据skuId获取sku基本信息
     * @param skuId
     * @return
     */
    SkuInfo getSkuInfo(Long skuId);

    /**
     * 根据skuId获取对应的图片信息
     * @param skuId
     * @return
     */
    BigDecimal getSkuPrice(Long skuId);

    /**
     * 根据spuId获取销售属性组合
     * @param spuId
     * @return
     */
    Map getSkuValueIdsMap(Long spuId);
}
