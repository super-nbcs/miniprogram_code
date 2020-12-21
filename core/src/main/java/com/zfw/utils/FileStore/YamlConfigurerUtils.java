package com.zfw.utils.FileStore;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Properties;

/**
 * @Author:zfw
 * @Date:2019/12/11
 * @Content:
 */
public class YamlConfigurerUtils {

    public static String getStr(String key,String ymlName) {
        if (StringUtils.isBlank(ymlName)){
            ymlName="application-image";
        }
        Resource app = new ClassPathResource(String.format("%s.yml",ymlName));
        YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        yamlPropertiesFactoryBean.setResources(app);

        Properties properties = yamlPropertiesFactoryBean.getObject();
        return properties.getProperty(key);
    }
    public static String getStr(String key){
        return getStr(key,null);
    }
    public static Integer getInteger(String key,String ymlName){
            return Integer.valueOf(getStr(key,ymlName));
    }
    public static Long getLong(String key,String ymlName){
        return Long.valueOf(getStr(key,ymlName));
    }
      public static Double getDouble(String key,String ymlName){
            return Double.valueOf(getStr(key,ymlName));
        }

    public static Integer getInteger(String key) {
        return getInteger(key,null);
    }

    public static Long getLong(String key) {
        return getLong(key,null);
    }

    public static Double getDouble(String key) {
        return getDouble(key,null);
    }
}
