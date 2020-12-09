package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseTrademark;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BaseTrademarkService extends IService<BaseTrademark> {
    /**
     * 获取销售属性
     * @param baseTrademarkPage
     * @return
     */
    IPage<BaseTrademark> getBaseTrademark(Page<BaseTrademark> baseTrademarkPage);

    /**
     * 根据Id获取品牌信息
     * @param tmId
     * @return
     */
    BaseTrademark getBaseTrademarkById(Long tmId);
}
