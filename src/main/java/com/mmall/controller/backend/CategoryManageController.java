package com.mmall.controller.backend;/*
 *  cteate by tao on 2018/2/28.
 */

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {
    @Autowired
    private ICategoryService iCategoryService;

    //获取品类子节点(平级)get_category
    @RequestMapping(value = "get_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getCategory(@RequestParam(value= "categoryId" ,defaultValue = "0") int categoryId, HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if (user.getRole()!=Const.Role.admin_role) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"没有权限");
        }
        return iCategoryService.getCategory(categoryId);
    }


    //获取当前分类id及递归子节点categoryId
    @RequestMapping(value = "get_deep_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session, int categoryId) {
      User user = (User)session.getAttribute(Const.CURRENT_USER);
      if (user == null) {
          return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
      }
        if (user.getRole()!=Const.Role.admin_role) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"没有权限");
        }
        return iCategoryService.getCategoryAndDeepChildrenCategory(categoryId);
    }
}
