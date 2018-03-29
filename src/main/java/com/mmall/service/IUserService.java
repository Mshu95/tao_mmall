package com.mmall.service;/*
 *  cteate by tao on 2018/1/25.
 */

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

public interface IUserService {
    public ServerResponse<User> login(String username, String password);

    public ServerResponse<String> register(User user);

    public ServerResponse<String> checkValid(String str, String type);

    public ServerResponse<String> forgetGetQuestion(String username);

    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer);

    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String token);

    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

    public ServerResponse<String> updateUserInfo(String email, String phone, String answer, User user);

    public ServerResponse<User> get_information(int id);
    public ServerResponse<String> checkAdmin(User user);

}
