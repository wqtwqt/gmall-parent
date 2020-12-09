package com.atguigu.gmall.model.list.service;

public interface SearchService {

    /**
     * 上架
     * @param skuId
     */
    void upperGoods(Long skuId);

    /**
     * 下架
     * @param skuId
     */
    void lowerGoods(Long skuId);

    /**
     * 更新热点
     * @param skuId
     */
    void incrHotScore(Long skuId);
}
