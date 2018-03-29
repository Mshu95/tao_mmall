package com.mmall.service.impl;/*
 *  cteate by tao on 2018/2/28.
 */

import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("ICategoryService")
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse getCategory(int categoryId){
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentKey(categoryId);
        if (CollectionUtils.isEmpty(categoryList)){
            return ServerResponse.createByErrorMessage("未找到该品类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    public ServerResponse getCategoryAndDeepChildrenCategory( int categoryId){
        Set<Category> categorySet =Sets.newHashSet();
        this.findChildrenCategory(categoryId, categorySet);
        List<Integer> categoryList = new ArrayList<>();
        for (Category categoryItem : categorySet) {
            categoryList.add(categoryItem.getId());
        }
        return ServerResponse.createBySuccess(categoryList);
    }
    //递归算法
    public Set<Category> findChildrenCategory(int categroyId,Set<Category> categorySet){
        Category category = categoryMapper.selectByPrimaryKey(categroyId);
        if (category != null) {
            categorySet.add(category);
        }
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentKey(categroyId);
        //递归算法需要一个退出条件
        for (Category categoryItem : categoryList) {
            findChildrenCategory(categoryItem.getId(), categorySet);
        }
        return categorySet;
    }
}
