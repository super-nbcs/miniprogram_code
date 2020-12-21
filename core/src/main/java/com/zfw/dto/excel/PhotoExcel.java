package com.zfw.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

/**
 * 图片上传，反馈的excel信息
 */
@Data
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(20)
public class PhotoExcel extends ExcelContext {
    @ExcelProperty(index = 0,value="文件名")
    private String fileName;
    @ExcelProperty(index = 1,value = "备注")
    private String remark;

}
