package com.atguigu.gmall.product.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.common.cache.GmallCache;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.mapper.*;
import com.atguigu.gmall.product.service.ManageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ManageServiceImpl implements ManageService {

    @Autowired
    private BaseCategory1Mapper baseCategory1Mapper;

    @Autowired
    private BaseCategory2Mapper baseCategory2Mapper;

    @Autowired
    private BaseCategory3Mapper baseCategory3Mapper;

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;

    @Autowired
    private BaseCategoryViewMapper baseCategoryViewMapper;


    @Override
    public List<BaseCategory1> getCategory1() {

        return baseCategory1Mapper.selectList(null);
    }

    @Override
    public List<BaseCategory2> getCategory2(Long category1Id) {
        return baseCategory2Mapper.selectList(new QueryWrapper<BaseCategory2>().eq("category1_id",category1Id));
    }

    @Override
    public List<BaseCategory3> getCategory3(Long category2Id) {
        new QueryWrapper<BaseCategory2>().eq("category1_id", category2Id);

        return baseCategory3Mapper.selectList(new QueryWrapper<BaseCategory3>().eq("category2_id", category2Id));
    }

    @Override
    public List<BaseAttrInfo> getBaseAttrInfo(@Param("category1Id") Long category1Id,
                                              @Param("category2Id") Long category2Id,
                                              @Param("category3Id") Long category3Id) {
        List<BaseAttrInfo> baseAttrInfoList = baseAttrInfoMapper.selectBaseAttrInfoList(category1Id, category2Id, category3Id);
        return baseAttrInfoList;
    }

    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        if (baseAttrInfo.getId() != null){
            baseAttrInfoMapper.updateById(baseAttrInfo);
        }else {
            baseAttrInfoMapper.insert(baseAttrInfo);
        }

        QueryWrapper<BaseAttrValue> baseAttrValueQueryWrapper = new QueryWrapper<>();
        baseAttrValueQueryWrapper.eq("attr_id",baseAttrInfo.getId());
        baseAttrValueMapper.delete(baseAttrValueQueryWrapper);


        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        if (attrValueList != null && attrValueList.size() > 0) {
            for (BaseAttrValue baseAttrValue : attrValueList) {
                baseAttrValue.setAttrId(baseAttrInfo.getId());
                baseAttrValueMapper.insert(baseAttrValue);
            }
        }
    }

    @Override
    public List<BaseAttrValue> getBaseAttrValue(Long attrId) {
        QueryWrapper<BaseAttrValue> baseAttrValueQueryWrapper = new QueryWrapper<>();
        baseAttrValueQueryWrapper.eq("attr_id",attrId);
        List<BaseAttrValue> baseAttrValueList = baseAttrValueMapper.selectList(baseAttrValueQueryWrapper);

        return baseAttrValueList;
    }

    @Override
    public BaseAttrInfo getAttrInfo(Long attrId) {
        BaseAttrInfo baseAttrInfo = baseAttrInfoMapper.selectById(attrId);
        if (baseAttrInfo != null) {
            baseAttrInfo.setAttrValueList(getBaseAttrValue(attrId));
        }
        return baseAttrInfo;
    }

    @Override
    @GmallCache(prefix = "baseCategoryViewByCategory3Id")
    public BaseCategoryView getBaseCategoryViewByCategory3Id(Long category3Id) {
        return getBaseCategoryViewDB(category3Id);
    }

    @Override
    public List<JSONObject> getBaseCategoryList() {
        // 声明几个json 集合
        ArrayList<JSONObject> list = new ArrayList<>();
        // 声明获取所有分类数据集合
        List<BaseCategoryView> baseCategoryViewList = baseCategoryViewMapper.selectList(null);
        // 循环上面的集合并安一级分类Id 进行分组
        Map<Long, List<BaseCategoryView>> category1Map = baseCategoryViewList.stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory1Id));
        int index = 1;
        // 获取一级分类下所有数据
        for (Map.Entry<Long, List<BaseCategoryView>> category1 : category1Map.entrySet()) {
            // 获取一级分类Id
            Long category1Id = category1.getKey();
            // 获取一级分类下面的所有集合
            List<BaseCategoryView> category2List = category1.getValue();
            //声明一级分类对象
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("index",index);
            //一级分类id
            jsonObject1.put("categoryId",category1Id);
            // 一级分类名称
            jsonObject1.put("categoryName",category2List.get(0).getCategory1Name());
            // 变量迭代
            index++;

            // 循环获取二级分类数据
            Map<Long, List<BaseCategoryView>> category2Map = category2List.stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory2Id));
            // 声明二级分类对象集合
            List<JSONObject> category2Child = new ArrayList<>();
            // 循环遍历
            for (Map.Entry<Long, List<BaseCategoryView>> category2 : category2Map.entrySet()) {
                // 获取二级分类Id
                Long category2Id = category2.getKey();
                // 获取二级分类下的所有集合
                List<BaseCategoryView> category3List = category2.getValue();
                // 声明二级分类对象
                JSONObject jsonObject2 = new JSONObject();
                //二级分类id
                jsonObject2.put("categoryId",category2Id);
                //二级分类名称
                jsonObject2.put("categoryName",category3List.get(0).getCategory2Name());
                // 添加到二级分类集合
                category2Child.add(jsonObject2);

                //声明三级分类集合
                List<JSONObject> category3Child = new ArrayList<>();
                // 循环三级分类数据
                category3List.stream().forEach(category3View -> {
                    JSONObject jsonObject3 = new JSONObject();
                    jsonObject3.put("categoryId",category3View .getCategory3Id());
                    jsonObject3.put("categoryName",category3View.getCategory3Name());
                    category3Child.add(jsonObject3);
                });
                // 将三级数据放入二级里面
                jsonObject2.put("categoryChild",category3Child);
            }
            // 将二级数据放入一级里面
            jsonObject1.put("categoryChild",category2Child);
            //将全部分类数据添加到集合中
            list.add(jsonObject1);
        }

        return list;
    }

    @Override
    public List<BaseAttrInfo> getBaseAttrList(Long skuId) {
       List<BaseAttrInfo> list = baseAttrInfoMapper.selectBaseAttrListById(skuId);
       return list;
    }

    private BaseCategoryView getBaseCategoryViewDB(Long category3Id) {
        BaseCategoryView baseCategoryView = baseCategoryViewMapper.selectById(category3Id);

        return baseCategoryView;
    }


}
