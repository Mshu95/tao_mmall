package com.mmall.service;/*
 *  cteate by tao on 2018/2/28.
 */

import com.mmall.common.ServerResponse;

public interface ICategoryService {
    public ServerResponse getCategoryAndDeepChildrenCategory(int categoryId);

    public ServerResponse getCategory(int categoryId);
}
