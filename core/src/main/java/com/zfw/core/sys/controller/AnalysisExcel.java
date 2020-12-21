package com.zfw.core.sys.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.event.SyncReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.zfw.core.exception.GlobalException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * @Author:zfw
 * @Date:2020-08-20
 * @Content: 解析excel
 */
public interface AnalysisExcel {
    /**
     * 校验excel表格是否是从系统下载的
     * @param sheet 第几个工作表，从0开始
     * @param checkCode 校验码
     */
//    default void checkExcel(MultipartFile file,Integer sheet,String checkCode) throws IOException {
//        List<CheckExcel> checkExcels = analysisExcel(file, CheckExcel.class, sheet);
//        if (checkExcels.size()==0||!StringUtils.equals(checkExcels.get(0).getCheckCode(),checkCode)){
//            throw new IOException("系统校验，此表格不对");
//        }
//
//    }
    /**
     * 导入excel表格,解析数据
     *
     * @param file
     */
    /**
     * @param file     上传文件
     * @param tClass  接收实体类
     * @param sheet  第几个Sheet  从0开始
     * @param <T>
     * @return
     */
    default <T> List<T> analysisExcel(MultipartFile file, Class<T> tClass, Integer sheet) {
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename().toLowerCase();
            if (StringUtils.endsWithAny(fileName, "xlsx", "xls")) {
                List<Object> datum = EasyExcel.read(inputStream, tClass, new SyncReadListener()).sheet(sheet).headRowNumber(1).autoTrim(true).doReadSync();
                return (List<T>) datum;
            } else {
                throw new GlobalException("请上传标准的excel文件");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new GlobalException("excel文件读取异常");
        }
    }

    /**
     * 从excel表格中获取所有的sheetName TODO 没有读出来
     *
     * @param file
     * @return
     */
    default String getSheetName(MultipartFile file, int sheetNo) {
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename().toLowerCase();
            if (StringUtils.endsWithAny(fileName, "xlsx", "xls")) {
                ReadSheet build = EasyExcel.read(inputStream).sheet(sheetNo).build();
                String sheetName = build.getSheetName();
                return sheetName;
            } else {
                throw new GlobalException("请上传标准的excel文件");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new GlobalException("excel文件读取异常");

        }
    }


    /**
     * 导出Excel，所有导出都走这一个
     *
     * @param response
     * @param clazz    类
     * @param fileName 文件名
     * @param sheet    表格
     * @param data
     */
    default void exportExcel(HttpServletResponse response, Class<?> clazz, String fileName, String sheet, List<?> data) {
        ServletOutputStream outputStream=null;

        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            if (!fileName.endsWith("xls")) {
                fileName = String.format("%s.xls", fileName);
            }
            response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
            EasyExcel.write(outputStream, clazz).sheet(sheet).doWrite(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载excel 文件
     *
     * @param response
     * @param file
     */
    default void excelDownload(HttpServletResponse response, File file) {
        InputStream in = null;
        OutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(file.getName(), "UTF-8"));
            in = new FileInputStream(file);
            out = response.getOutputStream();

            int count = 0;
            byte[] by = new byte[1024];
            while ((count = in.read(by)) != -1) {
                out.write(by, 0, count);
            }
            in.close();
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            close(in,out);
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            close(in,out);
            e.printStackTrace();
        } catch (IOException e) {
            close(in,out);
            e.printStackTrace();
        }
    }
    default void close(InputStream in,OutputStream out){
        if (in!=null){
            try {
                in.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (out!=null){
            try {
                out.flush();
                out.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
    class CheckExcel{
        public String getCheckCode() {
            return checkCode;
        }

        public void setCheckCode(String checkCode) {
            this.checkCode = checkCode;
        }
        //校验码
        private String checkCode;
    }
}
