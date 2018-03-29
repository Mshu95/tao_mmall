package com.mmall.service.impl;/*
 *  cteate by tao on 2018/3/5.
 */

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("iproductService")
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;
    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product!= null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImagesArray = product.getSubImages().split(",");
                if (subImagesArray.length > 0) {
                    product.setMainImage(subImagesArray[0]);
                }
            }
            if (product.getId() != null) {
                int resultCount = productMapper.updateByPrimaryKeySelective(product);
                if (resultCount > 0) {
                    return ServerResponse.createBySuccessMessage("更新产品成功");
                }
                return ServerResponse.createByErrorMessage("更新产品失败");
            }else {
                int resultCount = productMapper.insertSelective(product);
                if (resultCount > 0) {
                    return ServerResponse.createBySuccessMessage("添加产品成功");
                }
                return ServerResponse.createByErrorMessage("添加产品失败");
            }
        }
        return ServerResponse.createByErrorMessage("产品参数错误！");
    }

    public ServerResponse<String> setSaleStatus(Integer status,Integer productId) {
        if (status == null||productId==null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int resultCount = productMapper.updateByPrimaryKeySelective(product);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("修改产品状态成功");
        }else {
            return ServerResponse.createByErrorMessage("修改产品状态失败");
        }
    }

    public ServerResponse getProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);

        if (product == null) {
            return ServerResponse.createByErrorMessage("查询失败");
        }else {
            return ServerResponse.createBySuccess(productDetailVo);
        }
    }

    public ServerResponse getProductList(Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectList();
        List<ProductListVo> productListVoList = new ArrayList<>();
        for (Product product : productList) {
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);

    }

    public ServerResponse productSearch(Integer pageNum, Integer pageSize, String productName, Integer productId) {
        if (productName == null && productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
       if(StringUtils.isNotBlank(productName)){
           productName = new StringBuilder().append("%").append(productName).append("%").toString();
       }
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.search(productId,productName);
        List<ProductListVo> productListVoList = new ArrayList<>();
        for (Product product : productList) {
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }






    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/")+product.getMainImage());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }

    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/")+product.getMainImage());
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setCreateTime(DateTimeUtil.dateToString(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToString(product.getUpdateTime()));
        return  productDetailVo;
    }
}
