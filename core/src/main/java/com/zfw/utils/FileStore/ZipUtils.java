package com.zfw.utils.FileStore;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 解压缩工具类，应急用，先用以前的，以前的通过apache ant实现，后面改成java8自带的工具类
 */
public class ZipUtils {
    private static Logger logger = LoggerFactory.getLogger(ZipUtils.class);


    private static String getSystemProperty() {
        String property = System.getProperty("os.name").toLowerCase();
        return property;
    }

    private static boolean isWindows() {
        return getSystemProperty().contains("windows") ? true : false;
    }
    private static String createPath(String windowPath,String linuxPath) {
        String s = isWindows() ? YamlConfigurerUtils.getStr(windowPath) : YamlConfigurerUtils.getStr(linuxPath);
        Path directories = null;
        try {
            directories = Files.createDirectories(Paths.get(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return directories.toString();
    }

    /**
     * 根据系统，获取并创建系统压缩文件目录
     * @return
     */
    public static String systemZipParentPath(){
        return createPath("fileStore.zip.windows","fileStore.zip.linux");
    }

    /**
     * 根据系统，获取并创建系统解压文件目录
     * @return
     */
    public static String systemUnzipParentPath(){
        return createPath("fileStore.unzip.windows","fileStore.unzip.linux");
    }

    /**
     * 上传zip文件,解压，并返回文件所有压缩文件
     * @param file
     * @throws IOException
     */
    public static List<File> uploadZip(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String s = systemZipParentPath() + File.separator + originalFilename;
        Files.copy(file.getInputStream(), Paths.get(s), StandardCopyOption.REPLACE_EXISTING);
        String upZipDir = systemUnzipParentPath() + File.separator + FileStoreUtils.getFileNameWithoutExtension(originalFilename);
        Files.createDirectories(Paths.get(upZipDir));
        unZipFiles(s, upZipDir);
        List<File> fileList = new ArrayList<>();
        getFiles(upZipDir, fileList);
        return fileList;
    }

    /**
     * 解压缩ZIP文件，将ZIP文件里的内容解压到descFileName目录下，TODO 此处会造成内存溢出
     * @param zipFileName 需要解压的ZIP文件
     * @param descFileName 目标文件
     */
    public static boolean unZipFiles(String zipFileName, String descFileName) {
        String descFileNames = descFileName;
        if (!descFileNames.endsWith(File.separator)) {
            descFileNames = descFileNames + File.separator;
        }
        try {
            // 根据ZIP文件创建ZipFile对象
            ZipFile zipFile = new ZipFile(zipFileName);
            ZipEntry entry = null;
            String entryName = null;
            String descFileDir = null;
            byte[] buf = new byte[4096];
            int readByte = 0;
            // 获取ZIP文件里所有的entry
            @SuppressWarnings("rawtypes")
            Enumeration enums = zipFile.getEntries();
            // 遍历所有entry
            while (enums.hasMoreElements()) {
                entry = (ZipEntry) enums.nextElement();
                // 获得entry的名字
                entryName = entry.getName();
                descFileDir = descFileNames + entryName;
                if (entry.isDirectory()) {
                    // 如果entry是一个目录，则创建目录
                    new File(descFileDir).mkdirs();
                    continue;
                } else {
                    // 如果entry是一个文件，则创建父目录
                    new File(descFileDir).getParentFile().mkdirs();
                }
                File file = new File(descFileDir);
                // 打开文件输出流
                OutputStream os = new FileOutputStream(file);
                // 从ZipFile对象中打开entry的输入流
                InputStream is = zipFile.getInputStream(entry);
                while ((readByte = is.read(buf)) != -1) {
                    os.write(buf, 0, readByte);
                }
                os.close();
                is.close();
            }
            zipFile.close();
            logger.debug("文件解压成功!");
            return true;
        } catch (Exception e) {
            logger.debug("文件解压失败：" + e.getMessage());
            return false;
        }
    }

    /**
     * 根据指定路径获取所有的文件
     * @param path
     * @return
     */
    public static List<File> getFiles(String path, List<File> fileList){
        File file = new File(path);
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File fileIndex:files){
                if(fileIndex.isDirectory()){
                    //如果这个文件是目录，则进行递归搜索
                    getFiles(fileIndex.getPath(),fileList);
                }else {
                    //如果文件是普通文件，则将文件句柄放入集合中
                    fileList.add(fileIndex);
                }
            }
        }
        return  fileList;
    }


}
