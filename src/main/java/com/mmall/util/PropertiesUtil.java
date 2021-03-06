package com.mmall.util;/*
 *  cteate by tao on 2018/3/5.
 */

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    private static Properties prop;

    static {
        prop = new Properties();
        String fileName = "mmall.properties";
        try {
            prop.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName), "UTF-8"));
        } catch (Exception e) {
            logger.error("配置文件加载错误", e);
        }
    }
    public static String getProperty(String key) {
        String value = prop.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value.trim();
    }
    public static String getProperty(String key,String defultValue) {
        String value = prop.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            return defultValue;
        }
        return value.trim();
    }
}