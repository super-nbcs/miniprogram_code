package com.zfw.utils;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Author:zfw
 * @Date:2019/7/16
 * @Content:
 */
@Component
public class CaptchaUtils {
    // 验证码字符集 TODO 加到数据字典中，系统可配置
    public  final static String wordData="ABCDEFGHJKLMNRSTUWXY235689";
    // 字符数量 TODO 加到数据字典中，系统可配置
    private static final int SIZE = 4;
    // 干扰线数量 TODO 加到数据字典中，系统可配置
    private static final int LINES = 5;
    // 宽度 TODO 加到数据字典中，系统可配置
    private static final int IMAGE_WIDTH = 80;
    // 高度 TODO 加到数据字典中，系统可配置
    private static final int IMAGE_HEIGHT = 35;
    // 字体大小 TODO 加到数据字典中，系统可配置
    private static final int FONT_SIZE = 20;

    /**
     * 随机取色
     * @return
     */
    public static Color getRandomColor() {
        Random ran = new Random();
        Color color = new Color(ran.nextInt(256), ran.nextInt(256),
                ran.nextInt(256));
        return color;
    }
    public Map<String,BufferedImage> getImageCaptcha() {
        StringBuffer sb = new StringBuffer();
        // 1.创建空白图片
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        // 2.获取图片画笔
        Graphics graphic = image.getGraphics();
        // 3.设置画笔颜色
        graphic.setColor(Color.LIGHT_GRAY);
        // 4.绘制矩形背景
        graphic.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        // 5.随机字符
        Random ran = new SecureRandom();
        char[] chars=wordData.toCharArray();
        for (int i = 0; i < SIZE; i++) {
            // 取随机字符索引
            int n = ran.nextInt(chars.length);
            // 设置随机颜色
            graphic.setColor(new Color(0, 0, 80));
            // 设置字体大小
            graphic.setFont(new Font(null, Font.BOLD + Font.ITALIC, FONT_SIZE));
            // 画字符
            graphic.drawString(chars[n] + "", i * IMAGE_WIDTH / SIZE,
                    IMAGE_HEIGHT * 2 / 3);
            // 记录字符
            sb.append(chars[n]);
        }
        // 6.画干扰线
        for (int i = 0; i < LINES; i++) {
            // 设置随机颜色
            graphic.setColor(getRandomColor());
            // 随机画线
            graphic.drawLine(ran.nextInt(IMAGE_WIDTH),
                    ran.nextInt(IMAGE_HEIGHT), ran.nextInt(IMAGE_WIDTH),
                    ran.nextInt(IMAGE_HEIGHT));
        }
        Map<String, BufferedImage> map = new HashMap<>();
        map.put(sb.toString(),image);
        return map;
    }

}
