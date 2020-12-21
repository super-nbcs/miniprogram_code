package com.zfw.utils.FileStore;

import com.zfw.core.exception.GlobalException;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片压缩工具类
 */
public class ImageUtils {
    // 指定压缩的大小
    private static Long size = YamlConfigurerUtils.getLong("image.size");
    // 指定像素宽
    private static Integer width = YamlConfigurerUtils.getInteger("image.width");
    // 指定像素高
    private static Integer height = YamlConfigurerUtils.getInteger("image.height");
    // 指定默认压缩图片质量 如压缩后不能小于size，在原质量上继续降低
    private static Double quality = YamlConfigurerUtils.getDouble("image.quality");
    /**
     * 图片转buffImage
     *
     * @param file
     * @return
     */
    private static BufferedImage file2BufferedImage(File file) {
        BufferedImage bf = null;
        try {
            bf = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bf;
    }

    /**
     * 输入流转buffedImage
     *
     * @param is
     * @return
     */
    private static BufferedImage inputStream2BuffedImage(InputStream is) {
        BufferedImage bf = null;
        try {
            bf = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bf;
    }


    private static int getWidth(File image) {
        return file2BufferedImage(image).getWidth();
    }

    private static int getHeight(File image) {
        return file2BufferedImage(image).getHeight();
    }

    private static int getWidth(InputStream imageInputStream) {
        return inputStream2BuffedImage(imageInputStream).getWidth();
    }

    private static int getHeight(InputStream imageInputStream) {
        return inputStream2BuffedImage(imageInputStream).getHeight();
    }

    /**
     * 压缩像素
     * @param image
     * @param w 宽
     * @param h 高
     * @throws IOException
     */
    private static void image2Size(File image, int w, int h) throws IOException {
        BufferedImage bufferedImage = file2BufferedImage(image);
        int height = bufferedImage.getHeight();
        int width = bufferedImage.getWidth();
        if (height<=h&&width<=w){
            return;
        }
        width = width > w ? w : width;
        height = height > h ? h : height;
        Thumbnails.of(image).size(width, height).toFile(image);
    }


    /**
     * 递归压缩
     *
     * @param file    需要压缩的图片
     * @param size    指定压缩到的大小   如不指定，默认为原图的一半大小
     * @param quality 压缩质量比 最好指定为0.9 如不指定，默认为0.9
     * @throws IOException
     */
    private static void compress(File file, Long size, Double quality) throws IOException {
        quality = quality == null ? 0.90d : quality;
        size = size == null ? file.length() / 2 : size;
        if (file.length() <= size) {
            return;
        } else {
            quality -= 0.05d;
            BufferedImage bufferedImage = file2BufferedImage(file);
            Thumbnails.of(file).size(bufferedImage.getWidth(), bufferedImage.getHeight()).outputQuality(quality).toFile(file);
            compress(file, size, quality);
        }
    }


    public static void main(String[] args) {
        compress(new File("H://1.jpg"));
    }
    public static void compress(File file){
        try {
            image2Size(file,width,height);
            compress(file,size,quality);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩文件路径
     * @param filePath 可以传全路径，可传相对路径（相对于配置文件中的fileStore.dir.windows|linux的路径）
     */
    public static void compress(String filePath){
        if (StringUtils.isBlank(filePath))
            throw new GlobalException("路径不能为空");
        String prefix = FileStoreUtils.systemParentPath();
        if (!filePath.startsWith(prefix))
            filePath = prefix + filePath;
        compress(new File(filePath));
    }
    /**
     * 获取上传文件后缀名
     *
     * @param file
     * @return
     */
    public static String fileType(MultipartFile file) {
        return file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
    }


}
