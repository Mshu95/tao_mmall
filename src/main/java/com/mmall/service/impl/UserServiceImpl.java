package com.mmall.service.impl;/*
 *  cteate by tao on 2018/1/25.
 */

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    //登录
    @Override
    public ServerResponse<User> login(String username, String password) {
        //验证姓名是否正确
        if (this.checkValid(username, Const.USERNAME).getStatus() != 0) {
            return ServerResponse.createByErrorMessage("姓名不存在！请检查姓名！");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectByLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误！");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登陆成功", user);
    }
    //注册
    public ServerResponse<String> register(User user) {

        if (this.checkValid(user.getUsername(), Const.USERNAME).getStatus() == 0) {
            return ServerResponse.createByErrorMessage("姓名存在！请检查姓名！");
        }
        if (this.checkValid(user.getEmail(),Const.EMAIL).getStatus() == 0) {
            return ServerResponse.createByErrorMessage("email存在！请检查email！");
        }
        user.setRole(Const.Role.cous_role);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        } else {
            return ServerResponse.createBySuccess("注册成功！");
        }

    }
    //校验用户名和邮箱
    public ServerResponse<String> checkValid(String str, String type) {
        if (!StringUtils.isNotEmpty(type)) {
            return ServerResponse.createByErrorMessage("校验失败");
        }
        if (Const.USERNAME.equals(type)) {
            Integer resultCount = userMapper.checkUsername(str);
            if (resultCount == 0) {
                return ServerResponse.createByErrorMessage("姓名不存在！请检查姓名！");
            }
        }
        if (Const.EMAIL.equals(type)) {
            Integer resultCount = userMapper.checkEmail(str);
            if (resultCount == 0) {
                return ServerResponse.createByErrorMessage("email不存在！请检查email！");
            }
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }
    //忘记密码
    public ServerResponse<String> forgetGetQuestion(String username){
        String question = userMapper.selectQuestionByUsername(username);
        return ServerResponse.createBySuccess(question);
    }

    @Override
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        
        int resultCount = userMapper.selectAnswer(username,question,answer);
        if (resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            //放入缓存
            TokenCache.setCache(TokenCache.Token_Profix+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("答案错误");
    }

    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String token){
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token为空");
        }
        if(!this.checkValid(username, Const.USERNAME).isSuccess()){
            return ServerResponse.createByErrorMessage("用户名错误");
        }
        String forgetToken = TokenCache.getCache(TokenCache.Token_Profix + username);
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("token无效或者过期");
        }
        if (!forgetToken.equals(token)) {
            return ServerResponse.createByErrorMessage("token不匹配");
        }else {
            int resultCount = userMapper.updatePasswordByusername(username, MD5Util.MD5EncodeUtf8(passwordNew));
            if (resultCount > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }else {
                return ServerResponse.createByErrorMessage("修改密码操作失败");
            }
        }
    }
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user){
        if (user == null) {
            return ServerResponse.createByErrorMessage("请登陆");
        }
        int resultCount = userMapper.selectPassword(MD5Util.MD5EncodeUtf8(passwordOld));
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        if(userMapper.updatePassword(user.getId(), MD5Util.MD5EncodeUtf8(passwordNew))>0){
            return ServerResponse.createBySuccessMessage("密码修改成功");
        }else {
            return ServerResponse.createByErrorMessage("密码修改失败");
        }
    }
    public ServerResponse<String> updateUserInfo(String email, String phone, String answer,User user){
        if (user == null) {
            return ServerResponse.createByErrorMessage("未登陆");
        }
        if (!this.checkValid(user.getUsername(), Const.CURRENT_USER).isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在，请重新登陆");
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setAnswer(answer);
        updateUser.setEmail(email);
        updateUser.setPhone(phone);
        int resultCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("更新个人信息成功");
        } else {
            return ServerResponse.createByErrorMessage("更新个人信息失败");
        }
    }
    public ServerResponse<User> get_information(int id){
        User user = userMapper.selectByPrimaryKey(id);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,无法获取当前用户信息,status=10,强制登录");

    }

    public ServerResponse<String> checkAdmin(User user) {
        if (user.getRole() == Const.Role.admin_role) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("没有权限");
    }
}
