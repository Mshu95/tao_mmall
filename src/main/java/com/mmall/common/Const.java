package com.mmall.common;/*
 *  cteate by tao on 2018/1/25.
 */

public class Const {
    public static final String CURRENT_USER ="current_user";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public interface Role{
        public int cous_role = 0;
        public int admin_role =1;
    }

    public interface Cart{
        public int CHECKED = 1;
        public int UNCHECKED = 0;
    }
}
