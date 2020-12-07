package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface ManageService {

    /**
     * 查询一级分类
     * @return
     */
    List<BaseCategory1> getCategory1();

    /**|
     * 查询二级分类
     * @return
     */
    List<BaseCategory2> getCategory2(Long category1Id);

    /**
     * 查询三级分类
     * @return
     */
    List<BaseCategory3> getCategory3(Long category2Id);

    /**
     * 获取平台属性数据
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return
     */
    List<BaseAttrInfo> getBaseAttrInfo(Long category1Id,Long category2Id,Long category3Id);

    /**
     * 添加属性
     * @param baseAttrInfo
     */
    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    /**
     * 根据属性Id获取属性值
     * @param attrId
     * @return
     */
    List<BaseAttrValue> getBaseAttrValue(Long attrId);

    /**
     * 根据id获取属性对象
     * @param attrId
     * @return
     */
    BaseAttrInfo getAttrInfo(Long attrId);

    /**
     * 根据三级分类id获取全部类信息
     * @param category3Id
     * @return
     */
    BaseCategoryView getBaseCategoryViewByCategory3Id(Long category3Id);
}
