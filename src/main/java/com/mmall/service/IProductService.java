package com.mmall.service;/*
 *  cteate by tao on 2018/3/5.
 */

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);
    ServerResponse<String> setSaleStatus(Integer status,Integer productId);
    ServerResponse getProductDetail(Integer productId);
    ServerResponse getProductList(Integer pageNum,Integer pageSize);
    ServerResponse productSearch(Integer pageNum, Integer pageSize, String productName, Integer productId);

}
