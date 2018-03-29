package com.mmall.controller.backend;/*
 *  cteate by tao on 2018/3/5.
 */

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;
//.新增OR更新产品
    @RequestMapping(value = "save.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse saveProduct(Product product, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录");
        }
        if (iUserService.checkAdmin(user).isSuccess()) {
            return iProductService.saveOrUpdateProduct(product);
        }else {
            return ServerResponse.createByErrorMessage("没有权限");
        }
    }
    //.产品上下架
    @RequestMapping(value = "set_sale_status.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> setSaleStatus(Integer status,Integer productId,HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录");
        }
        if (iUserService.checkAdmin(user).isSuccess()) {
            return iProductService.setSaleStatus(status,productId);
        }else {
            return ServerResponse.createByErrorMessage("没有权限");
        }
    }
    //.产品详情
    @RequestMapping(value = "detail.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Product> getProductDetail(Integer productId,HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录");
        }
        if (iUserService.checkAdmin(user).isSuccess()) {
            return iProductService.getProductDetail(productId);
        }else {
            return ServerResponse.createByErrorMessage("没有权限");
        }
    }

    @RequestMapping(value = "list.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Product> getList(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录");
        }
        if (iUserService.checkAdmin(user).isSuccess()) {
            return iProductService.getProductList(pageNum, pageSize);
        }else {
            return ServerResponse.createByErrorMessage("没有权限");
        }
    }
    @RequestMapping(value = "search.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse productSearch(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,  String productName, Integer productId,HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录");
        }
        if (iUserService.checkAdmin(user).isSuccess()) {
            return iProductService.productSearch(pageNum,pageSize,productName,productId);
        }else {
            return ServerResponse.createByErrorMessage("没有权限");
        }
    }

    //文件上传
    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(MultipartFile file, HttpServletRequest request,HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录");
        }
        if (iUserService.checkAdmin(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetName =iFileService.upload(path,file);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/")+targetName;
            Map reslutMap = new HashMap();
            reslutMap.put("uri", targetName);
            reslutMap.put("url", url);
            return ServerResponse.createBySuccess(reslutMap);
        }else {
            return ServerResponse.createByErrorMessage("没有权限");
        }
    }
    @RequestMapping("richtext_img_upload")
    @ResponseBody
    public ServerResponse richtextImgUpload(MultipartFile file, HttpServletRequest request,HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录");
        }
        if (iUserService.checkAdmin(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetName =iFileService.upload(path,file);
            System.out.println(targetName);
            Map reslutMap = new HashMap();
            if (StringUtils.isBlank(targetName)) {
                reslutMap.put("success", false);
                reslutMap.put("msg", "error message");
                reslutMap.put("file_path", "[real file path]");
            }else {
                String url = PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/") + targetName;
                reslutMap.put("success", true);
                reslutMap.put("msg", "上传成功");
                reslutMap.put("file_path", url);
            }
            return ServerResponse.createBySuccess(reslutMap);
        }else {
            return ServerResponse.createByErrorMessage("没有权限");
        }
    }
}
