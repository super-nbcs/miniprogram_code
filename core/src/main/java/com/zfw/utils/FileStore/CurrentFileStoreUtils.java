package com.zfw.utils.FileStore;


import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @Author:zfw
 * @Date:2019/12/10
 * @Content: 通行记录图片存储工具类
 */
public class CurrentFileStoreUtils {
    private static Logger logger = LoggerFactory.getLogger(CurrentFileStoreUtils.class);


    private static String getSystemProperty() {
        String property = System.getProperty("os.name").toLowerCase();
        return property;
    }

    private static boolean isWindows() {
        return getSystemProperty().contains("windows") ? true : false;
    }

    public static String systemParentPath() {
        String s = isWindows() ? YamlConfigurerUtils.getStr("fileStore.dir.windows") : YamlConfigurerUtils.getStr("fileStore.dir.linux");
        File file = new File(s);
        return file.toString();
    }

    private static String createAndGetDirs() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String year = String.format("%02d", calendar.get(Calendar.YEAR));
        String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
        String day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        String separator = File.separator;
        Path path = Paths.get(systemParentPath() +separator+"current"+ separator + year + separator + month + separator + day + separator);
        Path directories = null;
        try {
            directories = Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return directories.toString();
    }

    /**
     * 创建一个空文件
     *
     * @param fileType 文件后缀名
     * @return
     * @throws IOException
     */
    public static Path createEmptyFile(String fileType, boolean isTemp) throws IOException {
        String s = UUID.randomUUID().toString().replaceAll("-", "");
        if (!isBlank(fileType)) {
            s = s + (char) 46 + fileType;
        }
        Path source = Paths.get(createAndGetDirs(), s);
        if (!Files.exists(source)) {
            Files.createFile(source);
        }
        if (!isTemp) {
            source.toFile().deleteOnExit();
        }
        return source;
    }


    /**
     * 删除文件
     *
     * @param filePath 传createFile方法返回的路径，相对路径
     * @return
     */
    public static boolean deleteFile(String filePath) {
        boolean is = false;
        if (isBlank(filePath)) {
            return false;
        }
        Path path = Paths.get(systemParentPath(), filePath);
        try {
            is = Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("没有这个文件：" + path);
        }
        return is;
    }


    /**
     * 传入文件名，获取文件后缀
     *
     * @param filename 如：filename  1.jpg   2.tar.gz
     * @return 如：            jpg     tar.gz
     */

    private static String getExtensions(String filename) {
        if (filename.contains(".")) {
            int i = filename.indexOf(".");
            return filename.substring(i + 1, filename.length());
        } else {
            return "";
        }
    }

    /**
     * 传入文件名，获取文件后缀
     *
     * @param filename 如：filename  1.jpg   2.tar.gz
     * @return 如：            jpg     gz
     */
    private static String getExtension(String filename) {
        if (filename == null) {
            return null;
        } else {
            int index = indexOfExtension(filename);
            return index == -1 ? "" : filename.substring(index + 1);
        }
    }

    private static int indexOfExtension(String filename) {
        if (filename == null) {
            return -1;
        } else {
            int extensionPos = filename.lastIndexOf(46);
            int lastSeparator = indexOfLastSeparator(filename);
            return lastSeparator > extensionPos ? -1 : extensionPos;
        }
    }

    private static int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        } else {
            int lastUnixPos = filename.lastIndexOf(47);
            int lastWindowsPos = filename.lastIndexOf(92);
            return Math.max(lastUnixPos, lastWindowsPos);
        }
    }

    private static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * ---------结束-----------
     **/
    private static boolean isBase64(String base64) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, base64);
    }

    /**
     * @param base64   不带前缀
     * @param filePath
     * @return
     */
    private static Boolean decryptByBase64(String base64, Path filePath) {
        if (StringUtils.isBlank(base64) && Strings.isBlank(filePath.toString())) {
            return Boolean.FALSE;
        }
        try {
            byte[] bytes = Base64.getDecoder().decode(base64.split(",")[1]);
            Files.write(filePath, bytes, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.TRUE;
    }


    /**---------开始-----------**/

    /**
     * 上传文件到本地
     *
     * @param file
     * @return
     * @throws IOException
     */
    private static String createFile(MultipartFile file, boolean isTemp) throws IOException {
        if (file == null) {
            throw new IOException("file不能为null");
        }
        return createFile(file.getInputStream(), getExtensions(file.getOriginalFilename()), isTemp);
    }

    /**
     * 上传文件到本地
     *
     * @param file
     * @param isTemp 是否为临时文件
     * @return
     * @throws IOException
     */
    private static String createFile(File file, boolean isTemp) throws IOException {
        if (file == null) {
            throw new IOException("file不能为null");
        }
        return createFile(new FileInputStream(file), getExtensions(file.getName()), isTemp);
    }


    /**
     * 上传文件
     *
     * @param inputStream
     * @param fileType    需指定文件后缀 如：jpa , png
     * @param isTemp      是否为临时文件
     * @return
     * @throws IOException
     */
    private static String createFile(InputStream inputStream, String fileType, boolean isTemp) throws IOException {
        if (inputStream == null) {
            throw new IOException("inputStream不能为null");
        }
        Path source = createEmptyFile(fileType, isTemp);
        Files.copy(inputStream, source, StandardCopyOption.REPLACE_EXISTING);
        String filePath = source.toString().replace(systemParentPath(), "").replace("\\", "/");
        return filePath;
    }


    /**
     * base64存图片
     *
     * @param base64
     * @param isTemp 是否为临时文件
     * @return
     * @throws IOException
     */
    private static String createFile(String base64, boolean isTemp) throws IOException {
        if (StringUtils.isBlank(base64)) {
            throw new IOException("base64字符串不能为空");
        }
        return base64.indexOf(",") == -1 ? createFileNoPrefix(base64, isTemp) : createFilePrefix(base64, isTemp);
    }

    /**
     * 无前缀的base64文件
     *
     * @param base64NoPrefix
     * @param isTemp         是否为临时文件
     * @return
     * @throws IOException
     */
    private static String createFileNoPrefix(String base64NoPrefix, boolean isTemp) throws IOException {
        if (StringUtils.isBlank(base64NoPrefix)) {
            throw new IOException("base64字符串不能为空");
        }
        String base64Prefix = String.format("data:image/jpg;base64,%s", base64NoPrefix);
        return createFilePrefix(base64Prefix, isTemp);
    }

    /**
     * 有前缀的base64文件
     *
     * @param base64Prefix
     * @return
     * @throws IOException
     */
    private static String createFilePrefix(String base64Prefix, boolean isTemp) throws IOException {
        if (isBase64(base64Prefix)) {
            throw new IOException("此字符串不是标准的base64编码");
        }
        String fileType = base64Prefix.split(",")[0].split(";")[0].split("/")[1];
        Path emptyFile = createEmptyFile(fileType, isTemp);
        Boolean aBoolean = decryptByBase64(base64Prefix, emptyFile);
        if (aBoolean) {
            return emptyFile.toString().replace(systemParentPath(), "").replace("\\", "/");
        }
        return null;
    }

    /**
     * 上传文件到本地 永久保存在本地
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String createFile(File file) throws IOException {
        return createFile(file, true);
    }

    /**
     * 上传临时文件到本地 系统退出时自动删除
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String createTempFile(File file) throws IOException {
        return createFile(file, false);
    }

    /**
     * 上传文件到本地 永久保存在本地
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String createFile(MultipartFile file) throws IOException {
        return createFile(file, true);
    }

    /**
     * 上传临时文件到本地 系统退出时自动删除
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String createTempFile(MultipartFile file) throws IOException {
        return createFile(file, false);
    }

    /**
     * 上传文件到本地 永久保存在本地
     *
     * @param is
     * @param fileType 文件类型
     * @return
     * @throws IOException
     */
    public static String createFile(InputStream is, String fileType) throws IOException {
        return createFile(is, fileType, true);
    }

    /**
     * 上传临时文件到本地 系统退出时自动删除
     *
     * @param is
     * @param fileType
     * @return
     * @throws IOException
     */
    public static String createTempFile(InputStream is, String fileType) throws IOException {
        return createFile(is, fileType, false);
    }

    /**
     * 上传base64永久保存到本地
     *
     * @param base64 字符串
     * @return
     * @throws IOException
     */
    public static String createFile(String base64) throws IOException {
        return createFile(base64, true);
    }

    /**
     * 上传临时文件到本地 系统退出时自动删除
     *
     * @param base64
     * @return
     * @throws IOException
     */
    public static String createTempFile(String base64) throws IOException {
        return createFile(base64, false);
    }

    /**---------结束-----------**/

    /**--------文件转换————**/

    /**
     * 文件转base64
     *
     * @param file
     * @return 返回的base64 不带前缀
     */
    public static String file2Base64(File file) throws IOException {
        if (file == null)
            throw new IOException("文件不能为null");
        byte[] b = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(b);
    }

    /**
     * 文件路径转成base64
     *
     * @param filePath 可以传全路径，可传相对路径（相对于配置文件中的fileStore.dir.windows|linux的路径）
     * @return 返回的base64 不带前缀
     * @throws IOException
     */
    public static String file2Base64(String filePath) throws IOException {
        if (StringUtils.isBlank(filePath))
            throw new IOException("路径不为空");
        if (!filePath.startsWith(systemParentPath()))
            filePath = systemParentPath() + filePath;
        byte[] b = Files.readAllBytes(Paths.get(filePath));
        return Base64.getEncoder().encodeToString(b);
    }

    /**
     * base64转字节
     *
     * @param base64
     * @return
     */
    public static byte[] base64Tobytes(String base64) throws IOException {
        if (StringUtils.isBlank(base64)){
            throw new IOException("base64不能为空");
        }
        byte[] decode = Base64.getDecoder().decode(base64.indexOf(",")==-1?base64:base64.split(",")[1]);
        return decode;
    }

    /**
     * 字节转base64
     * @param bytes
     * @return 返回的base64 不带前缀
     * @throws IOException
     */
    public static String bytes2Base64(byte[] bytes) throws IOException {
        if (bytes==null||bytes.length==0){
            throw new IOException("bytes字节不能为空");
        }
        String s = Base64.getEncoder().encodeToString(bytes);
        return s;
    }

    /**
     * 判断文件是不是视频
     *
     * @param file
     * @return
     */
    public static boolean isVideo(MultipartFile file) {
        return file.getContentType().contains("video");
    }

    /**
     *  判断文件是不是图片
     * @param file
     * @return
     */
    public static boolean isPhoto(MultipartFile file) {
        return file.getContentType().contains("image");
    }

    /**
     * nuc加密方式不一样，所以单写一个
     * @param base64Prefix
     * @param isTemp
     * @return
     * @throws IOException
     */
    public static String createFilePrefixForNuc(String base64Prefix, boolean isTemp) throws IOException {
        if (isBase64(base64Prefix)) {
            throw new IOException("此字符串不是标准的base64编码");
        }
        String fileType = base64Prefix.split(",")[0].split(";")[0].split("/")[1];
        Path emptyFile = createEmptyFile(fileType, isTemp);
        Boolean aBoolean = decryptByBase64ForNuc(base64Prefix, emptyFile);
        if (aBoolean) {
            return emptyFile.toString().replace(systemParentPath(), "").replace("\\", "/");
        }
        return null;
    }
    private static Boolean decryptByBase64ForNuc(String base64, Path filePath) {
        if (StringUtils.isBlank(base64) && Strings.isBlank(filePath.toString())) {
            return Boolean.FALSE;
        }
        try {
            byte[] bytes = Base64.getMimeDecoder().decode(base64);
            Files.write(filePath, bytes, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.TRUE;
    }

}
