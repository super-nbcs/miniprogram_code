package com.zfw.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

/**
 * @Author:zfw
 * @Date:2019/9/25
 * @Content:
 */
@Data
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(20)
public class PhotoExcelError {

    @ExcelProperty(index = 0,value="照片名称")
    private String photoName;

    @ExcelProperty(index = 1,value = "类型")
    private String type;

    @ExcelProperty(index = 2,value = "错误信息")
    private String message;

    @ExcelProperty(index = 3,value = "其他")
    private String other;

}
