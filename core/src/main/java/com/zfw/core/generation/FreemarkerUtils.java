package com.zfw.core.generation;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Map;

/**
 * @Author:zfw
 * @Date:2019/10/25
 * @Content:
 */
public class FreemarkerUtils {

    private static Logger logger = LoggerFactory.getLogger(FreemarkerUtils.class);

    private static Configuration getConfiguration(File fileDir) {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
            cfg.setDefaultEncoding("utf-8");
            cfg.setDirectoryForTemplateLoading(fileDir);
            cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_23));
            return cfg;
        } catch (IOException e) {
            logger.info("模板配置目录有问题,找不到模板目录:" + fileDir.getName());
            return null;
        }
    }

    /**
     * 模板文件
     *
     * @param fileDir
     * @param templateName
     * @return
     * @throws Exception
     */
    private static Template getTemplate(File fileDir, String templateName) {
        try {
            Configuration cfg = getConfiguration(fileDir);
            Template template = cfg.getTemplate(templateName, "utf-8");
            return template;
        } catch (Exception e) {
            logger.info("获取模板文件时出了问题,找不到模板文件：" + templateName);
            return null;
        }
    }

    public static void processToFile(Map<String, Object> dataModel, String templatePath,File toFile){
        try {
            Writer out = new FileWriter(toFile);
            File file = ResourceUtils.getFile("classpath:templates_generation_jpa/");

            Template template = getTemplate(file, templatePath);
            template.process(dataModel, out);
        } catch (FileNotFoundException e) {
            logger.info("在classpath下没有找到templates_generation_jpa/文件夹");
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
