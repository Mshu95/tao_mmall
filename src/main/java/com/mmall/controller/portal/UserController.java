package com.mmall.controller.portal;/*
 *  cteate by tao on 2018/1/25.
 */

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {
    @Autowired
    @Qualifier("iUserService")
    private IUserService iUserService;
    //登陆
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
       // System.out.println("login.do+huangtao");
        ServerResponse<User> response = iUserService.login(username,password);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    //登出
    @RequestMapping(value = "logout.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }
    //注册
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        System.out.println(user);
        ServerResponse<String> response = iUserService.register(user);
        return response;
    }

    //检查用户名是否有效
    //check_valid.do?str=admin&type=username就是检查用户名。
    @RequestMapping(value = "check_valid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type) {
        return iUserService.checkValid(str, type);
    }

    //获取用户信息
    @RequestMapping(value = "get_user.do" , method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }else {
            return ServerResponse.createByErrorMessage("用户未登录！");
        }

    }
    //忘记密码
    @RequestMapping(value = "forgetGetQuestion.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        ServerResponse<String> checkResponse = checkValid(username, Const.USERNAME);
        if (checkResponse.isSuccess()) {
            return iUserService.forgetGetQuestion(username);
        }else {
            return ServerResponse.createByErrorMessage("用户名不存在！");
        }
    }
    //提交问题答案
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.forgetCheckAnswer(username, question, answer);
    }
    //忘记密码的重设密码 forget_reset_password.do
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String token) {
        return iUserService.forgetRestPassword(username, passwordNew, token);
    }
    //登录中状态重置密码
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return iUserService.resetPassword(passwordOld, passwordNew, user);

    }
    //登录状态更新个人信息
    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> updateUserInfo(String email, String phone, String answer,HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return iUserService.updateUserInfo(email, phone, answer, user);
    }
    //get_information.do 获取当前登录用户的详细信息，并强制登录
    @RequestMapping(value = "get_information.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> get_information(HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,无法获取当前用户信息,status=10,强制登录");
        }
        return iUserService.get_information(user.getId());
    }
}
