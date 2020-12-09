package com.atguigu.gmall.model.list.service.impl;

import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchAttr;
import com.atguigu.gmall.model.list.service.GoodsRepository;
import com.atguigu.gmall.model.list.service.SearchService;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.client.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void upperGoods(Long skuId) {
        Goods goods = new Goods();
        CompletableFuture<Void> baseAttrInfoListCompletableFuture = CompletableFuture.runAsync(() -> {
            List<BaseAttrInfo> baseAttrInfoList = productFeignClient.getAttrList(skuId);
            ArrayList<SearchAttr> searchAttrs = new ArrayList<>();
            baseAttrInfoList.stream().forEach(baseAttrInfo -> {
                SearchAttr searchAttr = new SearchAttr();
                searchAttr.setAttrId(baseAttrInfo.getId());
                searchAttr.setAttrName(baseAttrInfo.getAttrName());
                searchAttr.setAttrValue(baseAttrInfo.getAttrValueList().get(0).getValueName());
                searchAttrs.add(searchAttr);
            });
            goods.setAttrs(searchAttrs);
        }, threadPoolExecutor);

        CompletableFuture<SkuInfo> skuInfoCompletableFuture = CompletableFuture.supplyAsync(() -> {
            SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
            goods.setId(skuId);
            goods.setTitle(skuInfo.getSkuName());
            goods.setDefaultImg(skuInfo.getSkuDefaultImg());
            goods.setCreateTime(new Date());
            goods.setPrice(skuInfo.getPrice().doubleValue());
            return skuInfo;
        }, threadPoolExecutor);

        CompletableFuture<Void> categoryViewCompletableFuture = skuInfoCompletableFuture.thenAcceptAsync(skuInfo -> {
            BaseCategoryView categoryView = productFeignClient.getCategoryView(skuInfo.getCategory3Id());
            goods.setCategory1Id(categoryView.getCategory1Id());
            goods.setCategory1Name(categoryView.getCategory1Name());
            goods.setCategory2Id(categoryView.getCategory2Id());
            goods.setCategory2Name(categoryView.getCategory2Name());
            goods.setCategory3Id(categoryView.getCategory3Id());
            goods.setCategory3Name(categoryView.getCategory3Name());
        }, threadPoolExecutor);

        CompletableFuture<Void> trademarkCompletableFuture = skuInfoCompletableFuture.thenAcceptAsync(skuInfo -> {
            BaseTrademark trademark = productFeignClient.getTrademark(skuInfo.getTmId());
            goods.setTmId(trademark.getId());
            goods.setTmName(trademark.getTmName());
            goods.setTmLogoUrl(trademark.getLogoUrl());
        }, threadPoolExecutor);

        CompletableFuture.allOf(
                baseAttrInfoListCompletableFuture,
                skuInfoCompletableFuture,
                categoryViewCompletableFuture,
                trademarkCompletableFuture
        ).join();
        goodsRepository.save(goods);
    }

    @Override
    public void lowerGoods(Long skuId) {
        goodsRepository.deleteById(skuId);
    }

    @Override
    public void incrHotScore(Long skuId) {
        String hotKey = "hotScore";
        Double incrementScore = redisTemplate.opsForZSet().incrementScore(hotKey, "skuId:" + skuId, 1);
        if (incrementScore % 10 == 0){
            Optional<Goods> optional = goodsRepository.findById(skuId);
            Goods goods = optional.get();
            goods.setHotScore(Math.round(incrementScore));
            goodsRepository.save(goods);
        }
    }
}
