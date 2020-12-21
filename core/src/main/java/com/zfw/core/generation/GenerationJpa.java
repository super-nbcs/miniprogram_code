package com.zfw.core.generation;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @Author:zfw
 * @Date:2020/7/21
 * @Content: 自动生成jpa的dao,service,impl,controller
 */
public class GenerationJpa {
    private final static String service = "service";
    private final static String dao = "dao";
    private final static String impl = "impl";
    private final static String controller = "controller";

    public static void init(Class aClass, String auth, String content){
        for (DirType value : DirType.values()) {
            try {
                createDirAndFile(aClass,value ,auth,content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Path getClassPath(Class aClass){
        URL resource = aClass.getResource("");
        String targetAllPath = resource.toString();
        String javaAllPath = targetAllPath.replace("target/classes", "src/main/java");
        URI uri = null;
        try {
            uri = new URL(javaAllPath).toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return Paths.get(uri);

    }
    private static void createDirAndFile(Class aClass, DirType dirType, String auth, String content) throws IOException {
        Path classPath = getClassPath(aClass);
        Path parent = classPath.getParent();

        Path toFile=null;
        Path path = null;
        String fileName = null;
        String templatePath=null;
        switch (dirType){
            case DAO:
                path=Paths.get(parent.toString(), dao);
                fileName = String.format("I%sDao.java", aClass.getSimpleName());
                templatePath="dao/dao.ftl";
                break;
            case SERVICE:
                path=Paths.get(parent.toString(), service);
                fileName = String.format("I%sService.java", aClass.getSimpleName());
                templatePath="service/service.ftl";
                break;
            case IMP:
                path=Paths.get(parent.toString(), service, impl);
                fileName = String.format("%sServiceImpl.java", aClass.getSimpleName());
                templatePath="service/impl.ftl";
                break;
            case CONTROLLER:
                path=Paths.get(parent.toString(), controller);
                fileName = String.format("%sController.java", aClass.getSimpleName());
                templatePath="controller/controller.ftl";
                break;
                default:
                    break;
        }

        if (!path.toFile().exists()){
            Files.createDirectory(path);
        }
        toFile = Paths.get(path.toString(), fileName);
        if (!toFile.toFile().exists()){
            Files.createFile(toFile);
        }
        Map<String, Object> map = generationModel(aClass, dirType);
        Path fileName1 = toFile.getFileName();
        String s = fileName1.toString();
        String[] split = s.split("\\.");
        String value = split[0];
        map.put("fileName", value);
        map.put("auth", StringUtils.isBlank(auth)?System.getenv("USERDOMAIN"):auth);
        map.put("content", StringUtils.isBlank(content)?System.getenv("USERDOMAIN"):content);
        map.put("date",new Date());
        FreemarkerUtils.processToFile(map,templatePath,toFile.toFile());
        System.out.println(String.format("%s文件创建成功",value));
    }

    /**
     * 通过反射生成freemarker所需数据对象
     * @param aClass
     * @param dirType
     * @return
     */
    private static Map<String,Object> generationModel(Class aClass,DirType dirType){
        String entityAllPath = aClass.getName();
        String entityName = aClass.getSimpleName();
        String packageName = aClass.getPackage().getName();
        packageName=packageName.substring(0,packageName.lastIndexOf("."));
        Map<String,Object> modelMap=new HashMap<>();
        modelMap.put("entityAllPath",entityAllPath);
        modelMap.put("entityName",entityName);
        modelMap.put("entityPackage",entityAllPath);
        modelMap.putAll(fieldData(aClass));
        modelMap.put("basePackageName",packageName);
        switch (dirType){
            case DAO:
                packageName=String.format("%s.%s",packageName,dao);
                break;
            case SERVICE:
                packageName=String.format("%s.%s",packageName,service);
                break;
            case IMP:
                packageName=String.format("%s.%s.%s",packageName,service,impl);
                break;
            case CONTROLLER:
                packageName=String.format("%s.%s",packageName,controller);
                break;

                default:
                    break;
        }
        modelMap.put("packageName",packageName);
        return modelMap;
    }

    private static Map<String,Object> fieldData(Class aClass){
        Field[] fields = aClass.getDeclaredFields();
        Map<String, Object> objectObjectHashMap = new HashMap<>();
        Set<String> dataTypes=new HashSet<>();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column!=null){
                if (!field.getType().isPrimitive()){
                    dataTypes.add(field.getType().getName());
                }
                String[] split = field.getType().getName().split("\\.");
                objectObjectHashMap.put(field.getName(),split[split.length-1]);
            }
        }

        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("fields",objectObjectHashMap);
        stringObjectHashMap.put("dataTypes",dataTypes);
        return stringObjectHashMap;
    }


    public enum DirType{
        DAO,SERVICE,IMP,CONTROLLER
    }

}
