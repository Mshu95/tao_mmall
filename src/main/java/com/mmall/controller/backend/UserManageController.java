package com.mmall.controller.backend;/*
 *  cteate by tao on 2018/2/27.
 */

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/user/")
public class UserManageController {
    @Autowired
    private IUserService iUserService;
    @RequestMapping(value = "login.do" ,method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> loginResponse = iUserService.login(username, password);
        if (loginResponse.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER,loginResponse.getData());
        }
        return loginResponse;
    }
}
